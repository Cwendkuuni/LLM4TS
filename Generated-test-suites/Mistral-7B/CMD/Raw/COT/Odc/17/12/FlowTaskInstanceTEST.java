package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
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

/**
 * FlowTaskInstance Test
 *
 * @author yh263208
 * @date 2023-03-28 12:17
 */
public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskServiceMock;
    private EventPublisher eventPublisherMock;
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;
    private SequenceInstanceRepository sequenceInstanceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    private FlowableAdaptor flowableAdaptorMock;
    private RuntimeDelegateMapper runtimeDelegateMapperMock;
    private ServiceTaskStartedListener serviceTaskStartedListenerMock;

    @Before
    public void setUp() {
        taskServiceMock = Mockito.mock(TaskService.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        nodeInstanceEntityRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
        runtimeDelegateMapperMock = Mockito.mock(RuntimeDelegateMapper.class);
        serviceTaskStartedListenerMock = Mockito.mock(ServiceTaskStartedListener.class);

        flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(
                FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapperMock, flowableAdaptorMock,
                eventPublisherMock, taskServiceMock, nodeInstanceEntityRepositoryMock, sequenceInstanceRepositoryMock,
                serviceTaskInstanceRepositoryMock);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        FlowableElementType expected = FlowableElementType.SERVICE_TASK;
        FlowableElementType actual = flowTaskInstance.getCoreFlowableElementType();
        assertEquals(expected, actual);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        List<ServiceTaskInstanceEntity> entities = mock(List.class);

        when(serviceTaskInstanceRepositoryMock.batchCreate(entities)).thenReturn(entities);

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        verify(serviceTaskInstanceRepositoryMock).batchCreate(entities);
        assertEquals(instances, result);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);

        when(serviceTaskInstanceRepositoryMock.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        verify(serviceTaskInstanceRepositoryMock).save(entity);
        verify(eventPublisherMock).publishEvent(new TaskInstanceCreatedEvent(flowTaskInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate_notExists() {
        flowTaskInstance.update();
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        entity.setId(flowTaskInstance.getId());
        entity.setStatus(FlowNodeStatus.EXECUTING);

        when(serviceTaskInstanceRepositoryMock.update(entity)).thenReturn(1);

        flowTaskInstance.update();

        verify(serviceTaskInstanceRepositoryMock).update(entity);
    }

    @Test
    public void testDoDelete() {
        when(serviceTaskInstanceRepositoryMock.deleteById(flowTaskInstance.getId())).thenReturn(1);

        flowTaskInstance.doDelete(flowTaskInstance.getId());

        verify(serviceTaskInstanceRepositoryMock).deleteById(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(targetMock);

        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);
        targetMock.setTargetTaskInstanceId(flowTaskInstance.getId());
        flowTaskInstance.targetTaskHandle = targetMock;

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        ServiceTaskStartedListener serviceTaskStartedListener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.dealloc();

        verify(eventPublisher).removeEventListener(serviceTaskStartedListener);
    }

    @Test
    public void testAlloc() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        ServiceTaskStartedListener serviceTaskStartedListener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.alloc();

        verify(eventPublisher).addEventListener(serviceTaskStartedListener);
    }

    @Test
    public void testConfirmExecute() throws InterruptedException, ExecutionException {
        flowTaskInstance.complete(false);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testAbort() throws InterruptedException, ExecutionException {
        flowTaskInstance.complete(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel_notPresent() {
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);
        targetMock.cancel(true);

        flowTaskInstance.targetTaskHandle = targetMock;

        flowTaskInstance.cancel(false);

        verify(targetMock).cancel(false);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);
        targetMock.setDone(true);

        flowTaskInstance.targetTaskHandle = targetMock;

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);
        Object result = "test result";
        when(targetMock.get()).thenReturn(result);

        flowTaskInstance.targetTaskHandle = targetMock;

        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResult_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetMock = mock(BaseRuntimeFlowableDelegate.class);
        Object result = "test result";
        when(targetMock.get(5L, TimeUnit.SECONDS)).thenReturn(result);

        flowTaskInstance.targetTaskHandle = targetMock;

        assertEquals(result, flowTaskInstance.getResult(5L, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        ResourceType expected = ResourceType.ODC_FLOW_TASK_INSTANCE;
        ResourceType actual = flowTaskInstance.resourceType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<BaseRuntimeFlowableDelegate<?>> expected = BaseRuntimeFlowableDelegate.class;
        Class<BaseRuntimeFlowableDelegate<?>> actual = flowTaskInstance.getTargetDelegateClass();

        assertEquals(expected, actual);
    }

    @Test
    public void testComplete_manual() throws InterruptedException, ExecutionException {
        TaskService taskService = mock(TaskService.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        List<Task> tasks = Collections.singletonList(mock(Task.class));
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        Optional<String> processInstanceIdOptional = Optional.of("processInstanceId");

        when(taskServiceMock.createTaskQuery().processInstanceId(processInstanceIdOptional.get())
                .list()).thenReturn(tasks);
        when(flowableAdaptorMock.getFlowableElementByType(flowTaskInstance.getId(), FlowableElementType.USER_TASK,
                FlowableElementType.class)).thenReturn(elements.get(0));

        flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(
                FlowTaskExecutionStrategy.MANUAL), false, false, runtimeDelegateMapperMock, flowableAdaptorMock,
                eventPublisherMock, taskServiceMock, nodeInstanceEntityRepositoryMock,
                sequenceInstanceRepositoryMock, serviceTaskInstanceRepositoryMock);

        flowTaskInstance.complete(false);

        verify(taskServiceMock).complete(tasks.get(0).getId(), Collections.emptyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComplete_automatic() {
        flowTaskInstance.complete(false);
    }
}