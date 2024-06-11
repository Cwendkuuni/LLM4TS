import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class FlowTaskInstanceTest {

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(nodeRepository.findById(anyLong())).thenAnswer(new Answer<Optional<FlowableElement>>() {
            @Override
            public Optional<FlowableElement> answer(final InvocationOnMock invocation) throws Throwable {
                long id = invocation.getArgument(0);
                FlowableElement element = new FlowableElement();
                element.setId(id);
                element.setName("userTask");
                element.setNodeType(FlowNodeType.USER_TASK);
                return Optional.of(element);
            }
        });
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowNodeType.USER_TASK),
                eq(FlowableElementType.USER_TASK))).thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("pid"));
        when(taskService.createTaskQuery().processInstanceId(eq("pid"))).thenReturn(mock(List.class));
        when(taskService.createTaskQuery().processInstanceId(eq("pid"))).thenReturn(mock(TaskInfo.class));
        when(taskService.complete(anyLong(), anyMap())).thenReturnVoid();
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(new Answer<ServiceTaskInstanceEntity>() {
            @Override
            public ServiceTaskInstanceEntity answer(final InvocationOnMock invocation) throws Throwable {
                ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
                entity.setId(RandomUtils.nextLong());
                entity.setCreateTime(System.currentTimeMillis());
                entity.setUpdateTime(System.currentTimeMillis());
                return entity;
            }
        });
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(0);
    }

    @After
    public void teardown() {
        flowTaskInstance = null;
    }

    @Test
    public void testCreateFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.PROCESS_TASK, 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, true, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        assertThat(flowTaskInstance.getId(), is(notNullValue()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFlowTaskInstanceAlreadyExists() throws Exception {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.PROCESS_TASK, 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, true, delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        flowTaskInstance.create();
    }

    @Test
    public void testBindServiceTask() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertThat(flowTaskInstance.isPresentOnThisMachine(), is(true));
    }

    @Test
    public void testDealloc() throws Exception {
        flowTaskInstance.dealloc();
        assertThat(flowTaskInstance.listener, is(nullValue()));
    }

    @Test
    public void testAlloc() throws Exception {
        flowTaskInstance.alloc();
        assertThat(flowTaskInstance.listener, is(notNullValue()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskDifferentInstanceId() throws Exception {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(RandomUtils.nextLong());
        flowTaskInstance.bindServiceTask(target);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfirmExecuteWithRunningTask() throws Exception {
        flowTaskInstance.confirmExecute();
    }

    @Test
    public void testConfirmExecuteWithPendingTask() throws Exception {
        flowTaskInstance.confirmExecute();
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.complete(taskInfo.getId(), anyMap())).thenReturnVoid();
        flowTaskInstance.complete(false);
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.COMPLETED));
    }

    @Test(expected = IllegalStateException.class)
    public void testConfirmExecuteWithAbortedTask() throws Exception {
        flowTaskInstance.confirmExecute();
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskInfo.getName()).thenReturn("userTask");
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.complete(taskInfo.getId(), anyMap())).thenReturnVoid();
        flowTaskInstance.complete(true);
        flowTaskInstance.confirmExecute();
    }

    @Test
    public void testAbortWithRunningTask() throws Exception {
        flowTaskInstance.abort();
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskInfo.getName()).thenReturn("userTask");
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.complete(taskInfo.getId(), anyMap())).thenReturnVoid();
        flowTaskInstance.complete(false);
        flowTaskInstance.abort();
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.ABORTED));
    }

    @Test
    public void testAbortWithCompletedTask() throws Exception {
        flowTaskInstance.abort();
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskInfo.getName()).thenReturn("userTask");
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.complete(taskInfo.getId(), anyMap())).thenReturnVoid();
        flowTaskInstance.complete(false);
        flowTaskInstance.abort();
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.ABORTED));
    }

    @Test
    public void testCancelWithRunningTask() throws Exception {
        flowTaskInstance.cancel(true);
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.cancel(taskInfo.getId(), true)).thenReturn(true);
        flowTaskInstance.complete(false);
        flowTaskInstance.cancel(true);
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.CANCELED));
    }

    @Test
    public void testCancelWithCompletedTask() throws Exception {
        flowTaskInstance.cancel(true);
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(1L);
        when(taskService.createTaskQuery().processInstanceId("pid")).thenReturn(taskInfo);
        when(taskService.cancel(taskInfo.getId(), true)).thenReturn(false);
        flowTaskInstance.complete(false);
        flowTaskInstance.cancel(true);
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.COMPLETED));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithNonExistentTask() throws Exception {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testGetResultWithRunningTask() throws Exception {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        Object result = flowTaskInstance.getResult();
        assertThat(result, is(nullValue()));
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterruptedTask() throws Exception {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        Thread.currentThread().interrupt();
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetResultWithCompletedTask() throws Exception {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.setStatus(FlowNodeStatus.COMPLETED);
        Object result = flowTaskInstance.getResult();
        assertThat(result, is(notNullValue()));
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithFailedTask() throws Exception {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.setStatus(FlowNodeStatus.FAILED);
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeOutTask() throws Exception {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        Thread.sleep(1000);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testDeleteTaskInstance() throws Exception {
        flowTaskInstance.delete();
        assertThat(flowTaskInstance.getStatus(), is(FlowNodeStatus.DELETED));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteNonExistentTaskInstance() throws Exception {
        flowTaskInstance.delete();
    }
}