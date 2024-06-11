import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;

    @Before
    public void setUp() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testInitCallable() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        delegate.execute(execution);
        String result = delegate.get();
        assertNotNull(result);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        delegate.execute(execution);
        doThrow(new Exception()).when(delegate).get();
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        delegate.execute(execution);
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    // Add more test cases for other public methods

}