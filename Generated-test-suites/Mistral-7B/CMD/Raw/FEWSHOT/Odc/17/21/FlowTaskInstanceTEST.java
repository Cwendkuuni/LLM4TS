package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.persistence.entity.TaskInstanceEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Unit tests for {@link FlowTaskInstance} class
 *
 * @author yh263208
 */
public class FlowTaskInstanceTest {

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private ServiceTaskStartedListener serviceTaskStartedListener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        // Given
        when(nodeInstanceEntityRepository.findById(anyLong())).thenReturn(mock(NodeInstanceEntity.class));
        when(sequenceInstanceRepository.findById(anyLong())).thenReturn(mock(SequenceInstanceEntity.class));
        when(serviceTaskInstanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        // Then
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testFlowTaskInstanceCreationFromTaskType() {
        // Given
        when(nodeInstanceEntityRepository.findById(anyLong())).thenReturn(mock(NodeInstanceEntity.class));
        when(sequenceInstanceRepository.findById(anyLong())).thenReturn(mock(SequenceInstanceEntity.class));
        when(serviceTaskInstanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                new TaskType("taskType"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        // Then
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        // Given
        // When
        FlowNodeType expected = FlowNodeType.SERVICE_TASK;
        FlowNodeType actual = flowTaskInstance.getCoreFlowableElementType();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testBatchCreate() {
        // Given
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskInstanceRepository.batchCreate(anyList()))
                .thenAnswer(i -> Collections.singletonList(new ServiceTaskInstanceEntity()));

        // When
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        // Then
        assertEquals(instances, result);
    }

    @Test
    public void testCreate() {
        // Given
        when(serviceTaskInstanceRepository.save(any()))
                .thenAnswer(i -> new ServiceTaskInstanceEntity());

        // When
        flowTaskInstance.create();

        // Then
        verify(serviceTaskInstanceRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        // Given
        when(serviceTaskInstanceRepository.update(any()))
                .thenAnswer(i -> 1);

        // When
        flowTaskInstance.update();

        // Then
        verify(serviceTaskInstanceRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        // Given

        // When
        flowTaskInstance.doDelete(1L);

        // Then
        verify(serviceTaskInstanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

        // When
        flowTaskInstance.bindServiceTask(mockDelegate);

        // Then
        assertEquals(mockDelegate, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);

        // When
        boolean result = flowTaskInstance.isPresentOnThisMachine();

        // Then
        assertTrue(result);
    }

    @Test
    public void testDealloc() {
        // Given
        ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();

        // When
        flowTaskInstance.dealloc();

        // Then
        verify(eventPublisher, times(1)).removeEventListener(mockListener);
    }

    @Test
    public void testAlloc() {
        // Given
        ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();

        // When
        flowTaskInstance.alloc();

        // Then
        verify(eventPublisher, times(1)).addEventListener(mockListener);
    }

    @Test
    public void testConfirmExecute() {
        // Given
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowNodeType.USER_TASK, any()))
                .thenReturn(mock(FlowableElement.class));
        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(mock(Task.class)));

        // When
        flowTaskInstance.confirmExecute();

        // Then
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        // Given
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowNodeType.USER_TASK, any()))
                .thenReturn(mock(FlowableElement.class));
        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(mock(Task.class)));

        // When
        flowTaskInstance.abort();

        // Then
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        doReturn(mockDelegate).when(mockDelegate).getId();
        doReturn(true).when(mockDelegate).isDone();
        doThrow(new RuntimeException("Test Exception")).when(mockDelegate).cancel(anyBoolean());

        // When
        flowTaskInstance.cancel(false);

        // Then
        verify(mockDelegate, times(1)).cancel(false);
        verify(log, times(1)).warn("Cancel task failed, instanceId={}, instanceType={}, taskType={}, taskId={}",
                anyLong(), anyString(), anyString(), anyLong(), argThat(t -> t.getMessage().equals("Test Exception")));
    }

    @Test
    public void testIsDone() {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        doReturn(true).when(mockDelegate).isDone();

        // When
        boolean result = flowTaskInstance.isDone();

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        doReturn("Result").when(mockDelegate).get();

        // When
        Object result = flowTaskInstance.getResult();

        // Then
        assertEquals("Result", result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        doAnswer(invocation -> {
            Thread.sleep(100);
            return "Result";
        }).when(mockDelegate).get(anyLong(), any(TimeUnit.class));

        // When
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);

        // Then
        assertEquals("Result", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutTask() {
        // Given

        // When
        flowTaskInstance.getResult();

        // Then
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Given
        Class<? extends BaseRuntimeFlowableDelegate<?>> expected = BaseRuntimeFlowableDelegate.class;

        // When
        Class<? extends BaseRuntimeFlowableDelegate<?>> actual = flowTaskInstance.getTargetDelegateClass();

        // Then
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithoutTask() {
        // Given

        // When
        flowTaskInstance.complete(false);

        // Then
        fail("Expected UnsupportedOperationException");
    }
}