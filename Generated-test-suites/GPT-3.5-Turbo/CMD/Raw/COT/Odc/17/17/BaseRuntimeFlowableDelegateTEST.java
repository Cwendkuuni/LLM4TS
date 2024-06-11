import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<Integer> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.initCallable(any())).thenReturn(() -> 10);

        Callable<Integer> callable = delegate.initCallable(mock(DelegateExecution.class));
        assertEquals(10, callable.call());
    }

    // Add more test cases for other public methods if needed

}