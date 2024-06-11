import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.task.api.Task;
import org.flowable.task.api.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceRepository mockServiceTaskRepository;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private EventPublisher mockEventPublisher;
    @Mock
    private RuntimeDelegateMapper mockDelegateConvertor;
    @Mock
    private Task mockTask;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(TaskType.SOME_TASK_TYPE, 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 5),
                true, false, mockDelegateConvertor, mock(FlowableAdaptor.class),
                mockEventPublisher, mockTaskService, mock(NodeInstanceEntityRepository.class),
                mock(SequenceInstanceRepository.class), mockServiceTaskRepository);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);

        when(mockServiceTaskRepository.batchCreate(anyList())).thenReturn(new ArrayList<>());

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, mockServiceTaskRepository);

        verify(mockServiceTaskRepository, times(1)).batchCreate(anyList());
        // Assert any additional verification if needed
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        verify(mockServiceTaskRepository, times(1)).save(any());
        // Assert any additional verification if needed
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();

        verify(mockServiceTaskRepository, times(1)).update(any());
        // Assert any additional verification if needed
    }

    @Test
    public void testComplete() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);

        when(mockTaskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(tasks);

        flowTaskInstance.complete(false);

        verify(mockTaskService, times(1)).createTaskQuery().processInstanceId(anyString());
        // Assert any additional verification if needed
    }

    @Test
    public void testAbort() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);

        when(mockTaskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(tasks);

        flowTaskInstance.complete(true);

        verify(mockTaskService, times(1)).createTaskQuery().processInstanceId(anyString());
        // Assert any additional verification if needed
    }

    @Test
    public void testIsPresentOnThisMachine() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

        boolean result = flowTaskInstance.isPresentOnThisMachine();

        // Assert the result
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        when(mockTargetTaskHandle.isDone()).thenReturn(true);

        boolean result = flowTaskInstance.isDone();

        // Assert the result
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        Object expectedResult = new Object();

        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        when(mockTargetTaskHandle.get()).thenReturn(expectedResult);

        Object result = flowTaskInstance.getResult();

        // Assert the result
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        Object expectedResult = new Object();

        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        when(mockTargetTaskHandle.get(10, TimeUnit.SECONDS)).thenReturn(expectedResult);

        Object result = flowTaskInstance.getResult(10, TimeUnit.SECONDS);

        // Assert the result
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        flowTaskInstance.cancel(false);

        verify(mockTargetTaskHandle, times(1)).cancel(false);
        // Assert any additional verification if needed
    }

}