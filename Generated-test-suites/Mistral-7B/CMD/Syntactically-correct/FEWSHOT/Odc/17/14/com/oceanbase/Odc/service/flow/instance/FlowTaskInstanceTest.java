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

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link FlowTaskInstance}
 */
public class FlowTaskInstanceTest {

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
    private EventPublisher eventPublisher;

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Mock
    private Task task;

    @Mock
    private TaskInfo taskInfo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(1);
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class))).thenReturn(Collections.emptyList());
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L, 1L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndPoint());
        assertNull(flowTaskInstance.getId());
        assertNull(flowTaskInstance.getCreateTime());
        assertNull(flowTaskInstance.getUpdateTime());
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testFlowTaskInstanceCreationWithTargetTaskId() {
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        Long targetTaskId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L, 1L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndPoint());
        assertEquals(targetTaskId, flowTaskInstance.getTargetTaskId());
        assertNull(flowTaskInstance.getId());
        assertNull(flowTaskInstance.getCreateTime());
        assertNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = new ArrayList<>();
        assertEquals(Collections.emptyList(), FlowTaskInstance.batchCreate(Collections.emptyList(), serviceTaskRepository));
        assertEquals(1, FlowTaskInstance.batchCreate(instances, serviceTaskRepository).size());
        entities.add(mock(ServiceTaskInstanceEntity.class));
        when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);
        assertEquals(1, FlowTaskInstance.batchCreate(instances, serviceTaskRepository).size());
        verify(serviceTaskRepository, times(1)).batchCreate(entities);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        flowTaskInstance.create();
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
        verify(serviceTaskRepository, times(1)).save(entity);
        verify(eventPublisher, times(1)).publishEvent(isA(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.update(entity)).thenReturn(1);
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.UPDATED, flowTaskInstance.getStatus());
        verify(serviceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testDelete() {
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(1);
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        flowTaskInstance.doDelete(1L);
        assertEquals(FlowNodeStatus.DELETED, flowTaskInstance.getStatus());
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        when(taskService.createTaskQuery().taskId(anyString())).thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("taskName");
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK, FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()), eq(FlowableElementType.USER_TASK),
                any(FlowableElementType.class));
        verify(taskService, times(1)).createTaskQuery().taskId(eq("taskId"));
        verify(taskService, times(1)).complete(task.getId(), anyMap());
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        assertNull(flowTaskInstance.getListener());
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance.getListener());
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK, FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("taskName");
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()), eq(FlowableElementType.USER_TASK),
                any(FlowableElementType.class));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowTaskInstance.getFlowInstanceId()));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq("processInstanceId"));
        verify(taskService, times(1)).complete(task.getId(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.abort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() {
        when(targetTaskHandle.isDone()).thenReturn(false);
        assertFalse(flowTaskInstance.isDone());

        when(targetTaskHandle.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        when(targetTaskHandle.get()).thenReturn("result");
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn("result");
        assertEquals("result", flowTaskInstance.getResult(100L, TimeUnit.MILLISECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutException() throws Exception {
        when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());
        flowTaskInstance.getResult(100L, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> clazz = flowTaskInstance.getTargetDelegateClass();
        assertEquals(Mockito.mock(BaseRuntimeFlowableDelegate.class), clazz.newInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithAutomaticStrategy() {
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK, FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("taskName");
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 0L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.complete(true));
    }
}