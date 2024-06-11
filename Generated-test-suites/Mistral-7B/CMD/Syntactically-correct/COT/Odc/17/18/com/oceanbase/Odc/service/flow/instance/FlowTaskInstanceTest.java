import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

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

    // Test for constructor with entity
    @Test
    public void testFlowTaskInstanceConstructorWithEntity() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        Assert.assertEquals(entity.getId(), flowTaskInstance.getId());
        Assert.assertEquals(entity.getOrganizationId(), flowTaskInstance.getOrganizationId());
        Assert.assertEquals(entity.getFlowInstanceId(), flowTaskInstance.getFlowInstanceId());
        Assert.assertEquals(entity.getStatus(), flowTaskInstance.getStatus());
        Assert.assertEquals(entity.isStartEndpoint(), flowTaskInstance.isStartEndpoint());
        Assert.assertEquals(entity.isEndEndpoint(), flowTaskInstance.isEndEndpoint());
        Assert.assertEquals(entity.getTaskType(), flowTaskInstance.getTaskType());
        Assert.assertNull(flowTaskInstance.getTargetTaskId());
        Assert.assertNull(flowTaskInstance.getStrategyConfig());
        Assert.assertNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertNull(flowTaskInstance.getListener());
    }

    // Test for constructor without entity
    @Test
    public void testFlowTaskInstanceConstructorWithoutEntity() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SOME_TASK_TYPE, 1L, 2L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5000L, 10000L), false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        Assert.assertEquals(1L, flowTaskInstance.getOrganizationId());
        Assert.assertEquals(2L, flowTaskInstance.getFlowInstanceId());
        Assert.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        Assert.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        Assert.assertEquals(TaskType.SOME_TASK_TYPE, flowTaskInstance.getTaskType());
        Assert.assertNull(flowTaskInstance.getId());
        Assert.assertNotNull(flowTaskInstance.getStrategyConfig());
        Assert.assertNull(flowTaskInstance.getTargetTaskId());
        Assert.assertNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertNull(flowTaskInstance.getListener());
    }

    // Test for getCoreFlowableElementType method
    @Test
    public void testGetCoreFlowableElementType() {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    // Test for batchCreate method
    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(anyList()))
                .thenReturn(Collections.singletonList(new ServiceTaskInstanceEntity()));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Assert.assertEquals(instances, result);
        verify(serviceTaskRepository, times(1)).batchCreate(anyList());
    }

    // Test for create method
    @Test
    public void testCreate() throws InterruptedException, ExecutionException {
        doNothing().when(serviceTaskRepository).save(any());
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    // Test for update method
    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = spy(this.flowTaskInstance);
        doAnswer(invocation -> {
            FlowTaskInstance instance = invocation.getArgument(0);
            instance.setId(1L);
            instance.setStatus(FlowNodeStatus.EXECUTING);
            return 1;
        }).when(serviceTaskRepository).update(any());
        int affectRows = flowTaskInstance.update();
        Assert.assertEquals(1, affectRows);
        verify(serviceTaskRepository, times(1)).update(any());
    }

    // Test for delete method
    @Test
    public void testDelete() {
        doNothing().when(serviceTaskRepository).deleteById(anyLong());
        flowTaskInstance.delete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(anyLong());
    }

    // Test for bindServiceTask method
    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
        Assert.assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    }

    // Test for isPresentOnThisMachine method
    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    // Test for dealloc method
    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(listener);
        Assert.assertNull(flowTaskInstance.getListener());
    }

    // Test for alloc method
    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(listener);
        Assert.assertNotNull(flowTaskInstance.getListener());
    }

    // Test for confirmExecute method
    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    // Test for abort method
    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        Map<String, Object> variables = new HashMap<>();
        variables.put(FlowTaskInstance.ABORT_VARIABLE_NAME, true);
        verify(taskService, times(1)).complete(anyLong(), variables);
    }

    // Test for cancel method
    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(1L);
        when(target.cancel(anyBoolean())).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        verify(target, times(1)).cancel(true);
    }

    // Test for isDone method
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isDone());
    }

    // Test for getResult method
    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");
        flowTaskInstance.bindServiceTask(target);
        String result = (String) flowTaskInstance.getResult();
        Assert.assertEquals("result", result);
    }

    // Test for getResult method with timeout
    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("timeout"));
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.getResult(500L, TimeUnit.MILLISECONDS);
    }

    // Test for getTargetDelegateClass method
    @Test
    public void testGetTargetDelegateClass() {
        Class<?> expectedClass = BaseRuntimeFlowableDelegate.class;
        Assert.assertEquals(expectedClass, flowTaskInstance.getTargetDelegateClass());
    }
}