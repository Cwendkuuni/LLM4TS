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
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
    private ServiceTaskStartedListener serviceTaskStartedListener;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(true);

        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(new ServiceTaskInstanceEntity());

        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(1);

        when(nodeRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new FlowNodeInstance()));

        when(sequenceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new SequenceInstance()));

        when(serviceTaskRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));

        when(delegateConvertor.map(FlowableElementType.USER_TASK))
                .thenReturn(mock(BaseRuntimeFlowableDelegate.class));

        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
    }

    @Test(expected = AssertionError.class)
    public void testCreateExisting() {
        when(serviceTaskRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));

        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.create();
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();

        assertEquals(FlowNodeStatus.UPDATING, flowTaskInstance.getStatus());
    }

    @Test(expected = AssertionError.class)
    public void testUpdateNonExisting() {
        when(serviceTaskRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        flowTaskInstance.update();
    }

    @Test
    public void testDelete() {
        when(serviceTaskRepository.deleteById(anyLong()))
                .thenReturn(1);

        flowTaskInstance.delete();

        assertEquals(FlowNodeStatus.DELETED, flowTaskInstance.getStatus());
    }

    @Test(expected = AssertionError.class)
    public void testDeleteNonExisting() {
        when(serviceTaskRepository.deleteById(anyLong()))
                .thenReturn(0);

        flowTaskInstance.delete();
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertSame(target, flowTaskInstance.getTargetTaskHandle());
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
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
        verify(eventPublisher, times(1))
                .publishEvent(eq(new TaskInstanceCompletedEvent(any(FlowTaskInstance.class), false)));
    }

    @Test
    public void testCancel() {
        flowTaskInstance.cancel(true);
        verify(taskService, times(1)).cancelTask(anyLong(), anyBoolean());
    }

    @Test
    public void testIsDone() {
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecution() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenThrow(new ExecutionException());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenThrow(new TimeoutException());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class,
                flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(flowTaskInstance, result.get(0));
    }

    @Test
    public void testMapToTaskEntity() {
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
        assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndpoint(), entity.isEndEndpoint());
        assertEquals(flowTaskInstance.strategyConfig.getStrategy(), entity.getStrategy());
        assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
        assertEquals(flowTaskInstance.getTargetTaskId(), entity.getTargetTaskId());
        assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(),
                entity.getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.strategyConfig.getExecutionTime(), entity.getExecutionTime());
    }

    @Test
    public void testValidateNotExists() {
        Verify.notNull(flowTaskInstance.getId(), "id");
        Verify.verify(eventPublisher, times(1))
                .publishEvent(new TaskInstanceCreatedEvent(flowTaskInstance));
    }

    @Test(expected = AssertionError.class)
    public void testValidateExists() {
        when(serviceTaskRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new ServiceTaskInstanceEntity()));

        Verify.notExists(flowTaskInstance, "id");
    }
}