import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

/**
 * Test class for FlowTaskInstance
 */
public class FlowTaskInstanceTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private ServiceTaskStartedListener listener;
    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;
    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(new Answer<ServiceTaskInstanceEntity>() {
            @Override
            public ServiceTaskInstanceEntity answer(ServiceTaskInstanceEntity entity) throws Exception {
                entity.setId(1L);
                entity.setCreateTime(System.currentTimeMillis());
                entity.setUpdateTime(System.currentTimeMillis());
                return entity;
            }
        });
        when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(mock(List.class));
        doAnswer(new Answer<List<Task>>() {
            @Override
            public List<Task> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                List<Task> tasks = (List<Task>) args[0];
                List<Task> mockList = mock(List.class);
                when(mockList.stream()).thenReturn(mockList.stream());
                when(mockList.filter(any(Task.class))).thenAnswer(i -> tasks.get(0));
                return mockList;
            }
        }).when(taskService).createTaskQuery().processInstanceId(anyString());
        doThrow(new UnsupportedOperationException("Task isn't on this machine"))
                .when(flowableAdaptor)
                .getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK), any());
        when(eventPublisher.removeEventListener(any())).thenAnswer(i -> {});
        when(eventPublisher.addEventListener(any())).thenAnswer(i -> {});
        when(delegateConvertor.map(any(TaskType.class)))
                .thenReturn(mock(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = new FlowTaskInstance(
                new TaskType("testType"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        instance.create();
        verify(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateWithExistingInstance() {
        FlowTaskInstance instance = new FlowTaskInstance(1L, new TaskType("testType"), 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, true, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        instance.create();
        verify(serviceTaskRepository, times(2)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(
                new FlowTaskInstance(new TaskType("testType"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                        true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                        sequenceRepository, serviceTaskRepository));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        verify(serviceTaskRepository, times(1)).batchCreate(anyListOf(ServiceTaskInstanceEntity.class));
        assertEquals(instances.size(), result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBatchCreateWithEmptyList() {
        List<FlowTaskInstance> instances = Collections.emptyList();
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = new FlowTaskInstance(1L, new TaskType("testType"), 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        instance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullInstance() {
        FlowTaskInstance instance = null;
        instance.update();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullRepository() {
        FlowTaskInstance instance = new FlowTaskInstance(1L, new TaskType("testType"), 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
                true, true, delegateConvertor, flowableAdaptor, eventPublisher, null, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        instance.update();
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskWithNullTarget() {
        flowTaskInstance.bindServiceTask(null);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachineWithNullTarget() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        EventPublisher eventPublisherSpy = spy(eventPublisher);
        FlowTaskInstance flowTaskInstanceSpy = spy(flowTaskInstance);
        doCallRealMethod().when(flowTaskInstanceSpy).dealloc();
        flowTaskInstanceSpy.dealloc();
        verify(eventPublisherSpy).removeEventListener(eq(flowTaskInstanceSpy.listener));
    }

    @Test
    public void testAlloc() {
        EventPublisher eventPublisherSpy = spy(eventPublisher);
        FlowTaskInstance flowTaskInstanceSpy = spy(flowTaskInstance);
        doCallRealMethod().when(flowTaskInstanceSpy).alloc();
        flowTaskInstanceSpy.alloc();
        verify(eventPublisherSpy).addEventListener(eq(flowTaskInstanceSpy.listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor).getFlowableElementByType(eq(flowTaskInstance.getId()), eq(FlowableElementType.USER_TASK), any());
        verify(taskService).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor).getFlowableElementByType(eq(flowTaskInstance.getId()), eq(FlowableElementType.USER_TASK), any());
        verify(taskService).createTaskQuery().processInstanceId(anyString()).list();
        verify(taskService).complete(anyLong(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithNoTaskHandle() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testCancelWithInterrupted() {
        when(targetTaskHandle.isInterrupted()).thenReturn(true);
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle).cancel(true);
    }

    @Test
    public void testCancelWithoutInterrupted() {
        when(targetTaskHandle.isInterrupted()).thenReturn(false);
        flowTaskInstance.cancel(false);
        verify(targetTaskHandle).cancel(false);
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeout() {
        when(targetTaskHandle.isDone()).thenReturn(false);
        flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testGetResult() throws Exception {
        when(targetTaskHandle.isDone()).thenReturn(true);
        Object result = flowTaskInstance.getResult();
        verifyNoMoreInteractions(targetTaskHandle);
        assertEquals(result, flowTaskInstance.targetTaskHandle.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterrupted() throws InterruptedException {
        when(targetTaskHandle.isDone()).thenReturn(false);
        flowTaskInstance.getResult();
        thread().interrupt();
        verify(targetTaskHandle).cancel(true);
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithExecutionException() throws InterruptedException, ExecutionException {
        when(targetTaskHandle.isDone()).thenReturn(true);
        when(targetTaskHandle.get()).thenThrow(new ExecutionException("Test exception"));
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

}