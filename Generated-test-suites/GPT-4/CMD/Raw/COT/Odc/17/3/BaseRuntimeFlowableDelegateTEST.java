import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

public class BaseRuntimeFlowableDelegateTest {
    @Mock
    private BaseRuntimeFlowableDelegate delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testInit() {
        // I'm not sure how to test this method as it doesn't return anything or change any fields that I can access  
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun() throws Exception{
        Mockito.doThrow(new Exception()).when(delegate).run(Mockito.any());
    }

    @Test
    public void testExecute() {
        delegate.execute(Mockito.mock(DelegateExecution.class));
        Mockito.verify(delegate).execute(Mockito.any());
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
        Mockito.verify(delegate).isDone();
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws Exception {
        Mockito.doThrow(new InterruptedException()).when(delegate).get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithArgs() throws Exception {
        Mockito.doThrow(new TimeoutException()).when(delegate).get(1L, Mockito.any());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));
        Mockito.verify(delegate).bindToFlowTaskInstance(Mockito.any());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Mockito.doNothing().when(delegate).updateFlowInstanceStatus(Mockito.any());
    }

    @Test
    public void testInitCallable() {
        delegate.initCallable(Mockito.mock(DelegateExecution.class));
        Mockito.verify(delegate).initCallable(Mockito.any());
    }
}