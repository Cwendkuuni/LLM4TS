package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @date 2023-03-13 15:15
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
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandleMock;

    @Mock
    private ServiceTaskStartedListener listenerMock;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private Long flowInstanceId;
    private Long id;
    private Long organizationId;
    private Long targetTaskId;
    private FlowNodeType nodeType;
    private FlowableElementType flowableElementType;
    private TaskType taskType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        flowInstanceId = 1L;
        id = 2L;
        organizationId = 3L;
        nodeType = FlowNodeType.SERVICE_TASK;
        flowableElementType = FlowableElementType.USER_TASK;
        taskType = TaskType.PROCESS;

        when(serviceTaskRepository.save(any())).thenReturn(serviceTaskInstanceEntity);
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(1);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(nodeType), eq(flowableElementType)))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(flowInstanceId));
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)).list())
                .thenReturn(Collections.singletonList(new TaskInfo()));

        when(delegateConvertor.map(eq(taskType))).thenReturn(BaseRuntimeFlowableDelegate.class);
        when(serviceTaskInstanceEntity.getId()).thenReturn(id);
        when(serviceTaskInstanceEntity.getOrganizationId()).thenReturn(organizationId);
        when(serviceTaskInstanceEntity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(serviceTaskInstanceEntity.getFlowInstanceId()).thenReturn(flowInstanceId);
        when(serviceTaskInstanceEntity.getTaskType()).thenReturn(taskType);
        when(serviceTaskInstanceEntity.getTargetTaskId()).thenReturn(targetTaskId);
        when(serviceTaskInstanceEntity.getStrategy()).thenReturn(FlowTaskExecutionStrategy.MANUAL);
        when(serviceTaskInstanceEntity.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(serviceTaskInstanceEntity.getCreateTime()).thenReturn(System.currentTimeMillis());
        when(serviceTaskInstanceEntity.getUpdateTime()).thenReturn(System.currentTimeMillis());
        when(serviceTaskInstanceEntity.isStartEndpoint()).thenReturn(false);
        when(serviceTaskInstanceEntity.isEndEndpoint()).thenReturn(false);

        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithExistingInstance() {
        when(serviceTaskRepository.findById(eq(id))).thenReturn(Optional.of(serviceTaskInstanceEntity));
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
    }

    @Test
    public void testUpdate() {
        verify(serviceTaskRepository, times(0)).update(any());

        flowTaskInstance.update();

        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test
    public void testDoDelete() {
        verify(serviceTaskRepository, times(1)).deleteById(eq(id));
    }

    @Test
    public void testBindServiceTask() {
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)).list())
                .thenReturn(Collections.singletonList(new TaskInfo()));
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)).list().get(0).getId())
                .thenReturn(targetTaskId);
        when(delegateConvertor.map(eq(taskType))).thenReturn(BaseRuntimeFlowableDelegate.class);
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(targetTaskHandle);

        verify(taskService, times(1)).createTask(anyString(), anyMap(), anyLong(), eq(flowInstanceId), any());
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(id), eq(nodeType), eq(flowableElementType))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowInstanceId))
                .thenReturn(Optional.of(flowInstanceId));
        verify(targetTaskHandle, times(1)).setId(eq(id));
        verify(targetTaskHandle, times(1)).setFlowableAdaptor(eq(flowableAdaptor));
        verify(targetTaskHandle, times(1)).setServiceTaskInstance(eq(flowTaskInstance));
        verify(flowTaskInstance, times(1)).setTargetTaskHandle(targetTaskHandle);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());

        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)).list())
                .thenReturn(Collections.singletonList(new TaskInfo()));
        when(taskService.createTaskQuery().processInstanceId(eq(flowInstanceId)).list().get(0).getId())
                .thenReturn(targetTaskId);
        when(delegateConvertor.map(eq(taskType))).thenReturn(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(eq(listenerMock));
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(eq(listenerMock));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(id), eq(nodeType), eq(flowableElementType))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId)).list()
                .stream().filter(t -> t.getName().equals(new FlowableElement().getName()))
                .findFirst().ifPresent(task -> taskService.complete(task.getId(), anyMap()));
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(id), eq(nodeType), eq(flowableElementType))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(eq(flowInstanceId)).list()
                .stream().filter(t -> t.getName().equals(new FlowableElement().getName()))
                .findFirst().ifPresent(task -> taskService.complete(task.getId(), anyMap())).thenCallRealMethod();
        verify(taskService, times(1)).complete(anyString(), anyMap());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        verify(targetTaskHandleMock, times(1)).cancel(anyBoolean());
    }

    @Test
    public void testIsDone() {
        verify(targetTaskHandleMock, times(0)).isDone();

        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        verify(targetTaskHandleMock, times(1)).isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        verify(targetTaskHandleMock, times(1)).get();
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandleMock.isDone()).thenReturn(false);

        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        verify(targetTaskHandleMock, times(1)).get(anyLong(), any());
    }

    @Test
    public void testGetResultWithInterruptedException()
            throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandleMock.isDone()).thenReturn(false);
        when(targetTaskHandleMock.isInterrupted()).thenReturn(true);

        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        verify(targetTaskHandleMock, times(1)).get(anyLong(), any());
        verify(targetTaskHandleMock, times(1)).isInterrupted();
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        verify(serviceTaskRepository, times(1)).batchCreate(any());
        assertEquals(1, entities.size());
        assertEquals(flowTaskInstance.getId(), entities.get(0).getId());
        assertEquals(flowTaskInstance.getOrganizationId(), entities.get(0).getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), entities.get(0).getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), entities.get(0).getFlowInstanceId());
        assertEquals(flowTaskInstance.getCreationTime(), entities.get(0).getCreateTime());
        assertEquals(flowTaskInstance.getUpdateTime(), entities.get(0).getUpdateTime());
        assertEquals(flowTaskInstance.isStartEndpoint(), entities.get(0).isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndpoint(), entities.get(0).isEndEndpoint());
        assertEquals(flowTaskInstance.getFlowNodeType(), entities.get(0).getFlowNodeType());
        assertEquals(flowTaskInstance.getFlowableElementType(), entities.get(0).getFlowableElementType());
        assertEquals(flowTaskInstance.getTaskType(), entities.get(0).getTaskType());
        assertEquals(flowTaskInstance.getStrategyConfig().getStrategy(), entities.get(0).getStrategy());
        assertEquals(flowTaskInstance.getStrategyConfig().getPendingExpireIntervalSeconds(),
                entities.get(0).getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.getStrategyConfig().getExecutionTime(), entities.get(0).getExecutionTime());
    }

}