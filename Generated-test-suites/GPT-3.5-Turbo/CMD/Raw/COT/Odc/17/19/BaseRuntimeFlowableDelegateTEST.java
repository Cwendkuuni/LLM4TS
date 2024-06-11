import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        Mockito.when(delegate.initCallable(execution)).thenReturn(() -> "Test Result");

        Callable<String> callable = delegate.initCallable(execution);
        String result = callable.call();

        assertEquals("Test Result", result);
    }

    @Test
    public void testPreHandle() {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        Mockito.doNothing().when(delegate).preHandle(execution);

        delegate.preHandle(execution);

        Mockito.verify(delegate, Mockito.times(1)).preHandle(execution);
    }

    // Add more test cases for other public methods

}