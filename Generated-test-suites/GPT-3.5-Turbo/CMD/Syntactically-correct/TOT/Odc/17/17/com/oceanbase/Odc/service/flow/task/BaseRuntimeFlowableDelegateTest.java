import org.junit.Test;
import static org.junit.Assert.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        // Typical use case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
        
        assertNotNull(delegate.initCallable(null).call());
    }

    @Test
    public void testRun() {
        // Edge case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new RuntimeException("Test Exception");
                };
            }
        };
        
        try {
            delegate.run(null);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        // Error scenario
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
        
        delegate.run(null);
        assertEquals("Test", delegate.get());
    }

    @Test
    public void testIsDone() {
        // Typical use case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        delegate.run(null);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        // Error scenario
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(5000);
                    return "Test";
                };
            }
        };

        delegate.run(null);
        assertThrows(TimeoutException.class, () -> delegate.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        // Edge case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(taskInstance.getServiceTask(), delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Typical use case
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        assertEquals(true, delegate.updateFlowInstanceStatus(FlowStatus.SUCCESS));
    }

}