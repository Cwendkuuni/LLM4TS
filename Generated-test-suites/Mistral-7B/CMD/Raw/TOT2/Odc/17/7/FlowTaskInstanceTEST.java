import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskServiceMock;
    private RuntimeDelegateMapper runtimeDelegateMapperMock;

    @Before
    public void setUp() {
        taskServiceMock = Mockito.mock(TaskService.class);
        runtimeDelegateMapperMock = Mockito.mock(RuntimeDelegateMapper.class);
        MockitoAnnotations.initMocks(this);

        flowTaskInstance = new FlowTaskInstance(
                new TaskType("taskType1"),
                1L,
                1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true,
                true,
                runtimeDelegateMapperMock,
                Mockito.mock(FlowableAdaptor.class),
                Mockito.mock(EventPublisher.class),
                taskServiceMock,
                Mockito.mock(NodeInstanceEntityRepository.class),
                Mockito.mock(SequenceInstanceRepository.class),
                Mockito.mock(ServiceTaskInstanceRepository.class)
        );
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Given
        ServiceTaskInstanceRepository serviceTaskRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        Mockito.when(taskServiceMock.createTaskQuery()).thenReturn(Mockito.mock(org.flowable.task.api.TaskQuery.class));

        // When
        flowTaskInstance.create();

        // Then
        Mockito.verify(serviceTaskRepositoryMock).save(Mockito.any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBatchCreateFlowTaskInstances() {
        // Given
        ServiceTaskInstanceRepository serviceTaskRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);

        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

        // When
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepositoryMock);

        // Then
        Mockito.verify(serviceTaskRepositoryMock, Mockito.times(1)).batchCreate(Mockito.anyListOf(ServiceTaskInstanceEntity.class));
        assertEquals(instances, result);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        // Given

        // When
        FlowableElementType result = flowTaskInstance.getCoreFlowableElementType();

        // Then
        assertEquals(FlowableElementType.SERVICE_TASK, result);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // When
        boolean result = flowTaskInstance.isPresentOnThisMachine();

        // Then
        assertTrue(result);
    }

    @Test
    public void testDealloc() {
        // Given
        ServiceTaskStartedListener listenerMock = Mockito.mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.setListener(listenerMock);

        // When
        flowTaskInstance.dealloc();

        // Then
        Mockito.verify(listenerMock).onTaskEnd(Mockito.any(Task.class), Mockito.any(Map.class));
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        // Given
        ServiceTaskStartedListener listenerMock = Mockito.mock(ServiceTaskStartedListener.class);
        flowTaskInstance.setListener(listenerMock);

        // When
        flowTaskInstance.alloc();

        // Then
        Mockito.verify(listenerMock).onTaskStart(Mockito.any(Task.class));
        assertSame(listenerMock, flowTaskInstance.getListener());
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        assertSame(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        // Given

        // When
        flowTaskInstance.confirmExecute();

        // Then
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        // Given

        // When
        flowTaskInstance.abort();

        // Then
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
        assertTrue(flowTaskInstance.isAborted());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // When
        flowTaskInstance.cancel(true);

        // Then
        Mockito.verify(taskServiceMock).cancel(Mockito.eq(target), Mockito.eq(true));
        assertNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsDone() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.isDone()).thenReturn(true);

        // When
        boolean result = flowTaskInstance.isDone();

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.get()).thenReturn("result");

        // When
        String result = flowTaskInstance.getResult();

        // Then
        assertEquals("result", result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Mockito.when(target.get(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenThrow(new TimeoutException());

        // When
        try {
            flowTaskInstance.getResult(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Then
            assertTrue(true);
            return;
        }

        // Then
        throw new AssertionError("Unexpected result");
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Given

        // When
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

        // Then
        assertEquals(BaseRuntimeFlowableDelegate.class, result);
    }

    @Test
    public void testGetStrategyConfig() {
        // Given

        // When
        ExecutionStrategyConfig result = flowTaskInstance.getStrategyConfig();

        // Then
        assertEquals(FlowTaskExecutionStrategy.MANUAL, result.getStrategy());
    }
}