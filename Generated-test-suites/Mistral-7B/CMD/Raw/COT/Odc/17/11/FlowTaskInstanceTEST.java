import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
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
    public void testConstructorWithEntity() throws Exception {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        Assert.assertNotNull(flowTaskInstance.getId());
        Assert.assertEquals(entity.getStatus(), flowTaskInstance.getStatus());
    }

    @Test
    public void testConstructorWithoutEntity() throws Exception {
        flowTaskInstance = new FlowTaskInstance(FlowNodeType.SERVICE_TASK, 1L, 1L, new ExecutionStrategyConfig(), true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);

        Assert.assertNotNull(flowTaskInstance.getId());
        Assert.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testGetCoreFlowableElementType() throws Exception {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() throws Exception {
        List<FlowTaskInstance> instances = new ArrayList<>();
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.batchCreate(Collections.singletonList(entity))).thenReturn(Collections.singletonList(entity));

        instances.add(new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository));

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(entity.getId(), result.get(0).getId());
    }

    @Test
    public void testCreate() throws Exception {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        Assert.assertEquals(entity.getId(), flowTaskInstance.getId());
        Assert.assertEquals(entity.getCreateTime(), flowTaskInstance.getCreateTime());
        Assert.assertEquals(entity.getUpdateTime(), flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() throws Exception {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.update(entity)).thenReturn(1);

        flowTaskInstance.update();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteWithInvalidId() throws Exception {
        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testDeleteWithValidId() throws Exception {
        doReturn(1).when(serviceTaskRepository).deleteById(anyLong());

        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testBindServiceTask() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertEquals(flowTaskInstance, flowTaskInstance.getTargetTaskHandle().getTaskInstance());
    }

    @Test
    public void testIsPresentOnThisMachine() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);

        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() throws Exception {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.dealloc();
        Assert.assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() throws Exception {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.alloc();
        Assert.assertNotNull(flowTaskInstance.getListener());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfirmExecuteWithoutTask() throws Exception {
        flowTaskInstance.confirmExecute();
    }

    @Test
    public void testConfirmExecuteWithTask() throws Exception {
        Task task = mock(Task.class);
        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(task));

        flowTaskInstance.complete(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortWithoutTask() throws Exception {
        flowTaskInstance.abort();
    }

    @Test
    public void testAbortWithTask() throws Exception {
        Task task = mock(Task.class);
        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(task));

        flowTaskInstance.complete(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithoutTask() throws Exception {
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testCancelWithTask() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);
        when(target.cancel(anyBoolean())).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.cancel(false);
    }

    @Test
    public void testIsDoneWithTask() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);

        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        when(target.get()).thenThrow(new ExecutionException("test"));

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult();
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterruptedWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("test"));

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionExceptionWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new ExecutionException("test"));

        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetTargetDelegateClass() throws Exception {
        Class<BaseRuntimeFlowableDelegate<String>> expectedClass = BaseRuntimeFlowableDelegate.class;
        Class<BaseRuntimeFlowableDelegate<String>> actualClass = flowTaskInstance.getTargetDelegateClass();

        Assert.assertEquals(expectedClass, actualClass);
    }
}