import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<TestObject> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<TestObject>() {
            @Override
            protected Callable<TestObject> initCallable(DelegateExecution execution) {
                return () -> new TestObject();
            }
        };
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));

        TestObject result = baseRuntimeFlowableDelegate.get();

        assertNotNull(result);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));

        TestObject result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);

        assertNotNull(result);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));

        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

}

class TestObject {}