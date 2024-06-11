import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(mock(TaskType.class), 1L, 1L, new ExecutionStrategyConfig(), false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
    }

    // Basic test cases for public methods

    @Test
    public void testGetCoreFlowableElementType() {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(ArgumentMatchers.list(ArgumentMatchers.isInstance(ServiceTaskInstanceEntity.class)))).thenReturn(Collections.singletonList(new ServiceTaskInstanceEntity()));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Assert.assertEquals(instances, result);
    }

    @Test
    public void testCreate() {
        when(serviceTaskRepository.save(ArgumentMatchers.isInstance(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
        flowTaskInstance.create();
        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.EXECUTING);
        when(serviceTaskRepository.update(ArgumentMatchers.isInstance(ServiceTaskInstanceEntity.class))).thenReturn(1);
        flowTaskInstance.update();
    }

    @Test
    public void testDelete() {
        flowTaskInstance.doDelete(1L);
        verify(serviceTaskRepository).deleteById(ArgumentMatchers.eq(1L));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertEquals(flowTaskInstance.getId(), target.getTargetTaskInstanceId());
        Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        Assert.assertFalse(flowTaskInstance.isPresentOnThisMachine());
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher).removeEventListener(ArgumentMatchers.eq(listener));
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher).addEventListener(ArgumentMatchers.eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.complete(false);
        verify(flowableAdaptor).getFlowableElementByType(ArgumentMatchers.eq(flowTaskInstance.getId()),
                ArgumentMatchers.eq(FlowableElementType.USER_TASK), ArgumentMatchers.eq(FlowableElementType.class));
        verify(taskService).createTaskQuery().processInstanceId(ArgumentMatchers.anyString()).list();
        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(TaskInstanceCompletedEvent.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        flowTaskInstance.complete(true);
        Assert.fail();
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(target.getClass()).thenReturn(BaseRuntimeFlowableDelegate.class);
        when(taskService.createTaskQuery().processInstanceId(ArgumentMatchers.anyString()).list()).thenReturn(Collections.singletonList(mock(Task.class)));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target).cancel(ArgumentMatchers.anyBoolean());
        verify(taskService).complete(ArgumentMatchers.eq(1L), ArgumentMatchers.anyMapOf(String.class, Object.class));
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Object result = "test result";
        when(target.getId()).thenReturn(1L);
        when(target.get()).thenReturn(result);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertEquals(result, flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterruptedException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(taskService.createTaskQuery().processInstanceId(ArgumentMatchers.anyString()).list()).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(target.get()).thenThrow(new ExecutionException("test execution exception"));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(taskService.createTaskQuery().processInstanceId(ArgumentMatchers.anyString()).list()).thenReturn(Collections.emptyList());
        when(target.get(ArgumentMatchers.anyLong(), ArgumentMatchers.any(TimeUnit.class))).thenThrow(new TimeoutException("test timeout exception"));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
    }

    // Edge cases and exception handling tests

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWhenAutomaticExecutionStrategy() {
        flowTaskInstance.strategyConfig.setStrategy(FlowTaskExecutionStrategy.AUTO);
        flowTaskInstance.complete(false);
        Assert.fail();
    }

    @Test
    public void testMapToTaskEntityWhenNullInstance() {
        Assert.assertNull(FlowTaskInstance.mapToTaskEntity(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testCompleteWhenNotPending() {
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        flowTaskInstance.complete(false);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBindServiceTaskWhenInvalidDelegate() {
        BaseRuntimeFlowableDelegate<?> invalidDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(invalidDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        when(invalidDelegate.getClass()).thenThrow(new IllegalArgumentException("test invalid delegate"));
        flowTaskInstance.bindServiceTask(invalidDelegate);
        Assert.assertNull(flowTaskInstance.getTargetTaskHandle());
    }
}