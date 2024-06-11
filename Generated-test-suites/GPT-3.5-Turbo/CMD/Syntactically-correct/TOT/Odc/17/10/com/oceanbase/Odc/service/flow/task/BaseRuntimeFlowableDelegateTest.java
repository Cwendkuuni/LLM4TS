import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegateMock = new BaseRuntimeFlowableDelegate<String>() {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Callable";
        }
    };

    @Before
    public void setup() {
        baseRuntimeFlowableDelegateMock.init(eventPublisher);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = baseRuntimeFlowableDelegateMock.initCallable(execution);
        assertNotNull(callable);
        assertEquals("Test Callable", callable.call());
    }

    @Test
    public void testRun_Success() {
        baseRuntimeFlowableDelegateMock.run(execution);
        assertTrue(baseRuntimeFlowableDelegateMock.isDone());
        try {
            assertEquals("Test Callable", baseRuntimeFlowableDelegateMock.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("Exception occurred");
        }
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout_ThrowsTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegateMock.run(execution);
        baseRuntimeFlowableDelegateMock.get(1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException, TimeoutException {
        doThrow(new Exception()).when(execution).getCurrentActivityId();
        baseRuntimeFlowableDelegateMock.run(execution);
        baseRuntimeFlowableDelegateMock.get();
    }

}