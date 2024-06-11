
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

// Assume DelegateExecution is also a mock object that can be derived from the mockito library.
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> testDelegate;
    private DelegateExecution mockExecution;
    private Future<Object> future;

    @Before
    public void setUp() {
        testDelegate = mock(BaseRuntimeFlowableDelegate.class);
        mockExecution = mock(DelegateExecution.class);
    }

    @Test
    public void testRunMethod() throws Exception {
        // normal case
        when(testDelegate.isDone()).thenReturn(true);
        testDelegate.run(mockExecution);
        assertEquals(true, testDelegate.isDone());

        // exception case
        doThrow(new ServiceTaskError()).when(testDelegate).run(mockExecution);
        testDelegate.run(mockExecution);
        assertTrue(testDelegate.get() instanceof ServiceTaskError);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithExceptionCase() throws InterruptedException, ExecutionException {
        // exception case, task is not done and an exception is thrown when get is called
        when(testDelegate.isDone()).thenReturn(false);
        testDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutExceptionCase() throws InterruptedException, ExecutionException, TimeoutException {
        // exception case, timeout case
        when(testDelegate.isDone()).thenReturn(false);
        testDelegate.get(1000, TimeUnit.MILLISECONDS);
    }
}
