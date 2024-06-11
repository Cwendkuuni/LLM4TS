
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    private TestRuntimeFlowableDelegate testRuntimeFlowableDelegate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testRuntimeFlowableDelegate = new TestRuntimeFlowableDelegate();
    }

    @Test
    public void testRun() throws Exception {
        doReturn("ActivityId").when(delegateExecution).getCurrentActivityId();
        testRuntimeFlowableDelegate.run(delegateExecution);
        assertTrue(testRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testIsDone() throws InterruptedException {
        assertFalse(testRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGetAfterRun() throws Exception {
        assertNull(testRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeoutAfterRun() throws InterruptedException, ExecutionException, TimeoutException {
        assertNull(testRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testExecute() {
        testRuntimeFlowableDelegate.execute(delegateExecution);
        verify(delegateExecution, times(1)).setVariable(eq("run"), anyString());
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Callable";
        }
    }

}
