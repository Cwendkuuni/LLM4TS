import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper sweeper;

    @Before
    public void setUp() {
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException {
        String workflowId = "123";
        CompletableFuture<Void> future = sweeper.sweepAsync(workflowId);
        future.get();
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "456";
        WorkflowModel workflowModel = mock(WorkflowModel.class);

        WorkflowContext workflowContext = new WorkflowContext("testAppId");
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 60;

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(30);

        workflowModel.getTasks().add(taskModel);

        ReflectionTestUtils.invokeMethod(sweeper, "unack", workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long result = ReflectionTestUtils.invokeMethod(sweeper, "workflowOffsetWithJitter", workflowOffsetTimeout);

        assertTrue(result >= 30 && result <= 60);
    }

}