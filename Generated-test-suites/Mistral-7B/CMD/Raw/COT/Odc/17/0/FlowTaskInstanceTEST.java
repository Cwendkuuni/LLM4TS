package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private ServiceTaskStartedListener listener;
    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                any(FlowableElementType.class))).thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        verify(serviceTaskInstanceRepository, times(1)).batchCreate(entities);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateWithoutValidation() {
        flowTaskInstance.create();
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdateWithoutValidation() {
        flowTaskInstance.update();
    }

    @Test
    public void testDelete() {
        flowTaskInstance.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(1L);
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
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any(FlowableElementType.class));
        verify(taskService, times(1)).completeAll(anyList());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any(FlowableElementType.class));
        verify(taskService, times(1)).completeAll(anyList());
        verify(taskService, times(1)).cancelAllInInstance(eq(flowTaskInstance.getFlowInstanceId()), anyBoolean());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.cancel(true);
        verify(taskService, times(1)).cancelAllInInstance(eq(flowTaskInstance.getFlowInstanceId()), anyBoolean());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenThrow(new InterruptedException());
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecution() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenThrow(new ExecutionException());
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get(1L, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
        flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(delegateConvertor.map(any(TaskType.class))).thenReturn(BaseRuntimeFlowableDelegate.class);
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        when(flowableAdaptor.getFlowableElementByType(eq(flowTaskInstance.getId()), any(FlowableElementType.class),
                any(FlowableElementType.class))).thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));

        flowTaskInstance.complete(false);

        ArgumentCaptor<List<Task>> tasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(taskService, times(1)).completeAll(tasksArgumentCaptor.capture());
        List<Task> tasks = tasksArgumentCaptor.getValue();
        Verify.verify(tasks.size() == 1, "Number of tasks should be equal to 1");
        Verify.verify(tasks.get(0).getName().equals("name"), "Name of task should be equal to 'name'");
    }

    @Test
    public void testCompleteWithException() {
        when(flowableAdaptor.getFlowableElementByType(eq(flowTaskInstance.getId()), any(FlowableElementType.class),
                any(FlowableElementType.class))).thenThrow(new RuntimeException());

        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                any(FlowableElementType.class), any(FlowableElementType.class));
        verifyZeroInteractions(taskService);
    }
}