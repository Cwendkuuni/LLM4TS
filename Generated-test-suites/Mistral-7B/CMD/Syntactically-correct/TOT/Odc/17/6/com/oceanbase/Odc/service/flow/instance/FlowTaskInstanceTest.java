package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.collection.ListUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
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
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@SuppressWarnings("unchecked")
public class FlowTaskInstanceTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();

    @Mock
    private RuntimeService runtimeService;

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
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Context.setProcessEngineConfiguration(flowableTestRule.getProcessEngineConfiguration());
        when(runtimeService.getTaskService()).thenReturn(taskService);
        when(taskService.createExecutionListenerBuilder()).thenReturn(mock(TaskService.ExecutionListenerBuilder.class));
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(nodeRepository.getById(anyLong(), anyLong())).thenReturn(null);
        when(sequenceRepository.getById(anyLong(), anyLong())).thenReturn(null);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> 1);
        when(serviceTaskRepository.deleteById(anyLong())).thenAnswer(i -> {});
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK))).thenReturn(ListUtils.emptyList());
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("PI_ID"));

        flowTaskInstance.setRuntimeService(runtimeService);
        flowTaskInstance.setTaskService(taskService);
        flowTaskInstance.setEventPublisher(eventPublisher);
        flowTaskInstance.setNodeRepository(nodeRepository);
        flowTaskInstance.setSequenceRepository(sequenceRepository);
        flowTaskInstance.setServiceTaskRepository(serviceTaskRepository);
        flowTaskInstance.setFlowableAdaptor(flowableAdaptor);
        flowTaskInstance.setListener(listener);
    }

    @After
    public void tearDown() {
        reset(runtimeService, taskService, eventPublisher, nodeRepository, sequenceRepository,
                serviceTaskRepository, flowableAdaptor, listener);
    }

    @Test
    @Deployment("test.bpmn")
    public void testCreate() {
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowNodeStatus status = FlowNodeStatus.EXECUTING;
        Long flowInstanceId = 123L;
        Long organizationId = 1L;

        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        strategyConfig.setPendingExpireIntervalSeconds(10);
        strategyConfig.setExecutionTime("PT1S");

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        // Given
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowNodeStatus status = FlowNodeStatus.EXECUTING;
        Long organizationId = 1L;
        Long flowInstanceId = 123L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        strategyConfig.setPendingExpireIntervalSeconds(10);
        strategyConfig.setExecutionTime("PT1S");
        List<FlowTaskInstance> instances = List.of(
                new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, false, false,
                        delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                        sequenceRepository, serviceTaskRepository),
                new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, false, false,
                        delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                        sequenceRepository, serviceTaskRepository));

        // When
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        // Then
        assertEquals(instances.size(), result.size());
        for (int i = 0; i < instances.size(); i++) {
            assertEquals(instances.get(i).getId(), result.get(i).getId());
            assertEquals(instances.get(i).getCreateTime(), result.get(i).getCreateTime());
            assertEquals(instances.get(i).getUpdateTime(), result.get(i).getUpdateTime());
        }
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();
        assertTrue(result.isInstance(new SimpleBaseRuntimeFlowableDelegate()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskNull() {
        flowTaskInstance.bindServiceTask(null);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachineFalse() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).complete(anyLong(), MapUtils.map("aborted", true));
    }

    @Test
    public void testCancel() {
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        assertTrue(flowTaskInstance.isDone());
        when(targetTaskHandle.isDone()).thenReturn(false);
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        Object result = flowTaskInstance.getResult();
        verify(targetTaskHandle, times(1)).get();
        assertEquals(result, targetTaskHandle.get());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        verify(targetTaskHandle, times(1)).get(1, TimeUnit.SECONDS);
        assertEquals(result, targetTaskHandle.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutThrowException() throws InterruptedException, ExecutionException,
            TimeoutException {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        verify(targetTaskHandle, times(1)).get(1, TimeUnit.SECONDS);
        throw new AssertionError("Test should have thrown TimeoutException");
    }

    @Test
    public void testGetResultInterruptedException() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
        verify(targetTaskHandle, times(1)).get();
        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException,
            TimeoutException {
        when(targetTaskHandle.get()).thenThrow(new ExecutionException("Test"));
        flowTaskInstance.getResult();
        verify(targetTaskHandle, times(1)).get();
        throw new AssertionError("Test should have thrown ExecutionException");
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterruptedException() throws InterruptedException, ExecutionException,
            TimeoutException {
        when(targetTaskHandle.get()).thenThrow(new InterruptedException("Test"));
        flowTaskInstance.getResult();
        verify(targetTaskHandle, times(1)).get();
        throw new AssertionError("Test should have thrown InterruptedException");
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }
}