package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

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
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for FlowTaskInstance
 *
 * @author yh263208
 */
public class FlowTaskInstanceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

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

    private Long organizationId = 1L;
    private Long flowInstanceId = 1L;
    private Long taskInstanceId = 1L;
    private Long targetTaskId = 2L;
    private ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L);
    private TaskType taskType = TaskType.USER_TASK;
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(new Object());

        when(taskService.createTaskQuery().list())
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(mock(Task.class)));

        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());

        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
    }

    @After
    public void tearDown() {
        reset(eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository,
                flowableAdaptor, delegateConvertor, listener);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateAlreadyExists() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);
        verify(serviceTaskRepository, times(1)).save(anyCollectionOf(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        flowTaskInstance.doDelete(taskInstanceId);
        verify(serviceTaskRepository, times(1)).deleteById(taskInstanceId);
    }

    @Test
    public void testBindServiceTask() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        verify(flowableAdaptor).getFlowableElementByType(taskInstanceId, FlowNodeType.SERVICE_TASK,
                FlowableElementType.USER_TASK);
        verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(listener, times(1)).onEvent(any(TaskInstanceCreatedEvent.class));
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskAlreadyBound() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachineFalse() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.empty());
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.dealloc();
        verifyZeroInteractions(listener);
    }

    @Test
    public void testAlloc() {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.empty());
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.alloc();
        verify(listener, times(1)).onEvent(any(TaskInstanceCreatedEvent.class));
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verifyZeroInteractions(taskService);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verifyZeroInteractions(taskService);
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        when(targetTaskHandle.isDone()).thenReturn(false);
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelAlreadyCanceled() throws InterruptedException, ExecutionException, TimeoutException {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testResult() throws InterruptedException, ExecutionException {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        when(targetTaskHandle.get()).thenReturn("test result");
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals("test result", flowTaskInstance.getResult());
    }

    @Test
    public void testResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(serviceTaskRepository.findById(taskInstanceId)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> unitArgumentCaptor = ArgumentCaptor.forClass(TimeUnit.class);
        when(targetTaskHandle.get(timeoutArgumentCaptor.capture(timeoutArgumentCaptor), unitArgumentCaptor.capture(unitArgumentCaptor)))
                .thenThrow(new TimeoutException("timeout"));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertFalse(flowTaskInstance.isDone());
        fail("Expected TimeoutException");
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

}