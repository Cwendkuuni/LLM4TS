
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstanceBuilder()
            .setTargetTaskId(1L)
            .setTaskType(TaskType.TYPE)
            .setOrganizationId(1L)
            .setFlowInstanceId(1L)
            .setExecutionStrategyConfig(new ExecutionStrategyConfig())
            .setStartEndpoint(true)
            .setEndEndPoint(false)
            .setDelegateConvertor(new RuntimeDelegateMapper())
            .setFlowableAdaptor(new FlowableAdaptor())
            .setEventPublisher(new EventPublisher())
            .setTaskService(new TaskService())
            .setNodeRepository(new NodeInstanceEntityRepository())
            .setSequenceRepository(new SequenceInstanceRepository())
            .setServiceTaskRepository(serviceTaskInstanceRepository)
            .build();
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate());
        flowTaskInstance.cancel(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate());
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate());
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate());
        assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

}
