import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.RuntimeService;
import org.flowable.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.common.constant.TaskType;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandleMock;

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private FlowTaskInstanceRepository flowTaskInstanceRepository;

    private OngoingStubbing<ProcessInstance> processInstanceStubbing;
    private Long processInstanceId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L, new ExecutionStrategyConfig(
                FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        when(nodeRepository.existsById(anyLong())).thenReturn(true);
        when(sequenceRepository.existsById(anyLong())).thenReturn(true);
        when(serviceTaskRepository.existsById(anyLong())).thenReturn(false);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.emptyList());
        when(delegateConvertor.map(eq(TaskType.SERVICE_TASK)))
                .thenReturn(BaseRuntimeFlowableDelegateTest.createMockServiceTaskDelegate());
        when(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(anyLong()))
                .thenReturn(flowTaskInstance.getFlowInstanceId());
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowNodeType.USER_TASK), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.emptyList());
        when(eventPublisher.publishEvent(any())).thenReturn(null);
        processInstanceStubbing = when(runtimeService.createProcessInstanceByKey("testKey"));
        processInstanceId = processInstanceStubbing.getMock().getId();
    }

    @After
    public void cleanup() {
        verifyNoMoreInteractions(nodeRepository, sequenceRepository, serviceTaskRepository, eventPublisher,
                taskService, runtimeService);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(serviceTaskRepository).save(argThat(s -> Objects.equals(s.getFlowInstanceId(), flowTaskInstance.getFlowInstanceId())));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> flowTaskInstances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstanceRepository.batchCreate(flowTaskInstances);
        verify(serviceTaskRepository, times(1)).batchCreate(argThat(s -> Objects.equals(s.size(), 1)));
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(serviceTaskRepository).update(argThat(s -> Objects.equals(s.getId(), flowTaskInstance.getId())));
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete();
        verify(serviceTaskRepository).deleteById(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        when(taskService.createTaskQuery().processInstanceId(eq(processInstanceId))
                .taskDefinitionKey(eq("testTaskDefinition"))).thenReturn(Collections.singletonList(createMockTask()));
        flowTaskInstance.bindServiceTask(BaseRuntimeFlowableDelegateTest.createMockServiceTaskDelegate());
        verify(flowTaskInstance).setStatus(eq(FlowNodeStatus.EXECUTING));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTask_illegalTarget() {
        flowTaskInstance.bindServiceTask(BaseRuntimeFlowableDelegateTest.createMockServiceTaskDelegate());
        verify(flowTaskInstance, never()).setStatus(FlowNodeStatus.EXECUTING);
        verify(flowTaskInstance, never()).setId(anyLong());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher).addEventListener(argThat(l -> equals(l, flowTaskInstance.getListener())));
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher).removeEventListener(argThat(l -> equals(l, flowTaskInstance.getListener())));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor).handleTaskExecution(eq(flowTaskInstance), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor).handleTaskExecution(eq(flowTaskInstance), argThat(v -> v.containsKey(ABORT_VARIABLE_NAME)
                && v.get(ABORT_VARIABLE_NAME) == true));
    }

    @Test
    public void testCancel() {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createMockTask()));
        flowTaskInstance.cancel(true);
        verify(targetTaskHandleMock).cancel(true);
    }

    @Test
    public void testIsDone() {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createMockTask()));
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createMockTask()));
        Object result = flowTaskInstance.getResult();
        verify(targetTaskHandleMock).get();
        assertEquals(result, targetTaskHandleMock.get());
    }

    @Test
    public void testGetResultTimeout() throws Exception {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createMockTask()));
        Object result = flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
        verify(targetTaskHandleMock).get(1L, TimeUnit.SECONDS);
        assertEquals(result, targetTaskHandleMock.get(1L, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeoutException() throws Exception {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createMockTask()));
        flowTaskInstance.getResult(2L, TimeUnit.SECONDS);
    }

    @Test
    public void testGetResultInterrupted() throws Exception {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenThrow(new InterruptedException());
        try {
            flowTaskInstance.getResult();
            fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // expected
        }
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws Exception {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenThrow(new ExecutionException("test Exception"));
        try {
            flowTaskInstance.getResult();
            fail("Expected ExecutionException");
        } catch (ExecutionException e) {
            assertEquals("test Exception", e.getCause().getMessage());
        }
    }

    private Task createMockTask() {
        Task task = mock(Task.class);
        when(task.getId()).thenReturn(1L);
        when(task.getName()).thenReturn("Test Task");
        when(task.getProcessInstanceId()).thenReturn(processInstanceId);
        return task;
    }

    private static ProcessInstance createMockProcessInstance() {
        ProcessInstance processInstance = mock(ProcessInstance.class);
        processInstanceStubbing.thenReturn(processInstance);
        return processInstance;
    }

}