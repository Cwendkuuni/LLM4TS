package com.oceanbase.odc.service.flow.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.impl.test.AbstractFlowableTest;
import org.flowable.engine.task.api.Task;
import org.flowable.engine.task.api.TaskInfo;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.test.BaseRuntimeFlowableDelegateTest;

@Slf4j
public class FlowTaskInstanceTest extends AbstractFlowableTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final String PROCESS_ID = "processId";
    private static final String USER_TASK_ID = "userTaskId";

    private static final long FLOW_INSTANCE_ID = 1L;
    private static final long ORGANIZATION_ID = 1L;
    private static final long NODE_ID = 1L;
    private static final long TARGET_TASK_ID = 2L;
    private static final TaskType TASK_TYPE = TaskType.USER_TASK;

    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;
    private ServiceTaskInstanceEntity serviceTaskEntity;
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = new FlowableAdaptor(processEngineRule.getRuntimeManager(), nodeRepository,
                sequenceRepository, serviceTaskRepository);

        serviceTaskEntity = new ServiceTaskInstanceEntity();
        serviceTaskEntity.setOrganizationId(ORGANIZATION_ID);
        serviceTaskEntity.setStatus(FlowNodeStatus.PENDING);
        serviceTaskEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        serviceTaskEntity.setStartEndpoint(true);
        serviceTaskEntity.setEndEndpoint(false);
        serviceTaskEntity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        serviceTaskEntity.setTaskType(TASK_TYPE);
        serviceTaskEntity.setTargetTaskId(TARGET_TASK_ID);

        when(nodeRepository.findById(NODE_ID)).thenReturn(Optional.ofNullable(serviceTaskEntity));
        when(sequenceRepository.findByFlowInstanceId(FLOW_INSTANCE_ID)).thenReturn(Collections.emptyList());

        when(delegateConvertor.map(any(TaskType.class)))
                .thenReturn(BaseRuntimeFlowableDelegateTest.createTestDelegate(TASK_TYPE));
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        flowTaskInstance = new FlowTaskInstance(serviceTaskEntity, delegateConvertor, flowableAdaptor,
                eventPublisher, processEngineRule.getTaskService(), nodeRepository, sequenceRepository,
                serviceTaskRepository);
    }

    @Test
    @Deployment(resources = { "classpath:TestProcess.bpmn20.xml" })
    public void testCreateFlowTaskInstance() {
        flowTaskInstance.create();

        verify(nodeRepository).save(eq(serviceTaskEntity));
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertThat(flowTaskInstance.getCoreFlowableElementType(), is(FlowableElementType.SERVICE_TASK));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = List.of(flowTaskInstance, new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID,
                FLOW_INSTANCE_ID, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, processEngineRule.getTaskService(),
                nodeRepository, sequenceRepository, serviceTaskRepository));

        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertThat(entities.size(), is(instances.size()));
    }

    @Test
    public void testGetResult() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        when(taskService.getVariables(userTask.getId())).thenReturn(Map.of(ABORT_VARIABLE_NAME, false));

        Object result = flowTaskInstance.getResult();
        verify(targetTaskHandle).get();
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.MINUTES);
        verify(targetTaskHandle).get(1, TimeUnit.MINUTES);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        verify(targetTaskHandle, timeout(Duration.ofSeconds(2))).get(1, TimeUnit.SECONDS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.NANOSECONDS);
        verify(targetTaskHandle, timeout(Duration.ofNanos(2))).get(1, TimeUnit.NANOSECONDS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.MICROSECONDS);
        verify(targetTaskHandle, timeout(Duration.ofMillis(2))).get(1, TimeUnit.MILLISECONDS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.MILLISECONDS);
        verify(targetTaskHandle, timeout(Duration.ofMillis(2))).get(1, TimeUnit.MILLISECONDS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.HOURS);
        verify(targetTaskHandle, timeout(Duration.ofHours(2))).get(1, TimeUnit.HOURS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.DAYS);
        verify(targetTaskHandle, timeout(Duration.ofDays(2))).get(1, TimeUnit.DAYS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.WEEKS);
        verify(targetTaskHandle, timeout(Duration.ofWeeks(2))).get(1, TimeUnit.WEEKS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.MONTHS);
        verify(targetTaskHandle, timeout(Duration.ofMonths(2))).get(1, TimeUnit.MONTHS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(1, TimeUnit.YEARS);
        verify(targetTaskHandle, timeout(Duration.ofYears(2))).get(1, TimeUnit.YEARS);
        assertThat(result, is(notNullValue()));

        result = flowTaskInstance.getResult(Duration.ofMillis(500));
        verify(targetTaskHandle, timeout(Duration.ofMillis(2))).get(Duration.ofMillis(500));
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testAbort() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        when(taskService.getVariables(userTask.getId())).thenReturn(Map.of(ABORT_VARIABLE_NAME, true));

        flowTaskInstance.abort();

        verify(taskService).complete(userTask.getId(), Map.of(ABORT_VARIABLE_NAME, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortNotPresentOnThisMachine() {
        flowTaskInstance.abort();
        verify(targetTaskHandle, never()).cancel(anyBoolean());
    }

    @Test
    public void testCancel() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();

        flowTaskInstance.bindServiceTask(BaseRuntimeFlowableDelegateTest.createTestDelegate(TASK_TYPE));
        flowTaskInstance.cancel(true);

        verify(targetTaskHandle).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotPresentOnThisMachine() {
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle, never()).cancel(anyBoolean());
    }

    @Test
    public void testIsDone() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        when(taskService.getVariables(userTask.getId())).thenReturn(Map.of(ABORT_VARIABLE_NAME, false));

        assertTrue(flowTaskInstance.isDone());
        verify(targetTaskHandle).isDone();
    }

    @Test
    public void testIsDoneNotPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isDone());
        verify(targetTaskHandle, never()).isDone();
    }

    @Test
    public void testConfirmExecute() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        when(taskService.getVariables(userTask.getId())).thenReturn(Map.of(ABORT_VARIABLE_NAME, false));

        flowTaskInstance.confirmExecute();

        verify(taskService).complete(userTask.getId(), Map.of());
    }

    @Test
    public void testBindServiceTask() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        BaseRuntimeFlowableDelegate<?> target = BaseRuntimeFlowableDelegateTest.createTestDelegate(TASK_TYPE);

        flowTaskInstance.bindServiceTask(target);
        verify(target).bind(flowTaskInstance);
        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test(expected = AssertionError.class)
    public void testBindServiceTaskDuplicate() throws Exception {
        Task userTask = createTaskQuery().processInstanceId(PROCESS_ID).singleResult();
        BaseRuntimeFlowableDelegate<?> target = BaseRuntimeFlowableDelegateTest.createTestDelegate(TASK_TYPE);

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.bindServiceTask(target);
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();

        verify(eventPublisher).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        verify(eventPublisher).addEventListener(any(ServiceTaskStartedListener.class));
    }
}