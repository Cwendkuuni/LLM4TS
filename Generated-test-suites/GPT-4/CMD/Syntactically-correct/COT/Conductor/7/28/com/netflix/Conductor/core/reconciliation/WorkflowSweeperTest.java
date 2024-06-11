package com.netflix.conductor.core.reconciliation;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.Future;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    private WorkflowSweeper sweeper;
    private WorkflowExecutor executor;
    private WorkflowRepairService repairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        executor = mock(WorkflowExecutor.class);
        repairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        sweeper = new WorkflowSweeper(executor, Optional.of(repairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        Future<Void> future = sweeper.sweepAsync("WorkflowId");
        future.get();
        verify(sweeper, times(1)).sweep(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFoundException() {
        doThrow(new NotFoundException("Workflow not found")).when(executor).decide(anyString());
        sweeper.sweep("WorkflowId");
    }

    @Test(expected = RuntimeException.class)
    public void testSweepOtherException() {
        doThrow(new RuntimeException("Error running sweep")).when(executor).decide(anyString());
        sweeper.sweep("WorkflowId");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        sweeper.unack(workflowModel, 5L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long offset = sweeper.workflowOffsetWithJitter(45L);
        assertTrue(offset >= 30L && offset <= 60L);
    }

    @Test
    public void testWorkflowOffsetWithJitterNegative() {
        long offset = sweeper.workflowOffsetWithJitter(-45L);
        assertTrue(offset <= 30L && offset >= -60L);
    }

    @Test
    public void testSweepNullWorkflowId() {
          sweeper.sweep(null);
          verify(executor, times(1)).decide(null);
    }

    @Test
    public void testSweepEmptyWorkflowId() {
          sweeper.sweep("");
          verify(executor, times(1)).decide("");
    }
}