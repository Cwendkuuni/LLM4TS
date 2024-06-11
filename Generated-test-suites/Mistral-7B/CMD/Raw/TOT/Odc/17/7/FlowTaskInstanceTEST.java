package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance.FlowTaskInstanceBuilder;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final Long ID = 1L;
    private static final Long TARGET_TASK_ID = 2L;
    private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.MANUAL;

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setup() {
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);

        when(nodeRepository.findById(ID)).thenReturn(Optional.ofNullable(new NodeInstanceEntity()));
        when(sequenceRepository.findById(ID)).thenReturn(Optional.ofNullable(new SequenceInstanceEntity()));
        when(serviceTaskRepository.findById(ID)).thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));

        when(delegateConvertor.map(TaskType.USER_TASK)).thenReturn(Mockito.mock(UserTaskRuntimeDelegate.class));

        FlowTaskInstanceBuilder builder = new FlowTaskInstanceBuilder(ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance = builder.build();
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Given

        // When
        flowTaskInstance.create();

        // Then
        Verify.notNull(flowTaskInstance.getId(), "Id");
        Verify.notNull(flowTaskInstance.getCreateTime(), "CreateTime");
        Verify.notNull(flowTaskInstance.getUpdateTime(), "UpdateTime");
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        // Given
        flowTaskInstance.bindServiceTask(target);

        // Then
        assertEquals(flowTaskInstance, target.getTargetTaskInstance());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskToDifferentInstance() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance otherInstance = new FlowTaskInstance(
                new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        // Given
        otherInstance.bindServiceTask(target);

        // Then
        // AssertionError should be thrown
        flowTaskInstance.bindServiceTask(target);
    }

    @Test
    public void testDealloc() {
        // Given

        // When
        flowTaskInstance.dealloc();

        // Then
        verify(eventPublisher, times(1)).removeEventListener(flowTaskInstance.listener);
        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        // Given

        // When
        flowTaskInstance.alloc();

        // Then
        verify(eventPublisher, times(1)).addEventListener(flowTaskInstance.listener);
        assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        // Given

        // When
        flowTaskInstance.confirmExecute();

        // Then
        // Completion logic should be executed
        verify(flowableAdaptor, times(1)).getFlowableElementByType(ID, NODE_TYPE, FlowableElementType.USER_TASK);
        verify(taskService, times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
        verify(taskService, times(elementsInList(flowableAdaptor.getFlowableElementByType(ID, NODE_TYPE,
                FlowableElementType.USER_TASK).size()))).complete(anyInt(), anyMap());
    }

    @Test
    public void testAbort() {
        // Given

        // When
        flowTaskInstance.abort();

        // Then
        // Completion logic should be executed with abort flag
        verify(flowableAdaptor, times(1)).getFlowableElementByType(ID, NODE_TYPE, FlowableElementType.USER_TASK);
        verify(taskService, times(elementsInList(flowableAdaptor.getFlowableElementByType(ID, NODE_Type,
                FlowableElementType.USER_TASK).size()))).complete(anyInt(), anyMap());
        assertEquals(true, flowTaskInstance.isAborted());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        // When
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);

        // Then
        verify(target, times(1)).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotPresentTask() {
        // Given

        // When
        flowTaskInstance.cancel(true);

        // Then
        // UnsupportedOperationException should be thrown
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("Result");

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new InterruptedException());

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        // InterruptedException should be thrown
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new ExecutionException("ExecutionException"));

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        // ExecutionException should be thrown
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        // TimeoutException should be thrown
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(UserTaskRuntimeDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    private int elementsInList(int size) {
        return size;
    }

}

class UserTaskRuntimeDelegate {}