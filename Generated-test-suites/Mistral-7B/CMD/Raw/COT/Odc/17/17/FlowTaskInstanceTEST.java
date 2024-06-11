import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithEntity() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(any())).thenReturn(entity);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        Assert.assertNotNull(flowTaskInstance);
    }

    @Test
    public void testConstructorWithTaskType() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        Assert.assertNotNull(flowTaskInstance);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() throws Exception {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(any()))
                .thenAnswer(i -> Collections.singletonList(mock(ServiceTaskInstanceEntity.class)));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(flowTaskInstance.getId(), result.get(0).getId());
    }

    @Test
    public void testCreate() throws Exception {
        doAnswer(invocationOnMock -> {
            ServiceTaskInstanceEntity entity = invocationOnMock.getArgument(0);
            entity.setId(1L);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setUpdateTime(System.currentTimeMillis());
            return entity;
        }).when(serviceTaskRepository).save(any());

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.create();
        Assert.assertNotNull(flowTaskInstance.getId());
        Assert.assertNotNull(flowTaskInstance.getCreateTime());
        Assert.assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.update();
        Assert.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());

        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(flowTaskInstance.getId());
        entity.setStatus(FlowNodeStatus.EXECUTING);
        int affectRows = 1;
        when(serviceTaskRepository.update(any())).thenReturn(affectRows);
        flowTaskInstance.update();
        Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDelete() throws Exception {
        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testBindServiceTask() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertEquals(flowTaskInstance.getId(), target.getTargetTaskInstanceId());
        Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        Assert.assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        Assert.assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        Assert.assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() throws InterruptedException, ExecutionException {
        flowTaskInstance.complete(false);
        Assert.assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() throws InterruptedException, ExecutionException {
        flowTaskInstance.complete(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        doAnswer(invocationOnMock -> {
            BaseRuntimeFlowableDelegate<?> delegate = invocationOnMock.getArgument(0);
            delegate.cancel(true);
            return null;
        }).when(flowTaskInstance.getTargetTaskHandle()).cancel(anyBoolean());
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("test");
        flowTaskInstance.bindServiceTask(target);
        Assert.assertEquals("test", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.MINUTES)).thenReturn("test");
        flowTaskInstance.bindServiceTask(target);
        Assert.assertEquals("test", flowTaskInstance.getResult(1, TimeUnit.MINUTES));
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterrupted() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new ExecutionException("test"));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.MINUTES)).thenThrow(new TimeoutException("test"));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.MINUTES);
    }
}