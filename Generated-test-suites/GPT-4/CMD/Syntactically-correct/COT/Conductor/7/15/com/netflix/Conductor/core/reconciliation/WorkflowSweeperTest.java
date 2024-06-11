package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private Optional<WorkflowRepairService> workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = Optional.of(mock(WorkflowRepairService.class));
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "1";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        result.join();
        Mockito.verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "1";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, atLeastOnce()).decide(workflowId);
    }

    @Test(expected = NullPointerException.class)
    public void testSweepWithNullWorkflowId() {
        workflowSweeper.sweep(null);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepWithNonExistentWorkflowId() {
        String workflowId = "100";
        when(workflowExecutor.decide(workflowId)).thenThrow(NotFoundException.class);
        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 10L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(queueDAO, atLeastOnce()).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(result >= (2*workflowOffsetTimeout/3) && result <= (4*workflowOffsetTimeout/3));
    }
}
