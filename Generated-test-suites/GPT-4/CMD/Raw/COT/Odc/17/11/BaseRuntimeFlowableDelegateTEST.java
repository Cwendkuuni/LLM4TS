
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import java.util.concurrent.TimeUnit;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testInit() {
        Mockito.when(baseRuntimeFlowableDelegate.init(Matchers.any())).thenCallRealMethod();
        baseRuntimeFlowableDelegate.init(null);

        Mockito.verify(baseRuntimeFlowableDelegate).init(null);
    }

    @Test
    public void testRun() {
        Mockito.when(baseRuntimeFlowableDelegate.run(Matchers.any())).thenCallRealMethod();
        baseRuntimeFlowableDelegate.run(null);

        Mockito.verify(baseRuntimeFlowableDelegate).run(null);
    }

    @Test
    public void testExecute() {
        Mockito.when(baseRuntimeFlowableDelegate.execute(Matchers.any())).thenCallRealMethod();
        baseRuntimeFlowableDelegate.execute(null);

        Mockito.verify(baseRuntimeFlowableDelegate).execute(null);
    }

    @Test
    public void testIsDone() {
        Mockito.when(baseRuntimeFlowableDelegate.isDone()).thenCallRealMethod().thenReturn(true);
        Assert.assertEquals(baseRuntimeFlowableDelegate.isDone(), true);
    }

    @Test
    public void testGet() throws Exception {
        Mockito.when(baseRuntimeFlowableDelegate.get()).thenCallRealMethod();
        baseRuntimeFlowableDelegate.get();
        
        Mockito.verify(baseRuntimeFlowableDelegate).get();
    }

    @Test
    public void testGetWithParameter() throws Exception {
        Mockito.when(baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS)).thenCallRealMethod();
        thrown.expect(TimeoutException.class);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);

        Mockito.verify(baseRuntimeFlowableDelegate).get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        Mockito.when(baseRuntimeFlowableDelegate.bindToFlowTaskInstance(Matchers.any())).thenCallRealMethod();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(null);

        Mockito.verify(baseRuntimeFlowableDelegate).bindToFlowTaskInstance(null);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Mockito.when(baseRuntimeFlowableDelegate.updateFlowInstanceStatus(Matchers.any())).thenCallRealMethod();
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);

        Mockito.verify(baseRuntimeFlowableDelegate).updateFlowInstanceStatus(null);
    }  
}
