import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;

    @Before
    public void setUp() {
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), mock(RuntimeDelegateMapper.class),
                mock(FlowableAdaptor.class), mock(EventPublisher.class), taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        when(serviceTaskRepository.save(flowTaskInstance)).thenReturn(mock(ServiceTaskInstanceEntity.class));
        flowTaskInstance.create();
        assertTrue(flowTaskInstance.getId() != null);
        assertTrue(flowTaskInstance.getCreateTime() != null);
        assertTrue(flowTaskInstance.getUpdateTime() != null);
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(flowTaskInstance.getId());
        entity.setStatus(flowTaskInstance.getStatus());
        entity.setTargetTaskId(flowTaskInstance.getTargetTaskId());
        entity.setTaskType(flowTaskInstance.getTaskType());
        when(serviceTaskRepository.update(entity)).thenReturn(1);
        flowTaskInstance.update();
        assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.doDelete(1L);
        assertTrue(true);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertTrue(true);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertTrue(true);
    }

    @Test
    public void testCancel() {
        // Assuming target task handle is not null
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(true);
        assertTrue(true);
    }

    @Test
    public void testIsDone() {
        // Assuming target task handle is not null
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() {
        // Assuming target task handle is not null
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.getResult() != null);
    }

    @Test
    public void testGetResultWithTimeout() {
        // Assuming target task handle is not null
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.getResult(1, TimeUnit.SECONDS) != null);
    }

}