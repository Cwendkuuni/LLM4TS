import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

/**
 * Test class for FlowTaskInstance.
 */
public class FlowTaskInstanceTest {

    private TaskService taskService;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private FlowableAdaptor flowableAdaptor;
    private RuntimeDelegateMapper delegateConvertor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(taskService, eventPublisher, nodeRepository, sequenceRepository, serviceTaskRepository,
                flowableAdaptor, delegateConvertor);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(1L, flowTaskInstance.getOrganizationId());
        assertEquals(1L, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndPoint());
        assertEquals(FlowableElementType.SERVICE_TASK, flowableAdaptor.getFlowableElementType(flowTaskInstance));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> flowTaskInstances = Collections.singletonList(new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(flowTaskInstances, serviceTaskRepository);
        assertEquals(1, result.size());
        assertEquals(flowTaskInstances.get(0), result.get(0));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.create();
        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        verify(serviceTaskRepository).save(entityCaptor.capture());
        ServiceTaskInstanceEntity entity = entityCaptor.getValue();
        assertEquals(FlowNodeType.SERVICE_TASK, entity.getNodeType());
        assertEquals(1L, entity.getOrganizationId());
        assertEquals(1L, entity.getFlowInstanceId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(false, entity.isStartEndpoint());
        assertEquals(false, entity.isEndEndpoint());
        assertEquals(FlowableElementType.SERVICE_TASK, flowableAdaptor.getFlowableElementType(entity));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteWhenNotPresent() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachineWhenNotPresent() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachineWhenPresent() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testAlloc() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.confirmExecute();
        // TODO: Implement verification of execution nodes
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.abort();
        // TODO: Implement verification of execution nodes
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(target.cancel(anyBoolean())).thenReturn(true);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenNotPresent() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenReturn("result");

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        assertEquals("result", flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutExpired() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("Timeout"));

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
        assertEquals(1L, entity.getOrganizationId());
        assertEquals(FlowNodeType.SERVICE_TASK, entity.getNodeType());
        assertEquals(1L, entity.getFlowInstanceId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(false, entity.isStartEndpoint());
        assertEquals(false, entity.isEndEndpoint());
        assertEquals(FlowableElementType.SERVICE_TASK, flowableAdaptor.getFlowableElementType(entity));
    }
}