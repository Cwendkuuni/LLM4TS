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
import java.util.stream.Collectors;

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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.mockito.MockitoAnnotations;

/**
 * Test class for FlowTaskInstance
 */
public class FlowTaskInstanceTest {

    private static final String TEST_FLOW_INSTANCE_ID = "testFlowInstanceId";

    private static final String TEST_TASK_TYPE = "testTaskType";

    private static final Long TEST_TARGET_TASK_ID = 1L;

    private static final String TEST_PROCESS_INSTANCE_ID = "testProcessInstanceId";

    private static final FlowNodeType TEST_NODE_TYPE = FlowNodeType.USER_TASK;

    private static final FlowNodeStatus TEST_NODE_STATUS = FlowNodeStatus.PENDING;

    private static final ExecutionStrategyConfig TEST_STRATEGY_CONFIG = new ExecutionStrategyConfig(
            FlowTaskExecutionStrategy.MANUAL, 1000L, 5000L);

    private static final String TEST_TASK_NAME = "testTaskName";

    private TaskService mockTaskService;

    private EventPublisher mockEventPublisher;

    private NodeInstanceEntityRepository mockNodeRepository;

    private SequenceInstanceRepository mockSequenceRepository;

    private ServiceTaskInstanceRepository mockServiceTaskRepository;

    private FlowableAdaptor mockFlowableAdaptor;

    private RuntimeDelegateMapper mockDelegateConvertor;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockTaskService = mock(TaskService.class);
        mockEventPublisher = mock(EventPublisher.class);
        mockNodeRepository = mock(NodeInstanceEntityRepository.class);
        mockSequenceRepository = mock(SequenceInstanceRepository.class);
        mockServiceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        mockFlowableAdaptor = mock(FlowableAdaptor.class);
        mockDelegateConvertor = mock(RuntimeDelegateMapper.class);
        when(mockEventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(null);

        flowTaskInstance = new FlowTaskInstance(TEST_FLOW_INSTANCE_ID, TEST_TASK_TYPE, TEST_STRATEGY_CONFIG, true,
                true, mockFlowableAdaptor, mockEventPublisher, mockTaskService, mockNodeRepository,
                mockSequenceRepository, mockServiceTaskRepository);
        flowTaskInstance.alloc();
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        when(serviceTaskRepository.batchCreate(anyList()))
                .thenReturn(Collections.emptyList());
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(mockServiceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        verify(mockServiceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        assertEquals(TEST_FLOW_INSTANCE_ID, flowTaskInstance.getId());
        assertEquals(TEST_FLOW_INSTANCE_ID, entity.getId());
        assertEquals(TEST_FLOW_INSTANCE_ID, entity.getFlowInstanceId());
        assertEquals(TEST_NODE_TYPE, flowTaskInstance.getNodeType());
        assertEquals(TEST_NODE_TYPE, entity.getNodeType());
        assertEquals(TEST_NODE_STATUS, flowTaskInstance.getStatus());
        assertEquals(TEST_NODE_STATUS, entity.getStatus());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        entity.setId(TEST_FLOW_INSTANCE_ID);
        entity.setStatus(FlowNodeStatus.EXECUTING);

        int affectRows = 1;
        when(mockServiceTaskRepository.update(entity)).thenReturn(affectRows);

        flowTaskInstance.update();

        verify(mockServiceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
        assertEquals(TEST_NODE_STATUS, flowTaskInstance.getStatus());
        assertEquals(FlowNodeStatus.EXECUTING, entity.getStatus());
    }

    @Test
    public void testDelete() {
        doNothing().when(mockServiceTaskRepository).deleteById(TEST_FLOW_INSTANCE_ID);

        flowTaskInstance.doDelete(TEST_FLOW_INSTANCE_ID);

        verify(mockServiceTaskRepository, times(1)).deleteById(TEST_FLOW_INSTANCE_ID);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());

        flowTaskInstance.bindServiceTask(target);

        verify(mockTaskService, times(1)).saveTaskVariable(eq(TEST_FLOW_INSTANCE_ID), eq("aborted"), any());
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());
        when(target.getTargetTaskInstanceId()).thenReturn(TEST_FLOW_INSTANCE_ID);

        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.addListener(listener);
        flowTaskInstance.dealloc();

        verify(mockEventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        verify(mockEventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.complete(false);

        verify(mockFlowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                any(FlowableElementType.class));
        verify(mockTaskService, times(1)).createTaskQuery().processInstanceId(eq(TEST_PROCESS_INSTANCE_ID))
                .list().stream().filter(task -> task.getName().equals(TEST_TASK_NAME)).collect(Collectors.toList())
                .forEach(task -> taskService.complete(task.getId(), anyMap()));
    }

    @Test
    public void testAbort() {
        flowTaskInstance.complete(true);

        verify(mockFlowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                any(FlowableElementType.class));
        verify(mockTaskService, times(1)).createTaskQuery().processInstanceId(eq(TEST_PROCESS_INSTANCE_ID))
                .list().stream().filter(task -> task.getName().equals(TEST_TASK_NAME)).collect(Collectors.toList())
                .forEach(task -> taskService.complete(task.getId(), anyMap()));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());
        when(target.getId()).thenReturn(TEST_FLOW_INSTANCE_ID);

        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.cancel(true);

        verify(mockTaskService, times(1)).cancel(eq(TEST_FLOW_INSTANCE_ID), anyBoolean());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());
        when(target.isDone()).thenReturn(true);

        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());
        when(target.get()).thenReturn("testResult");

        flowTaskInstance.bindServiceTask(target);

        assertEquals("testResult", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());
        when(target.get(anyLong(), any(TimeUnit.class))).thenReturn("testResult");

        flowTaskInstance.bindServiceTask(target);

        assertEquals("testResult", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultUnsupported() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegateConvertor.map(TEST_TASK_TYPE)).thenReturn(target.getClass());

        flowTaskInstance.getResult();
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance
                .getTargetDelegateClass();
        assertEquals(BaseRuntimeFlowableDelegate.class, targetDelegateClass);
    }
}