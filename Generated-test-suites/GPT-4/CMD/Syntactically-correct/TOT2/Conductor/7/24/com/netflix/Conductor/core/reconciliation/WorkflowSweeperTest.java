package com.netflix.conductor.core.reconciliation;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {
    
    WorkflowSweeper workflowSweeper;
    WorkflowExecutor workflowExecutor;
    WorkflowRepairService workflowRepairService;
    ConductorProperties properties;
    QueueDAO queueDAO;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor,
                     Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test(expected = NullPointerException.class)
    public void sweepAsyncNullWorkflowId() {
        workflowSweeper.sweepAsync(null);
    }

    @Test(expected = NotFoundException.class)
    public void sweepNonExistentWorkflowId() {
        workflowSweeper.sweep("nonexistent_workflow_id");
    }

    @Test(expected = ExecutionException.class)
    public void sweepExceptionInWorkflowRepair() {
        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenThrow(ExecutionException.class);
        workflowSweeper.sweepAsync("workflow_id");
    }

    @Test
    public void unackNullWorkflowModel() {
        workflowSweeper.unack(workflowModel,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unackInvalidTimeout() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        workflowSweeper.unack(workflowModel, 0);
    }

    @Test
    public void workflowOffsetWithJitter() {
        long timeout = 5000;
        long timeoutWithJitter = workflowSweeper.workflowOffsetWithJitter(timeout);
        assertTrue(timeoutWithJitter <= timeout + timeout/3 && timeoutWithJitter >= timeout - timeout/3);
    }
}