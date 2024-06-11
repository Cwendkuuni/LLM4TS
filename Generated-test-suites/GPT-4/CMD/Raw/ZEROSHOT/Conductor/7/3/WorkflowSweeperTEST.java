
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

import java.util.Optional;
import java.util.concurrent.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void sweepAsyncReturnsCompletedFuture() {
        String workflowId = "test-workflow";
        CompletableFuture future = workflowSweeper.sweepAsync(workflowId);
        assertTrue("Validating CompletableFuture is done", future.isDone());
    }

    @Test
    public void sweepCallsVerifyAndRepairWorkflowTasksAndDecideMethod() {
        WorkflowModel mockWorkflow = mock(WorkflowModel.class);
        when(mockWorkflow.getStatus()).thenReturn(WorkflowModel.Status.IN_PROGRESS);
        when(workflowExecutor.decide(anyString())).thenReturn(mockWorkflow);
        workflowSweeper.sweep(anyString());
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(anyString());
        verify(workflowExecutor).decide(anyString());
    }

    @Test
    public void workflowOffsetWithJitter_ReturnsNumberInRange() {
        long offset = 40L;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(offset);
        assertTrue("workflowOffsetWithJitter should be >= 80% of original", workflowOffsetWithJitter >= 0.8*offset);
        assertTrue("workflowOffsetWithJitter should be <= 120% of original", workflowOffsetWithJitter <= 1.2*offset);
    }

    @Test
    public void unack_SetUnackTimeoutOnEachTask() {
        WorkflowModel mockWorkflow = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        TaskModel taskModel2 = new TaskModel();
        mockWorkflow.setWorkflowId("1");
        mockWorkflow.addTask(taskModel1);
        mockWorkflow.addTask(taskModel2);

        workflowSweeper.unack(mockWorkflow, 10);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }
}
