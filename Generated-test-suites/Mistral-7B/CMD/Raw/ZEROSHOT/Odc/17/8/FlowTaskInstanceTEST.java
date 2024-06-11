package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

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
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test class for FlowTaskInstance
 *
 * @author yh263208
 * @date 2023-03-24 11:06
 */
@Slf4j
public class FlowTaskInstanceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

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
    private ServiceTaskStartedListener listener;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        when(eventPublisher.removeEventListener(any()))
                .thenReturn(true);
        when(eventPublisher.addEventListener(any()))
                .thenReturn(listener);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskInstanceRepository, flowableAdaptor, eventPublisher, delegateConvertor, listener,
                targetTaskHandle);
    }

    @Test
    public void testCreate() {
        when(serviceTaskInstanceRepository.save(any()))
                .thenReturn(mock(ServiceTaskInstanceEntity.class));

        FlowTaskInstance flowTaskInstance =
                new FlowTaskInstance(TaskType.SEQUENTIAL, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                        false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                        nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.create();

        verify(taskService, times(1)).saveTask(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.SEQUENTIAL, 1L, 1L,
                FlowNodeStatus.PENDING, false, false, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.update();

        verify(serviceTaskInstanceRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        when(serviceTaskInstanceRepository.deleteById(1L))
                .thenReturn(1);

        flowTaskInstance.delete(1L);

        verify(serviceTaskInstanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        when(targetTaskHandle.getTargetTaskInstanceId())
                .thenReturn(1L);

        flowTaskInstance.bindServiceTask(targetTaskHandle);

        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // given
        when(targetTaskHandle != null)
                .thenReturn(true);

        // when
        boolean result = flowTaskInstance.isPresentOnThisMachine();

        // then
        assertTrue(result);
    }

    @Test
    public void testDealloc() {
        // given
        when(eventPublisher.removeEventListener(listener))
                .thenReturn(true);

        // when
        flowTaskInstance.dealloc();

        // then
        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        // given
        when(eventPublisher.addEventListener(listener))
                .thenReturn(listener);

        // when
        flowTaskInstance.alloc();

        // then
        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        // given
        when(flowableAdaptor.getFlowableElementByType(1L, FlowableElementType.USER_TASK, FlowableElement.class))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(taskService.createTaskQuery().processInstanceId("1")
                .list())
                .thenReturn(Collections.singletonList(mock(Task.class)));

        // when
        flowTaskInstance.confirmExecute();

        // then
        verify(taskService, times(1)).createTaskQuery().processInstanceId("1")
                .list();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(1L, FlowableElementType.USER_TASK, FlowableElement.class);
    }

    @Test
    public void testAbort() {
        // given
        when(flowableAdaptor.getFlowableElementByType(1L, FlowableElementType.USER_TASK, FlowableElement.class))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(taskService.createTaskQuery().processInstanceId("1")
                .list())
                .thenReturn(Collections.singletonList(mock(Task.class)));

        // when
        flowTaskInstance.abort();

        // then
        verify(taskService, times(1)).createTaskQuery().processInstanceId("1")
                .list();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(1L, FlowableElementType.USER_TASK, FlowableElement.class);
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandle.cancel(anyBoolean()))
                .thenReturn(true);

        // when
        flowTaskInstance.cancel(true);

        // then
        verify(targetTaskHandle, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        when(targetTaskHandle.isDone())
                .thenReturn(true);

        // when
        boolean result = flowTaskInstance.isDone();

        // then
        assertTrue(result);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(targetTaskHandle.get())
                .thenReturn("result");

        // when
        String result = (String) flowTaskInstance.getResult();

        // then
        assertEquals("result", result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandle.get(anyLong(), any()))
                .thenReturn("result");

        // when
        String result = (String) flowTaskInstance.getResult(1L, TimeUnit.SECONDS);

        // then
        assertEquals("result", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutTask() {
        // given
        when(isPresentOnThisMachine()).thenReturn(false);

        // when
        flowTaskInstance.getResult();

        // then
        fail();
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(delegateConvertor.map(TaskType.SEQUENTIAL))
                .thenReturn(BaseRuntimeFlowableDelegate.class);

        // when
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

        // then
        assertEquals(BaseRuntimeFlowableDelegate.class, result);
    }
}