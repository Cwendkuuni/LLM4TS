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
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Unit tests for FlowTaskInstance class
 *
 * @author yh263208
 * @date 2023-03-29 11:30
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

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateServiceTaskInstance() {
        // Given
        when(nodeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(sequenceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                new TaskType("testTaskType"),
                1L,
                1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true,
                true,
                delegateConvertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository);

        // Then
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
        assertEquals(1L, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
        assertEquals(FlowTaskExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
    }

    @Test
    public void testBatchCreateServiceTaskInstance() {
        // Given
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(
                new TaskType("testTaskType"),
                1L,
                1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true,
                true,
                delegateConvertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository));

        // When
        List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        // Then
        assertEquals(1, entities.size());
        assertEquals(instances.get(0).getId(), entities.get(0).getId());
        assertEquals(instances.get(0).getCreateTime(), entities.get(0).getCreateTime());
        assertEquals(instances.get(0).getUpdateTime(), entities.get(0).getUpdateTime());
    }

    @Test
    public void testCreateWithExistingInstance() {
        // Given
        ServiceTaskInstanceEntity existingEntity = new ServiceTaskInstanceEntity();
        existingEntity.setId(1L);
        existingEntity.setCreateTime(System.currentTimeMillis());
        existingEntity.setUpdateTime(System.currentTimeMillis());
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(existingEntity);

        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                new TaskType("testTaskType"),
                1L,
                1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true,
                true,
                delegateConvertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository);

        // Then
        assertEquals(existingEntity.getId(), flowTaskInstance.getId());
        assertEquals(existingEntity.getCreateTime(), flowTaskInstance.getCreateTime());
        assertEquals(existingEntity.getUpdateTime(), flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdateServiceTaskInstance() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                new TaskType("testTaskType"),
                1L,
                1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true,
                true,
                delegateConvertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository);

        // When
        flowTaskInstance.update();

        // Then
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDeleteServiceTaskInstance() {
        // Given
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(true);

        // When
        flowTaskInstance.doDelete(1L);

        // Then
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        // When
        flowTaskInstance.bindServiceTask(mockTaskHandle);

        // Then
        assertEquals(mockTaskHandle, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDeallocate() {
        // Given
        flowTaskInstance.alloc();

        // When
        flowTaskInstance.dealloc();

        // Then
        verify(eventPublisher, times(1)).removeEventListener(flowTaskInstance.listener);
        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAllocate() {
        // Given
        flowTaskInstance.dealloc();

        // When
        flowTaskInstance.alloc();

        // Then
        verify(eventPublisher, times(1)).addEventListener(flowTaskInstance.listener);
        assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        // Given
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(Collections.emptyList());
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());

        // When
        flowTaskInstance.confirmExecute();

        // Then
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void testAbort() {
        // Given
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(mock(Task.class)));

        // When
        flowTaskInstance.abort();

        // Then
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
        verify(taskService, times(1)).complete(any(Task.class).getId(), anyMap());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(mockTaskHandle.cancel(anyBoolean())).thenReturn(true);

        // When
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        flowTaskInstance.cancel(true);

        // Then
        verify(mockTaskHandle, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        // Given
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(mockTaskHandle.isDone()).thenReturn(true);

        // When
        flowTaskInstance.bindServiceTask(mockTaskHandle);

        // Then
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(mockTaskHandle.get()).thenReturn("testResult");

        // When
        flowTaskInstance.bindServiceTask(mockTaskHandle);

        // Then
        assertEquals("testResult", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(mockTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn("testResult");

        // When
        flowTaskInstance.bindServiceTask(mockTaskHandle);

        // Then
        assertEquals("testResult", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
}