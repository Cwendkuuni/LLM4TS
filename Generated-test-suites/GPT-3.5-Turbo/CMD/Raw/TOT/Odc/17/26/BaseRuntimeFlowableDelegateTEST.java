import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> testInstance;
    private DelegateExecution execution;

    @Before
    public void setUp() {
        testInstance = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        });
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testInitCallable() {
        Callable<Object> callable = testInstance.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    public void testRun() {
        try {
            testInstance.run(execution);
            assertTrue(testInstance.isDone());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGet() {
        final Object expectedResult = new Object();
        testInstance.run(execution);
        testInstance.returnObject = expectedResult;

        try {
            Object result = testInstance.get();
            assertEquals(expectedResult, result);
        } catch (InterruptedException | ExecutionException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetWithTimeout() {
        final Object expectedResult = new Object();
        testInstance.run(execution);
        testInstance.returnObject = expectedResult;

        try {
            Object result = testInstance.get(1, java.util.concurrent.TimeUnit.MILLISECONDS);
            assertEquals(expectedResult, result);
        } catch (InterruptedException | ExecutionException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testExecute() {
        try {
            testInstance.execute(execution);
            Mockito.verify(testInstance, Mockito.times(1)).execute(execution);
        } finally {
            Mockito.verify(testInstance, Mockito.times(1)).eventPublisher.removeEventListener(Mockito.any());
        }
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        testInstance.flowInstanceId = 1L;
        testInstance.retryExecutor = new RetryExecutor.Builder()
                .retryIntervalMillis(100)
                .retryTimes(3)
                .build();
        Mockito.when(testInstance.flowInstanceRepository.updateStatusById(Mockito.anyLong(), Mockito.any()))
                .thenReturn(true);

        testInstance.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        Mockito.verify(testInstance.flowInstanceRepository, Mockito.times(1))
                .updateStatusById(testInstance.flowInstanceId, FlowStatus.COMPLETED);
    }

}