package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Unit tests for FlowTaskInstance class
 */
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
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(TaskType.ASYNC_TASK, 1L, 1L,
                new ExecutionStrategyConfig("executionStrategy", FlowTaskExecutionStrategy.MANUAL, 10000),
                true, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities =
                ServiceTaskInstanceTestUtil.createServiceTaskInstanceEntities(instances);
        assertEquals(1, entities.size());
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertEquals(1, result.size());
        assertEquals(flowTaskInstance.getId(), result.get(0).getId());
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskInstanceRepository.save(entity)).thenReturn(entity);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(true);
        flowTaskInstance.create();
        verify(entity).getId();
        verify(entity).getCreateTime();
        verify(entity).getUpdateTime();
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        entity.setId(1L);
        when(serviceTaskInstanceRepository.update(entity)).thenReturn(1);
        flowTaskInstance.update();
        verify(serviceTaskInstanceRepository).update(entity);
    }

    @Test
    public void testDoDelete() {
        doNothing().when(serviceTaskInstanceRepository).deleteById(1L);
        flowTaskInstance.doDelete(1L);
        verify(serviceTaskInstanceRepository).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), anyList());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCompletedEvent.class));
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowableElementType.USER_TASK), anyList());
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCompletedEvent.class));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.cancel(anyBoolean())).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.cancel(true);
        verify(mockDelegate).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.get()).thenReturn("result");
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.get(anyLong(), any(TimeUnit.class))).thenReturn("result");
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertEquals("result", flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterruptedException() throws Exception {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.get()).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithExecutionException() throws Exception {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.get()).thenThrow(new ExecutionException("exception"));
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("timeout"));
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS);
    }
}