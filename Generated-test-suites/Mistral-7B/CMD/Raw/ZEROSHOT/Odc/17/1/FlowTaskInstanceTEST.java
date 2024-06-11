package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Test class for {@link FlowTaskInstance}.
 */
public class FlowTaskInstanceTest {

    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowNodeStatus INITIAL_STATUS = FlowNodeStatus.PENDING;
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final Long TARGET_TASK_ID = 1L;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig(
            FlowTaskExecutionStrategy.MANUAL, 5000L);
    private static final TaskType TASK_TYPE = TaskType.human;

    private TaskService taskServiceMock;
    private EventPublisher eventPublisherMock;
    private NodeInstanceEntityRepository nodeRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    private FlowableAdaptor flowableAdaptorMock;
    private RuntimeDelegateMapper delegateConvertorMock;
    private FlowTaskInstance instanceUnderTest;

    @Before
    public void setup() {
        taskServiceMock = Mockito.mock(TaskService.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        nodeRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);
        serviceTaskRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
        delegateConvertorMock = Mockito.mock(RuntimeDelegateMapper.class);

        instanceUnderTest = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
                false, false, delegateConvertorMock, flowableAdaptorMock, eventPublisherMock, taskServiceMock,
                nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, instanceUnderTest.getCoreFlowableElementType());
    }

    @Test
    public void testCreate() {
        // Given
        ServiceTaskInstanceEntity entityMock = Mockito.mock(ServiceTaskInstanceEntity.class);
        Mockito.when(serviceTaskRepositoryMock.save(entityMock)).thenReturn(entityMock);

        // When
        instanceUnderTest.create();

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).save(entityMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateAlreadyExists() {
        // Given
        ServiceTaskInstanceEntity entityMock = Mockito.mock(ServiceTaskInstanceEntity.class);
        Mockito.when(serviceTaskRepositoryMock.findById(instanceUnderTest.getId())).thenReturn(Optional.of(entityMock));

        // When
        instanceUnderTest.create();

        // Then
        fail();
    }

    @Test
    public void testUpdate() {
        // Given
        ServiceTaskInstanceEntity entityMock = Mockito.mock(ServiceTaskInstanceEntity.class);

        Mockito.when(serviceTaskRepositoryMock.update(entityMock)).thenReturn(1);

        // When
        instanceUnderTest.update();

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).update(entityMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteNonExistent() {
        // Given

        // When
        instanceUnderTest.doDelete(instanceUnderTest.getId());

        // Then
        fail();
    }

    @Test
    public void testDeleteExistent() {
        // Given
        Mockito.when(serviceTaskRepositoryMock.findById(instanceUnderTest.getId())).thenReturn(Optional.of(
                new ServiceTaskInstanceEntity()));

        // When
        instanceUnderTest.doDelete(instanceUnderTest.getId());

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).deleteById(instanceUnderTest.getId());
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        // When
        instanceUnderTest.bindServiceTask(targetMock);

        // Then
        Mockito.verify(instanceUnderTest, Mockito.times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        instanceUnderTest.bindServiceTask(targetMock);

        // When
        boolean result = instanceUnderTest.isPresentOnThisMachine();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsPresentOnThisMachineFalse() {
        // Given

        // When
        boolean result = instanceUnderTest.isPresentOnThisMachine();

        // Then
        assertFalse(result);
    }

    @Test
    public void testDealloc() {
        // Given
        ServiceTaskStartedListener listenerMock = Mockito.mock(ServiceTaskStartedListener.class);

        instanceUnderTest.alloc();
        instanceUnderTest.setListener(listenerMock);

        // When
        instanceUnderTest.dealloc();

        // Then
        Mockito.verify(instanceUnderTest, Mockito.times(1)).setListener(null);
        Mockito.verify(eventPublisherMock, Mockito.times(1)).removeEventListener(listenerMock);
    }

    @Test
    public void testAlloc() {
        // Given
        ServiceTaskStartedListener listenerMock = Mockito.mock(ServiceTaskStartedListener.class);

        instanceUnderTest.setListener(null);

        // When
        instanceUnderTest.alloc();

        // Then
        Mockito.verify(instanceUnderTest, Mockito.times(1)).setListener(listenerMock);
        Mockito.verify(eventPublisherMock, Mockito.times(1)).addEventListener(listenerMock);
    }

    @Test
    public void testConfirmExecute() {
        // Given
        List<FlowableElement> elementsMock = Collections.singletonList(Mockito.mock(FlowableElement.class));
        Mockito.when(flowableAdaptorMock.getFlowableElementByType(instanceUnderTest.getId(), NODE_TYPE,
                FlowableElementType.USER_TASK)).thenReturn(elementsMock);
        String processInstanceIdMock = "123";

        // When
        instanceUnderTest.confirmExecute();

        // Then
        Mockito.verify(flowableAdaptorMock, Mockito.times(1))
                .getProcessInstanceIdByFlowInstanceId(instanceUnderTest.getFlowInstanceId());
        Mockito.verify(taskServiceMock, Mockito.times(elementsMock.size()))
                .complete(Mockito.anyLong(), Mockito.eq(Collections.singletonMap(ABORT_VARIABLE_NAME, false)));
    }

    @Test
    public void testAbort() {
        // Given
        List<FlowableElement> elementsMock = Collections.singletonList(Mockito.mock(FlowableElement.class));
        Mockito.when(flowableAdaptorMock.getFlowableElementByType(instanceUnderTest.getId(), NODE_TYPE,
                FlowableElementType.USER_TASK)).thenReturn(elementsMock);
        String processInstanceIdMock = "123";

        // When
        instanceUnderTest.abort();

        // Then
        Mockito.verify(flowableAdaptorMock, Mockito.times(1))
                .getProcessInstanceIdByFlowInstanceId(instanceUnderTest.getFlowInstanceId());
        Mockito.verify(taskServiceMock, Mockito.times(elementsMock.size()))
                .complete(Mockito.anyLong(), Mockito.eq(Collections.singletonMap(ABORT_VARIABLE_NAME, true)));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        Mockito.when(delegateConvertorMock.map(instanceUnderTest.getTaskType())).thenReturn(targetMock.getClass());
        instanceUnderTest.bindServiceTask(targetMock);

        // When
        instanceUnderTest.cancel(true);

        // Then
        Mockito.verify(targetMock, Mockito.times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        Mockito.when(delegateConvertorMock.map(instanceUnderTest.getTaskType())).thenReturn(targetMock.getClass());
        instanceUnderTest.bindServiceTask(targetMock);

        // When
        Mockito.when(targetMock.isDone()).thenReturn(true);

        // Then
        assertTrue(instanceUnderTest.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        Mockito.when(delegateConvertorMock.map(instanceUnderTest.getTaskType())).thenReturn(targetMock.getClass());
        instanceUnderTest.bindServiceTask(targetMock);

        // When
        Object result = instanceUnderTest.getResult();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        Mockito.when(delegateConvertorMock.map(instanceUnderTest.getTaskType())).thenReturn(targetMock.getClass());
        instanceUnderTest.bindServiceTask(targetMock);

        // When
        Object result = instanceUnderTest.getResult(1000, TimeUnit.MILLISECONDS);

        // Then
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutPresent() {
        // Given

        // When
        instanceUnderTest.getResult();

        // Then
        fail();
    }

}