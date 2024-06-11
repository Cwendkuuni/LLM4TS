import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockModule;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig.Builder;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.test.BaseTest;
import com.oceanbase.odc.test.TestConstant;
import com.oceanbase.odc.test.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FlowTaskInstanceTest extends BaseTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private RuntimeDelegateMapper runtimeDelegateMapper;
    private ServiceTaskStartedListener serviceTaskStartedListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        taskService = Mockito.mock(TaskService.class);
        runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);
        serviceTaskStartedListener = Mockito.mock(ServiceTaskStartedListener.class);

        PowerMockito.spy(flowTaskInstance);
        PowerMockito.whenNew(ServiceTaskInstanceRepository.class).withAnyArguments().thenReturn(serviceTaskInstanceRepository);
        PowerMockito.whenNew(NodeInstanceEntityRepository.class).withAnyArguments().thenReturn(nodeInstanceRepository);
        PowerMockito.whenNew(SequenceInstanceRepository.class).withAnyArguments().thenReturn(sequenceInstanceRepository);
        PowerMockito.whenNew(FlowableAdaptor.class).withAnyArguments().thenReturn(flowableAdaptor);
        PowerMockito.whenNew(EventPublisher.class).withAnyArguments().thenReturn(eventPublisher);
        PowerMockito.whenNew(TaskService.class).withAnyArguments().thenReturn(taskService);
        PowerMockito.whenNew(RuntimeDelegateMapper.class).withAnyArguments().thenReturn(runtimeDelegateMapper);
        PowerMockito.when(flowableAdaptor.getFlowableElementByType(Mockito.anyLong(), Mockito.eq(FlowNodeType.USER_TASK),
                Mockito.eq(FlowableElementType.USER_TASK))).thenReturn(TestUtils.createEmptyList());

        flowTaskInstance.setNodeInstanceRepository(nodeInstanceRepository);
        flowTaskInstance.setSequenceInstanceRepository(sequenceInstanceRepository);
        flowTaskInstance.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowTaskInstance.setFlowableAdaptor(flowableAdaptor);
        flowTaskInstance.setEventPublisher(eventPublisher);
        flowTaskInstance.setTaskService(taskService);
        flowTaskInstance.setRuntimeDelegateMapper(runtimeDelegateMapper);

        Builder strategyBuilder = new Builder();
        ExecutionStrategyConfig strategy = strategyBuilder.strategy(FlowTaskExecutionStrategy.MANUAL)
                .pendingExpireIntervalSeconds(1).executionTime(10).build();
        flowTaskInstance.setStrategyConfig(strategy);
        flowTaskInstance.setTaskType(TestConstant.TEST_TASK_TYPE);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();
        verify(eventPublisher).publishEvent(Mockito.any(TaskInstanceCreatedEvent.class));
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(flowTaskInstance.getId(), target.getTargetTaskInstanceId());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        String taskId = RandomStringUtils.randomAlphanumeric(64);
        Task task = Mockito.mock(Task.class);
        when(taskService.createTaskQuery().processInstanceId(Mockito.anyString()).list().get(0)).thenReturn(task);

        flowTaskInstance.abort();
        flowTaskInstance.cancel(true);
        verify(taskService).cancel(task.getId(), true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithoutTask() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        String processInstanceId = RandomStringUtils.randomAlphanumeric(64);
        List<FlowableElement> elements = TestUtils.createEmptyList();
        when(flowableAdaptor.getFlowableElementByType(Mockito.anyLong(), Mockito.eq(FlowNodeType.USER_TASK),
                Mockito.eq(FlowableElementType.USER_TASK))).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(Mockito.anyLong())).thenReturn(Optional.of(processInstanceId));

        List<Task> tasks = TestUtils.createTasks(2);
        when(taskService.createTaskQuery().processInstanceId(processInstanceId).list()).thenReturn(tasks);

        flowTaskInstance.complete(false);
        verify(taskService).complete(tasks.get(0).getId(), Mockito.anyMap());
        verify(taskService).complete(tasks.get(1).getId(), Mockito.anyMap());
    }

    @Test
    public void testCompleteWithAborted() throws InterruptedException, ExecutionException {
        String processInstanceId = RandomStringUtils.randomAlphanumeric(64);
        List<FlowableElement> elements = TestUtils.createEmptyList();
        when(flowableAdaptor.getFlowableElementByType(Mockito.anyLong(), Mockito.eq(FlowNodeType.USER_TASK),
                Mockito.eq(FlowableElementType.USER_TASK))).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(Mockito.anyLong())).thenReturn(Optional.of(processInstanceId));

        List<Task> tasks = TestUtils.createTasks(2);
        when(taskService.createTaskQuery().processInstanceId(processInstanceId).list()).thenReturn(tasks);

        Map<String, Object> variables = new HashMap<>();
        variables.put(FlowTaskInstance.ABORT_VARIABLE_NAME, true);

        flowTaskInstance.complete(true);
        verify(taskService).complete(tasks.get(0).getId(), Mockito.anyMap());
        verify(taskService).complete(tasks.get(1).getId(), Mockito.anyMap());
        assertTrue(variables.get(FlowTaskInstance.ABORT_VARIABLE_NAME).equals(true));
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("test result");
        when(runtimeDelegateMapper.map(Mockito.eq(FlowTaskInstance.class), Mockito.eq(TaskType.TEST_TASK_TYPE)))
                .thenReturn(target);

        flowTaskInstance.bindServiceTask(target);
        String result = flowTaskInstance.getResult();
        assertEquals("test result", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutTask() {
        flowTaskInstance.getResult();
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testIsDoneWithoutTask() {
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> clazz = flowTaskInstance.getTargetDelegateClass();
        assertEquals(BaseRuntimeFlowableDelegate.class, clazz);
    }

    @Test
    public void testGetTaskType() {
        assertEquals(TestConstant.TEST_TASK_TYPE, flowTaskInstance.getTaskType());
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = TestUtils.createInstances(2);
        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertEquals(2, entities.size());
        assertEquals(instances.get(0).getId(), entities.get(0).getId());
        assertEquals(instances.get(1).getId(), entities.get(1).getId());
    }

    @Test
    public void testUpdate() {
        String status = "COMPLETED";
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.valueOf(status), flowTaskInstance.getStatus());
    }
}