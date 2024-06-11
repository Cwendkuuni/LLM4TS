package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor mockWorkflowExecutor;
    private WorkflowRepairService mockWorkflowRepairService;
    private ConductorProperties mockConductorProperties;
    private QueueDAO mockQueueDAO;

    @Before
    public void setUp() {
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockWorkflowRepairService = mock(WorkflowRepairService.class);
        mockConductorProperties = mock(ConductorProperties.class);
        mockQueueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(
            mockWorkflowExecutor,
            Optional.of(mockWorkflowRepairService),
            mockConductorProperties,
            mockQueueDAO
        );
    }

    @Test
    public void sweepAsync_shouldCallSweepAndReturnCompletedFuture() {
        String workflowId = "workflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        assertEquals(result.isDone(), true);
    }

    @Test(expected = NotFoundException.class)
    public void sweep_whenWorkflowNotFound_shouldThrowException() {
        String workflowId = "workflowId";
        doThrow(NotFoundException.class).when(mockWorkflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
    }

    // Continue with additional tests for edge cases and exceptions within the sweep method
}
