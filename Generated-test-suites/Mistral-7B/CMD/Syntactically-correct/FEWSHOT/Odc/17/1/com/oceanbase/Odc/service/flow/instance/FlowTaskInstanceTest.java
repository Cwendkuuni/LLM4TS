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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
 * Test class for {@link FlowTaskInstance}
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
    @Mock
    private ServiceTaskStartedListener listener;
    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
    }

    @Test
    public void testCreate() {
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = ExecutionStrategyConfig.builder()
                .strategy(FlowTaskExecutionStrategy.MANUAL)
                .build();
        boolean startEndpoint = false;
        boolean endEndPoint = false;

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId,
                strategyConfig, startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertTrue(flowTaskInstance.getId() > 0);
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertNull(flowTaskInstance.getTargetTaskId());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertNull(flowTaskInstance.getCreateTime());
        assertNull(flowTaskInstance.getUpdateTime());
        assertFalse(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndPoint());

        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(mock(FlowTaskInstance.class));

        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(1, entities.size());
        assertEquals(instances.get(0).getId(), entities.get(0).getId());
        assertEquals(instances.get(0).getFlowInstanceId(), entities.get(0).getFlowInstanceId());
        assertEquals(instances.get(0).getStatus(), entities.get(0).getStatus());
        assertEquals(instances.get(0).getNodeType(), entities.get(0).getNodeType());
        assertEquals(instances.get(0).getTargetTaskId(), entities.get(0).getTargetTaskId());
        assertEquals(instances.get(0).getStrategy(), entities.get(0).getStrategy());
        assertEquals(instances.get(0).getCreateTime(), entities.get(0).getCreateTime());
        assertEquals(instances.get(0).getUpdateTime(), entities.get(0).getUpdateTime());
        assertEquals(instances.get(0).isStartEndpoint(), entities.get(0).isStartEndpoint());
        assertEquals(instances.get(0).isEndEndPoint(), entities.get(0).isEndEndPoint());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);

        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
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

        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(eq(listener));
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(Optional.of("processInstanceId"));

        flowTaskInstance.confirmExecute();

        verify(taskService, times(1)).createTaskQuery().processInstanceId("processInstanceId")
                .list().forEach(task -> verify(taskService, times(1)).complete(task.getId(), anyMap()));
    }

    @Test
    public void testAbort() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(Optional.of("processInstanceId"));

        flowTaskInstance.abort();

        verify(taskService, times(1)).createTaskQuery().processInstanceId("processInstanceId")
                .list().forEach(task -> verify(taskService, times(1)).complete(task.getId(), anyMap())
                        .withAnyArguments().containingKey(ABORT_VARIABLE_NAME).containingValue(true));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId("processInstanceId").list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(taskService.complete(anyLong(), anyMap()))
                .thenThrow(new RuntimeException("Task completion failed"));

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);

        verify(target, times(1)).cancel(true);
        verify(listener, times(1)).onTaskStarted(flowTaskInstance);
        verify(eventPublisher, times(1)).publishEvent(eq(new TaskInstanceCreatedEvent(flowTaskInstance)));
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId("processInstanceId").list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(target.isDone())
                .thenReturn(false);

        assertFalse(flowTaskInstance.isDone());

        when(target.isDone())
                .thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId("processInstanceId").list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(target.get())
                .thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId("processInstanceId").list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(target.get(anyLong(), any(TimeUnit.class)))
                .thenThrow(new TimeoutException("Timeout"));

        assertNull(flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultUnsupported() {
        assertNull(flowTaskInstance.getResult());
    }

    @Test
    public void testGetTargetDelegateClass() {
        BaseRuntimeFlowableDelegate<String> delegate = mock(BaseRuntimeFlowableDelegate.class);

        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
        assertEquals(String.class, flowTaskInstance.getTargetDelegateClass().getSuperclass());
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass()
                .getSuperclass().getSuperclass());
        assertEquals(String.class, flowTaskInstance.getTargetDelegateClass()
                .getSuperclass().getSuperclass().getSuperclass());

        assertEquals(String.class, flowTaskInstance.delegateConvertor.map(taskType)
                .getInterfaces()[0].getName());
        assertEquals(String.class, flowTaskInstance.delegateConvertor.map(taskType)
                .getSuperclass().getName());
        assertEquals(RuntimeDelegateMapper.class, flowTaskInstance.delegateConvertor.getClass());

        assertEquals(String.class, flowTaskInstance.getTargetDelegateClass().cast(delegate).getClass()
                .getCanonicalName());
    }
}