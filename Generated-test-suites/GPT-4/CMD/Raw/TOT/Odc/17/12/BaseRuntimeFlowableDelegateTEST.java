import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate delegate;
    private DelegateExecution execution;
  
    @Before
    public void setup() {
        this.delegate = new BaseRuntimeFlowableDelegate();
        this.execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testInitCallable() {
        Callable<T> result = delegate.initCallable(execution);
        Mockito.verify(execution, Mockito.times(1)).getCurrentActivityId();
        assertNotNull(result);
    }
  
    @Test
    public void testExecute() {
        try {
            delegate.execute(execution);
            Mockito.verify(execution, Mockito.times(1)).signal();
        } catch (Exception e) {
            fail();
        }
    }
  
    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
    }
  
    @Test(expected = InterruptedException.class)
    public void testGetWithExecutionException() throws Exception {
        delegate.get();
    }
  
    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        delegate.get(10, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.bindToFlowTaskInstance(taskInstance);
        assertEquals(taskInstance, delegate.getTaskInstance());
    }
  
    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        assertEquals(FlowStatus.COMPLETED, delegate.getFlowStatus());
    }
}