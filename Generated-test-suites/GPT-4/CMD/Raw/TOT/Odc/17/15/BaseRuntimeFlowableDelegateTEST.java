import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.flowable.engine.delegate.DelegateExecution;
import java.util.concurrent.ExecutionException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate baseDeligate;
    private DelegateExecution delegateExecution;

    @Before
    public void setUp() throws Exception {
        baseDeligate = mock(BaseRuntimeFlowableDelegate.class);
        delegateExecution = mock(DelegateExecution.class);
        
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test_activity_Id");
        doNothing().when(baseDeligate).run(delegateExecution);
    }

    @Test
    public void testExecute() {
        baseDeligate.execute(delegateExecution);
        verify(baseDeligate, times(1)).execute(delegateExecution);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseDeligate.isDone());
        baseDeligate.run(delegateExecution);
        assertTrue(baseDeligate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Object expectedResult = new Object();
        when(baseDeligate.get()).thenReturn(expectedResult);
        assertEquals(expectedResult, baseDeligate.get());
    }

    @Test
    public void testGetTimeOut() throws InterruptedException, ExecutionException, TimeoutException{
        try {
            baseDeligate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            fail("Timed out");
        }
    }
}