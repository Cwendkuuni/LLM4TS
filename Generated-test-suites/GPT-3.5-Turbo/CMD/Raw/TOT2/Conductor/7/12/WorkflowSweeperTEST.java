import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test_workflow_id";
        
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        assertNotNull(future);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Exception occurred while running sweepAsync: " + e.getMessage());
        }
    }

    @Test
    public void testSweep() {
        String workflowId = "test_workflow_id";

        WorkflowModel mockWorkflow = new WorkflowModel();
        mockWorkflow.setWorkflowId(workflowId);

        WorkflowContext workflowContext = new WorkflowContext("test_app_id");
        WorkflowContext.set(workflowContext);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(mockWorkflow);
        when(queueDAO.remove(anyString(), eq(workflowId))).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "test_workflow_id";

        WorkflowContext workflowContext = new WorkflowContext("test_app_id");
        WorkflowContext.set(workflowContext);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testUnackTaskModelInProgressWithWaitTaskType() {
        WorkflowModel mockWorkflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10); // For testing purposes
        mockWorkflow.getTasks().add(taskModel);

        workflowSweeper.unack(mockWorkflow, 45L); // Using a fixed workflowOffsetTimeout value for testing

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(11L * 1000));
    }

    @Test
    public void testUnackTaskModelInProgressWithNonWaitTaskType() {
        WorkflowModel mockWorkflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SIMPLE);
        taskModel.setResponseTimeoutSeconds(20); // For testing purposes
        mockWorkflow.getTasks().add(taskModel);

        workflowSweeper.unack(mockWorkflow, 45L); // Using a fixed workflowOffsetTimeout value for testing

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(21L * 1000));
    }

    @Test
    public void testUnackTaskModelScheduled() {
        WorkflowModel mockWorkflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        mockWorkflow.getTasks().add(taskModel);

        workflowSweeper.unack(mockWorkflow, 45L); // Using a fixed workflowOffsetTimeout value for testing

        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), eq(46L * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45L); // Using a fixed workflowOffsetTimeout value for testing

        assertTrue(result >= 30 && result <= 60); // Jitter value should be between [30-60] seconds
    }
}