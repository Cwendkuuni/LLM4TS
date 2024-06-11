import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskService;

    @Before
    public void setup() {
        taskService = mock(TaskService.class);
        flowTaskInstance = new FlowTaskInstance(TaskType.SOME_TYPE, 1L, 1L, new ExecutionStrategyConfig(), true, false, null, null, null, taskService, null, null, null, null);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        FlowTaskInstance instance1 = new FlowTaskInstance(TaskType.SOME_TYPE, 1L, 1L, new ExecutionStrategyConfig(), true, false, null, null, null, taskService, null, null, null, null);
        FlowTaskInstance instance2 = new FlowTaskInstance(TaskType.SOME_TYPE, 1L, 1L, new ExecutionStrategyConfig(), true, false, null, null, null, taskService, null, null, null, null);
        instances.add(instance1);
        instances.add(instance2);

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, null);
        assertEquals(instances.size(), createdInstances.size());
        assertNotNull(createdInstances.get(0).getId());
        assertNotNull(createdInstances.get(1).getId());
        assertNotNull(createdInstances.get(0).getCreateTime());
        assertNotNull(createdInstances.get(1).getCreateTime());
        assertNotNull(createdInstances.get(0).getUpdateTime());
        assertNotNull(createdInstances.get(1).getUpdateTime());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);

        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.setId(1L);
        flowTaskInstance.doDelete(flowTaskInstance.getId());
        assertNull(flowTaskInstance.getId());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTask.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(targetTask);
        assertEquals(targetTask, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);

        flowTaskInstance.cancel(true);
        verify(targetTask, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);

        when(targetTask.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);

        when(targetTask.get()).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);

        when(targetTask.get(1L, TimeUnit.SECONDS)).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }
}