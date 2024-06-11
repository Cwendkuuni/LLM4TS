package com.oceanbase.odc.service.flow.instance;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @date 2023-03-22 10:58
 */
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

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @BeforeAll
    public static void initMocks() {
        MockitoAnnotations.initMocks(FlowTaskInstanceTest.class);
    }

    @Test
    public void testCreateWithId() {
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, organizationId, flowInstanceId,
                strategyConfig, true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        assertNotNull(flowTaskInstance);
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig.getStrategy(), flowTaskInstance.getStrategy());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testCreateWithoutId() {
        TaskType taskType = TaskType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 2L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        assertNotNull(flowTaskInstance);
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig.getStrategy(), flowTaskInstance.getStrategy());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(flowTaskInstance.getId(), flowTaskInstance.getId());
        assertEquals(flowTaskInstance.getCreateTime(), flowTaskInstance.getCreateTime());
        assertEquals(flowTaskInstance.getUpdateTime(), flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        List<ServiceTaskInstanceEntity> entities = mock(List.class);
        when(serviceTaskInstanceRepository.batchCreate(entities)).thenReturn(entities);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK), any())).thenReturn(
                Collections.emptyList());
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertEquals(instances, result);
    }

    @Test
    public void testCreate() {
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        verify(serviceTaskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
        verify(target, times(1)).setTargetTaskInstanceId(eq(flowTaskInstance.getId()));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);
        when(target.get()).thenThrow(new RuntimeException(""));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(taskService, times(1)).cancel(eq(target.getId()), eq(true));
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");
        flowTaskInstance.bindServiceTask(target);
        String result = flowTaskInstance.getResult();
        assertEquals("result", result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenReturn("result");
        flowTaskInstance.bindServiceTask(target);
        String result = flowTaskInstance.getResult(100L, TimeUnit.MILLISECONDS);
        assertEquals("result", result);
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
        assertEquals(RuntimeDelegateMapper.class, targetDelegateClass.getSuperclass());
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
        assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndPoint(), entity.isEndEndpoint());
        assertEquals(flowTaskInstance.strategyConfig.getStrategy(), entity.getStrategy());
        assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
        assertEquals(flowTaskInstance.getTargetTaskId(), entity.getTargetTaskId());
        assertEquals(flowTaskInstance.getWaitExecExpireIntervalSeconds(), entity.getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.getExecutionTime(), entity.getExecutionTime());
    }

}