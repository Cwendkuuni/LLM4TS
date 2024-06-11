
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;
import java.util.concurrent.ExecutionException;

import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class BaseRuntimeFlowableDelegateTest {

    BaseRuntimeFlowableDelegate target;

    @Before
    public void setup(){
        target = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable expectedCallable = Mockito.mock(Callable.class);
        
        Mockito.when(target.initCallable(execution)).thenReturn(expectedCallable);

        Callable actualCallable = target.initCallable(execution);

        assertEquals(expectedCallable, actualCallable);
    }

    @Test(expected = Exception.class)
    public void testExecute_ThrowsException() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        doThrow(new Exception()).when(target).execute(execution);

        target.execute(execution);
    }
}
