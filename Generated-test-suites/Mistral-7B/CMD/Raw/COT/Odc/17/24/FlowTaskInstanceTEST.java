import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.taskService = Mockito.mock(TaskService.class);
        this.nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        this.sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);

        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        RuntimeDelegateMapper runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);

        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), runtimeDelegateMapper,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        FlowableElementType expectedFlowableElementType = FlowableElementType.SERVICE_TASK;
        FlowNodeType expectedFlowNodeType = FlowNodeType.SERVICE_TASK;
        Long expectedOrganizationId = 1L;
        Long expectedFlowInstanceId = 1L;
        FlowNodeStatus expectedStatus = FlowNodeStatus.PENDING;
        boolean expectedStartEndpoint = true;
        boolean expectedEndEndpoint = false;
        ExecutionStrategyConfig expectedStrategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
        TaskType expectedTaskType = TaskType.TASK_TYPE_1;

        ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);
        Mockito.when(entity.getId()).thenReturn(1L);
        Mockito.when(entity.getOrganizationId()).thenReturn(expectedOrganizationId);
        Mockito.when(entity.getStatus()).thenReturn(expectedStatus);
        Mockito.when(entity.getFlowInstanceId()).thenReturn(expectedFlowInstanceId);
        Mockito.when(entity.isStartEndpoint()).thenReturn(expectedStartEndpoint);
        Mockito.when(entity.isEndEndpoint()).thenReturn(expectedEndEndpoint);
        Mockito.when(entity.getStrategy()).thenReturn(expectedStrategyConfig.getStrategy());
        Mockito.when(entity.getTaskType()).thenReturn(expectedTaskType);

        Mockito.when(serviceTaskRepository.save(ArgumentMatchers.any(ServiceTaskInstanceEntity.class)))
                .thenReturn(entity);

        flowTaskInstance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(expectedFlowableElementType, flowTaskInstance.getCoreFlowableElementType());
        assertEquals(expectedFlowNodeType, flowTaskInstance.getNodeType());
        assertEquals(expectedOrganizationId, flowTaskInstance.getOrganizationId());
        assertEquals(expectedFlowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(expectedStatus, flowTaskInstance.getStatus());
        assertEquals(expectedStartEndpoint, flowTaskInstance.isStartEndpoint());
        assertEquals(expectedEndEndpoint, flowTaskInstance.isEndEndPoint());
        assertEquals(expectedStrategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(expectedTaskType, flowTaskInstance.getTaskType());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testSetId() {
        long id = 1L;
        flowTaskInstance.setId(id);
        assertEquals(id, flowTaskInstance.getId());
    }

    @Test
    public void testGetId() {
        Long id = 1L;
        flowTaskInstance.setId(id);
        assertEquals(id, flowTaskInstance.getId());
    }

    @Test
    public void testGetFlowInstanceId() {
        Long flowInstanceId = 1L;
        flowTaskInstance.setFlowInstanceId(flowInstanceId);
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testGetStatus() {
        FlowNodeStatus status = FlowNodeStatus.EXECUTING;
        flowTaskInstance.setStatus(status);
        assertEquals(status, flowTaskInstance.getStatus());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);
        assertSame(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskWithDifferentId() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsNotPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        assertSame(flowTaskInstance.getListener(), flowTaskInstance.getListener());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<FlowTaskInstance> expectedInstances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = Mockito.mock(List.class);

        Mockito.when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(expectedInstances, result);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);

        Mockito.when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        Verify.notNull(flowTaskInstance.getId(), "id");
        Verify.notNull(flowTaskInstance.getCreateTime(), "CreateTime");
        Verify.notNull(flowTaskInstance.getUpdateTime(), "UpdateTime");
    }

    @Test
    public void testUpdate() {
        long instanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.EXECUTING;

        Mockito.when(serviceTaskRepository.update(ArgumentMatchers.any(ServiceTaskInstanceEntity.class)))
                .thenReturn(1);

        flowTaskInstance.setId(instanceId);
        flowTaskInstance.setStatus(status);
        flowTaskInstance.update();

        assertEquals(status, flowTaskInstance.getStatus());
    }

    @Test
    public void testDoDelete() {
        long instanceId = 1L;

        Mockito.doNothing().when(serviceTaskRepository).deleteById(instanceId);

        flowTaskInstance.setId(instanceId);
        flowTaskInstance.doDelete(instanceId);

        assertEquals(instanceId, flowTaskInstance.getId());
        Mockito.verify(serviceTaskRepository).deleteById(instanceId);
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        List<FlowableElement> elements = Mockito.mock(List.class);
        String pid = "pid";
        TaskService taskService = Mockito.mock(TaskService.class);

        Mockito.when(flowableAdaptor.getFlowableElementByType(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(FlowableElementType.USER_TASK),
                ArgumentMatchers.isA(FlowableElement.class))).thenReturn(elements);
        Mockito.when(taskService.createTaskQuery().processInstanceId(pid).list()).thenReturn(Collections.emptyList());

        flowTaskInstance.complete(false);

        Mockito.verify(flowableAdaptor).getFlowableElementByType(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(FlowableElementType.USER_TASK),
                ArgumentMatchers.isA(FlowableElement.class));
        Mockito.verify(taskService).createTaskQuery().processInstanceId(pid).list();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() throws InterruptedException, ExecutionException {
        flowTaskInstance.abort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        Mockito.when(target.isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        Object result = Mockito.mock(Object.class);
        Mockito.when(target.get()).thenReturn(result);

        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        Object result = Mockito.mock(Object.class);
        Mockito.when(target.get(1, TimeUnit.SECONDS)).thenReturn(result);

        assertEquals(result, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

}