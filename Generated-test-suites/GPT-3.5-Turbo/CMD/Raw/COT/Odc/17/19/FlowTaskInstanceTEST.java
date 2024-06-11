
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableAdaptor;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class FlowTaskInstanceTest {
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;

    @Before
    public void setUp() {
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
    }

    @Test
    public void testCompleteAborted() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.setStatus(FlowNodeStatus.PENDING);
        instance.setStrategyConfig(new ExecutionStrategyConfig());
        
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
            .thenReturn(new ArrayList<>());
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(java.util.Optional.of("pid"));
        when(taskService.createTaskQuery().processInstanceId("pid").list()).thenReturn(tasks);

        instance.complete(true);

        verify(taskService).complete(anyString(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenTaskNotOnMachine() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.cancel(true);
    }

    @Test
    public void testIsPresentOnThisMachineReturnsTrueWhenTargetTaskHandleNotNull() {
        FlowTaskInstance instance = new FlowTaskInstance();
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        instance.bindServiceTask(targetTaskHandle);

        assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testIsPresentOnThisMachineReturnsFalseWhenTargetTaskHandleNull() {
        FlowTaskInstance instance = new FlowTaskInstance();
        
        assertFalse(instance.isPresentOnThisMachine());
    }

    @Test
    public void testDeallocDoesNotAddListenerTwice() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.alloc();
        instance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testGetTargetDelegateClassReturnsDelegateClass() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.setTaskType(TaskType.TASK_A);
        
        assertEquals(RuntimeDelegateMapper.map(TaskType.TASK_A).getClass(), instance.getTargetDelegateClass());
    }

    @Test
    public void testMapToTaskEntityReturnsServiceTaskInstanceEntity() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.setId(1L);
        instance.setOrganizationId(10L);
        instance.setStatus(FlowNodeStatus.PENDING);
        instance.setFlowInstanceId(100L);
        instance.setStartEndpoint(true);
        instance.setEndEndpoint(false);
        instance.setStrategyConfig(new ExecutionStrategyConfig());
        instance.setTaskType(TaskType.TASK_B);
        instance.setTargetTaskId(20L);

        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(instance);

        assertEquals(10L, entity.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(100L, entity.getFlowInstanceId());
        assertTrue(entity.isStartEndpoint());
        assertFalse(entity.isEndEndpoint());
        assertEquals(FlowTaskExecutionStrategy.AUTO, entity.getStrategy());
        assertEquals(TaskType.TASK_B, entity.getTaskType());
        assertEquals(20L, entity.getTargetTaskId());
    }

    // Additional test cases for edge cases and exceptions can be added here
}

