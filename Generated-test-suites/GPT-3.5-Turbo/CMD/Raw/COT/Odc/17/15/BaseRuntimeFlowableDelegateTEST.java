
import org.junit.Test;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BaseRuntimeFlowableDelegateTest {
    
    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "test";
            }
        };
        
        Callable<String> callable = delegate.initCallable(null);
        assertNotNull(callable);
        try {
            String result = callable.call();
            assertEquals("test", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        assertFalse(delegate.isDone());
        delegate.execute(null);
        assertTrue(delegate.isDone());
    }
    
    @Test
    public void testGet() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        
        delegate.execute(null);
        try {
            String result = delegate.get();
            assertEquals("result", result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetWithTimeout() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        
        delegate.execute(null);
        try {
            String result = delegate.get(1, TimeUnit.SECONDS);
            assertEquals("result", result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
