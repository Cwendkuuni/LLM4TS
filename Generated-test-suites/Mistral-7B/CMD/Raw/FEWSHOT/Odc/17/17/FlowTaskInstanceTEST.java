package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnit;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance instance;

    @Mock
    private ServiceTaskInstanceEntity entity;

    private BaseRuntimeFlowableDelegate<?> targetTaskHandleMock;

    @BeforeEach
    public void setup() {
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);
        when(delegateConvertor.map(any(TaskType.class))).thenReturn(targetTaskHandleMock);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenAnswer(invocation -> 1);
        when(serviceTaskRepository.deleteById(anyLong())).thenAnswer(invocation -> {});
        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.complete(anyLong(), anyMap())).thenAnswer(invocation -> {});
        when(taskService.list(any())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCreate() {
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 60000);
        TaskType taskType = TaskType.TASK1;
        Long targetTaskId = 2L;
        boolean startEndpoint = true;
        boolean endEndPoint = false;

        instance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint, endEndPoint,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);

        assertEquals("instance not initialized correctly", FlowNodeType.SERVICE_TASK, instance.getNodeType());
        assertEquals("id not set correctly", 0, instance.getId());
        assertEquals("organizationId not set correctly", organizationId, instance.getOrganizationId());
        assertEquals("status not set correctly", status, instance.getStatus());
        assertEquals("flowInstanceId not set correctly", flowInstanceId, instance.getFlowInstanceId());
        assertEquals("createTime not set correctly", 0, instance.getCreateTime());
        assertEquals("updateTime not set correctly", 0, instance.getUpdateTime());
        assertEquals("startEndpoint not set correctly", startEndpoint, instance.isStartEndpoint());
        assertEquals("endEndPoint not set correctly", endEndPoint, instance.isEndEndPoint());
        assertEquals("strategyConfig not set correctly", strategyConfig, instance.getStrategyConfig());
        assertEquals("taskType not set correctly", taskType, instance.getTaskType());
        assertEquals("targetTaskId not set correctly", targetTaskId, instance.getTargetTaskId());

        instance.create();

        assertEquals("id not set after create", instance.getId(), entity.getId());
        assertEquals("createTime not set after create", instance.getCreateTime(), entity.getCreateTime());
        assertEquals("updateTime not set after create", instance.getUpdateTime(), entity.getUpdateTime());
        assertEquals("status not updated after create", FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testUpdate() {
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 60000);
        TaskType taskType = TaskType.TASK1;
        Long targetTaskId = 2L;
        boolean startEndpoint = true;
        boolean endEndPoint = false;

        when(entity.getId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(status);
        when(entity.getTargetTaskId()).thenReturn(targetTaskId);
        when(entity.getTaskType()).thenReturn(taskType);
        when(entity.getStrategy()).thenReturn(strategyConfig.getStrategy());

        instance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        instance.update();

        verify(serviceTaskRepository).update(entity);
        assertEquals("status not updated after update", FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.complete(anyLong(), anyMap())).thenAnswer(invocation -> {});

        instance.bindServiceTask(target);

        assertEquals("targetTaskHandle not set correctly after bind", target, instance.getTargetTaskHandle());
        assertEquals("status not updated after bind", FlowNodeStatus.EXECUTING, instance.getStatus());

        verify(taskService).createTaskQuery();
        verify(taskService, times(2)).complete(anyLong(), anyMap());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listenerMock = mock(ServiceTaskStartedListener.class);

        instance.alloc();
        instance.dealloc();

        verify(eventPublisher).removeEventListener(listenerMock);
        assertNull("listener should be null after dealloc", instance.getListener());
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listenerMock = mock(ServiceTaskStartedListener.class);

        instance.dealloc();
        instance.alloc();

        verify(eventPublisher).addEventListener(listenerMock);
        assertNotNull("listener should not be null after alloc", instance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        String processInstanceId = "pid";

        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(processInstanceId));
        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.list(any())).thenReturn(Collections.singletonList(mock(Task.class)));

        instance.confirmExecute();

        verify(flowableAdaptor).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(anyLong());
        verify(taskService).createTaskQuery();
        verify(taskService).list(any());
    }

    @Test
    public void testAbort() {
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        String processInstanceId = "pid";

        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(processInstanceId));
        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.list(any())).thenReturn(Collections.singletonList(mock(Task.class)));

        instance.abort();

        verify(flowableAdaptor).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(anyLong());
        verify(taskService).createTaskQuery();
        verify(taskService).list(any());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        instance.bindServiceTask(target);
        instance.cancel(true);

        verify(target).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);

        instance.bindServiceTask(target);

        assertTrue("isDone should return true when target is done", instance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Object result = "result";
        when(target.isDone()).thenReturn(true);
        when(target.get()).thenReturn(result);

        instance.bindServiceTask(target);

        assertEquals("getResult should return result when target is done", result, instance.getResult());
    }

    @Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Object result = "result";
        when(target.isDone()).thenReturn(false);
        when(target.get(anyLong(), any(TimeUnit.class))).thenReturn(result);

        instance.bindServiceTask(target);

        assertEquals("getResult should return result when target is done with timeout", result, instance.getResult(1000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWhenNotPresent() {
        instance.getResult();
    }

    @Test
    public void testGetTargetDelegateClass() {
        TaskType taskType = TaskType.TASK1;
        Class<? extends BaseRuntimeFlowableDelegate<?>> expected = BaseRuntimeFlowableDelegate.class;

        assertEquals("getTargetDelegateClass should return correct class for given taskType", expected, instance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        TaskType taskType = TaskType.TASK1;
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        String processInstanceId = "pid";
        List<Task> tasks = Collections.singletonList(mock(Task.class));

        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(processInstanceId));
        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.list(any())).thenReturn(tasks);

        instance.complete(true);

        verify(flowableAdaptor).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(anyLong());
        verify(taskService).createTaskQuery();
        verify(taskService).list(any());
        verify(tasks, times(elements.size())).complete(anyMap());
    }

    @Test
    public void testMapToTaskEntity() {
        Long organizationId = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        Long flowInstanceId = 1L;
        boolean startEndpoint = true;
        boolean endEndPoint = false;
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
        TaskType taskType = TaskType.TASK1;
        Long targetTaskId = 2L;
        long executionTime = 1000;
        long pendingExpireIntervalSeconds = 60000;

        ServiceTaskInstanceEntity entity = instance.mapToTaskEntity(instance);

        assertEquals("organizationId not set correctly", organizationId, entity.getOrganizationId());
        assertEquals("status not set correctly", status, entity.getStatus());
        assertEquals("flowInstanceId not set correctly", flowInstanceId, entity.getFlowInstanceId());
        assertEquals("startEndpoint not set correctly", startEndpoint, entity.isStartEndpoint());
        assertEquals("endEndPoint not set correctly", endEndPoint, entity.isEndEndPoint());
        assertEquals("strategy not set correctly", strategy, entity.getStrategy());
        assertEquals("taskType not set correctly", taskType, entity.getTaskType());
        assertEquals("targetTaskId not set correctly", targetTaskId, entity.getTargetTaskId());
        assertEquals("executionTime not set correctly", executionTime, entity.getExecutionTime());
        assertEquals("pendingExpireIntervalSeconds not set correctly", pendingExpireIntervalSeconds, entity.getWaitExecExpireIntervalSeconds());
    }
}