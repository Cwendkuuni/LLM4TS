import org.junit.Test;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Before;

public class FlowTaskInstanceTest {
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private TaskType taskType;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;
    
    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(taskType, 1L, 1L, 
            new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, null, 0), true, false, runtimeDelegateMapper,
            flowableAdaptor, eventPublisher, taskService, new NodeInstanceEntityRepository(), new SequenceInstanceRepository(), new ServiceTaskInstanceRepository());
    }

    @Test
    public void testGetter() {
        assertEquals(taskType, flowTaskInstance.getTaskType());
    }

    @Test
    public void testCreateMethod() {
        flowTaskInstance.create();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).create();
    }

    @Test
    public void testUpdateMethod() {
        flowTaskInstance.update();
        Mockito.verify(flowTaskInstance, Mockito.times(1)).update();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() throws InterruptedException, ExecutionException {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }
    
    @Test
    public void testBindServiceTaskMethod() {
        BaseRuntimeFlowableDelegate task = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        Mockito.when(task.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        
        flowTaskInstance.bindServiceTask(task);
        
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCancelMethod() {
        flowTaskInstance.cancel(true);
    }
    
    @Test
    public void testConfirmExecuteMethod() {
        throw  new UnsupportedOperationException("Automatic tasks do not support this execution");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAbortMethod() {
        flowTaskInstance.abort();
    }
}