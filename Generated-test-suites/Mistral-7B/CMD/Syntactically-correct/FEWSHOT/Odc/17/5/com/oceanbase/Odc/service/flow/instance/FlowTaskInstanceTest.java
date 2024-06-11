package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for FlowTaskInstance
 *
 * @author <a href="mailto:yh263208@gmail.com">yh263208</a>
 * @date 2023-03-27 10:21
 * @since ODC_release_3.3.0
 */
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

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);
        flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = mock(List.class);
        when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Verify.notNull(result.get(0).getId());
        Verify.notNull(result.get(0).getCreateTime());
        Verify.notNull(result.get(0).getUpdateTime());
    }

    @Test
    public void testCreate() throws Exception {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);
        flowTaskInstance.create();
        Verify.notNull(flowTaskInstance.getId());
        Verify.notNull(flowTaskInstance.getCreateTime());
        Verify.notNull(flowTaskInstance.getUpdateTime());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpdate() {
        flowTaskInstance.update();
    }

    @Test
    public void testDelete() {
        long instanceId = 1L;
        flowTaskInstance.doDelete(instanceId);
        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.targetTaskHandle);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(flowTaskInstance.getId());
        when(target.isDone()).thenReturn(false);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target, times(1)).cancel(true);
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
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(30, TimeUnit.SECONDS)).thenReturn("result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("result", flowTaskInstance.getResult(30, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutTask() {
        flowTaskInstance.getResult();
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = mock(Class.class);
        when(delegateConvertor.map(FlowableElementType.USER_TASK)).thenReturn(targetDelegateClass);
        assertEquals(targetDelegateClass, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException {
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        String pid = "pid";
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(), FlowableElementType.USER_TASK, any()))
                .thenReturn(elements.get(0));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(Optional.of(pid));
        when(taskService.createTaskQuery().processInstanceId(pid).list()).thenReturn(Collections.singletonList(taskInfo));

        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testCompleteWithAborted() throws InterruptedException, ExecutionException {
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        String pid = "pid";
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(), FlowableElementType.USER_TASK, any()))
                .thenReturn(elements.get(0));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(Optional.of(pid));
        when(taskService.createTaskQuery().processInstanceId(pid).list()).thenReturn(Collections.singletonList(taskInfo));

        flowTaskInstance.complete(true);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), any());
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(taskInfo, times(1)).setVariableLocal(eq(ABORT_VARIABLE_NAME), any());
    }
}