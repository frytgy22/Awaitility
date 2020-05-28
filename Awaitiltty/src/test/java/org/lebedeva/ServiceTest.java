package org.lebedeva;

import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.awaitility.Awaitility.*;
import static org.awaitility.proxy.AwaitilityClassProxy.to;
import static org.hamcrest.CoreMatchers.equalTo;

public class ServiceTest {

    private AsyncService asyncService;

    @Before
    public void setUp() {
        asyncService = new AsyncService();
    }

//    Awaitility.setDefaultPollInterval(10,TimeUnit.MILLISECONDS);
//    Awaitility.setDefaultPollDelay(Duration.ZERO);
//    Awaitility.setDefaultTimeout(Duration.ONE__MINUTE);

    @Test
    public void test() {
        //тест проверяет, происходит ли инициализация нашего сервиса в течение указанного периода времени (по умолчанию 10 с) после вызова метода initialize .
        asyncService.initialize();
        ;

        await()
                .atLeast(Duration.ONE_HUNDRED_MILLISECONDS)
                .atMost(Duration.FIVE_SECONDS)
                .with()
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(asyncService::isInitialized);
    }

    @Test
    public void test2() {
        asyncService.initialize();

        await()
                .until(asyncService::isInitialized);

        long value = 5;
        asyncService.addValue(value);
        await()
                .until(asyncService::getValue, equalTo(value));
    }

    @Test
    public void test3() {
        asyncService.initialize();

        given().ignoreException(IllegalStateException.class)
                .await().atMost(Duration.FIVE_SECONDS)
                .atLeast(Duration.FIVE_HUNDRED_MILLISECONDS)
                .until(asyncService::getValue, equalTo(0L));
    }

    @Test
    public void test4() {
        asyncService.initialize();
        await()
                .untilCall(to(asyncService).isInitialized(), equalTo(true));
    }

    @Test
    public void test5() {
        //Awaitility может даже получить доступ к приватным полям, чтобы выполнять над ними проверки.
        //В следующем примере мы можем увидеть другой способ получить статус инициализации нашего сервиса:
        asyncService.initialize();
        await()
                .until(fieldIn(asyncService)
                        .ofType(boolean.class)
                        .andWithName("initialized"), equalTo(true));
    }
}
