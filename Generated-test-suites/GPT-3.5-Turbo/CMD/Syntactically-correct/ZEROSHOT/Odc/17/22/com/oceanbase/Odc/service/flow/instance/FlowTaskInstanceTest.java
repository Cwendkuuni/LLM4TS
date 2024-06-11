import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private ServiceTaskStartedListener listener;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;

    @Before
    public void setUp() {
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);

        flowTaskInstance = new FlowTaskInstance(
                TaskType.TYPE1, 123L, 456L, new ExecutionStrategyConfig(), true, false,
                delegateConvertor, new FlowableAdaptor(), eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskRepository);

        listener = new ServiceTaskStartedListener(flowTaskInstance);
        flowTaskInstance.setListener(listener);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);

        List<ServiceTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new ServiceTaskInstanceEntity());

        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(entities);

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(instances.size(), createdInstances.size());
        assertEquals(entities.get(0).getId(), createdInstances.get(0).getId());
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.save(any())).thenReturn(entity);

        flowTaskInstance.create();

        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskRepository.update(any())).thenReturn(1);

        flowTaskInstance.update();

        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete();
        verify(serviceTaskRepository, times(1)).deleteById(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get(anyLong(), any())).thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(delegateConvertor.map(any())).thenReturn(BaseRuntimeFlowableDelegate.class);

        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }
}