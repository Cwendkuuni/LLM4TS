import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.api.task.Task;
import org.flowable.engine.api.task.TaskQuery;
import org.flowable.engine.api.runtime.ProcessInstance;
import org.flowable.engine.api.runtime.ProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstanceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;
    private TaskService taskServiceMock;
    private EventPublisher eventPublisherMock;
    private NodeInstanceEntityRepository nodeRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    private RuntimeDelegateMapper delegateConvertorMock;
    private FlowableAdaptor flowableAdaptorMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskServiceMock = mock(TaskService.class);
        eventPublisherMock = mock(EventPublisher.class);
        nodeRepositoryMock = mock(NodeInstanceEntityRepository.class);
        sequenceRepositoryMock = mock(SequenceInstanceRepository.class);
        serviceTaskRepositoryMock = mock(ServiceTaskInstanceRepository.class);
        delegateConvertorMock = mock(RuntimeDelegateMapper.class);
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        when(serviceTaskRepositoryMock.save(any(ServiceTaskInstanceEntity.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));

        flowTaskInstance = new FlowTaskInstance(null, delegateConvertorMock, flowableAdaptorMock, eventPublisherMock,
                taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Given
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L, 10L);
        boolean startEndpoint = true;
        boolean endEndpoint = false;
        FlowableElementType nodeType = FlowableElementType.SERVICE_TASK;

        // When
        FlowTaskInstance instance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig,
                startEndpoint, endEndpoint, delegateConvertorMock, flowableAdaptorMock, eventPublisherMock,
                taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
        instance.create();

        // Then
        Assert.assertNotNull(instance.getId());
        Assert.assertNotNull(instance.getCreateTime());
        Assert.assertNotNull(instance.getUpdateTime());
        Assert.assertEquals(organizationId, instance.getOrganizationId());
        Assert.assertEquals(flowInstanceId, instance.getFlowInstanceId());
        Assert.assertEquals(strategyConfig.getStrategy(), instance.getStrategyConfig().getStrategy());
        Assert.assertEquals(startEndpoint, instance.isStartEndpoint());
        Assert.assertEquals(endEndpoint, instance.isEndEndPoint());
        Assert.assertEquals(nodeType, instance.getNodeType());

        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).save(Mockito.any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        // Given
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        // When
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepositoryMock);

        // Then
        Assert.assertEquals(instances.size(), createdInstances.size());
        for (int i = 0; i < instances.size(); i++) {
            Assert.assertEquals(instances.get(i).getId(), createdInstances.get(i).getId());
            Assert.assertEquals(instances.get(i).getCreateTime(), createdInstances.get(i).getCreateTime());
            Assert.assertEquals(instances.get(i).getUpdateTime(), createdInstances.get(i).getUpdateTime());
        }

        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).batchCreate(Mockito.anyListOf(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdateFlowTaskInstance() {
        // Given
        Long instanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        Long targetTaskId = 1L;

        // When
        flowTaskInstance.setId(instanceId);
        flowTaskInstance.setStatus(status);
        flowTaskInstance.setTargetTaskId(targetTaskId);
        flowTaskInstance.update();

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1))
                .update(eq(new ServiceTaskInstanceEntity().setId(instanceId).setStatus(status).setTargetTaskId(targetTaskId)));
    }

    @Test
    public void testDeleteFlowTaskInstance() {
        // Given
        Long instanceId = 1L;

        // When
        flowTaskInstance.doDelete(instanceId);

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertEquals(flowTaskInstance, flowTaskInstance.getTargetTaskHandle().getTaskInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeallocFlowTaskInstanceWhenNotPresent() {
        // Given

        // When
        flowTaskInstance.dealloc();

        // Then
        // This test should throw an UnsupportedOperationException
    }

    @Test
    public void testAllocFlowTaskInstanceWhenPresent() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // When
        flowTaskInstance.alloc();

        // Then
        Mockito.verify(eventPublisherMock, Mockito.times(1)).addEventListener(eq(flowTaskInstance.getListener()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfirmExecuteWhenNotPresent() {
        // Given

        // When
        flowTaskInstance.confirmExecute();

        // Then
        // This test should throw an UnsupportedOperationException
    }

    @Test
    public void testAbort() throws InterruptedException, ExecutionException {
        // Given
        ProcessInstance processInstance = mock(ProcessInstance.class);
        ProcessInstanceQuery processInstanceQueryMock = mock(ProcessInstanceQuery.class);
        when(taskServiceMock.createTaskQuery()).thenReturn(processInstanceQueryMock);
        when(processInstanceQueryMock.processInstanceId(anyLong())).thenReturn(processInstance);
        when(processInstance.getName()).thenReturn("processName");

        // When
        when(flowableAdaptorMock.getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK), any())).thenReturn(mock(FlowableElement.class));
        when(flowableAdaptorMock.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(1L));
        when(taskServiceMock.createTaskQuery()).thenReturn(processInstanceQueryMock);
        when(taskServiceMock.complete(anyLong(), anyMap())).thenReturn(null);

        // When
        flowTaskInstance.abort();

        // Then
        Mockito.verify(taskServiceMock, Mockito.times(1)).complete(eq(flowTaskInstance.getTargetTaskId()), anyMap());
        Mockito.verify(eventPublisherMock, Mockito.times(1)).publishEvent(eq(new TaskInstanceCreatedEvent(flowTaskInstance)));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        // When
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);

        // Then
        Mockito.verify(target, Mockito.times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        Assert.assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(5L, TimeUnit.SECONDS)).thenThrow(new TimeoutException());

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        Assert.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(5L, TimeUnit.SECONDS));
    }

}