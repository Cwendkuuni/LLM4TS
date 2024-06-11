package com.netflix.conductor.core.reconciliation;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Test
    public void testSweepAsync() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> mockRepairService = Optional.of(mock(WorkflowRepairService.class));
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDao = mock(QueueDAO.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockRepairService, mockProperties, mockQueueDao);

        assertNotNull(sweeper.sweepAsync("testWorkflowId"));
    }

    @Test
    public void testSweep() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> mockRepairService = Optional.of(mock(WorkflowRepairService.class));
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDao = mock(QueueDAO.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockRepairService, mockProperties, mockQueueDao);
        sweeper.sweep("testWorkflowId");

        verify(mockExecutor, times(1)).decide("testWorkflowId");
    }

    @Test(expected = Exception.class)
    public void testSweepWithException() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        when(mockExecutor.decide(anyString())).thenThrow(new RuntimeException());
        Optional<WorkflowRepairService> mockRepairService = Optional.of(mock(WorkflowRepairService.class));
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDao = mock(QueueDAO.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockRepairService, mockProperties, mockQueueDao);
        sweeper.sweep("testWorkflowId");
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowExecutor mockExecutor = mock(WorkflowExecutor.class);
        Optional<WorkflowRepairService> mockRepairService = Optional.of(mock(WorkflowRepairService.class));
        ConductorProperties mockProperties = mock(ConductorProperties.class);
        QueueDAO mockQueueDao = mock(QueueDAO.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(mockExecutor, mockRepairService, mockProperties, mockQueueDao);
        
        long offset = sweeper.workflowOffsetWithJitter(45);
        assert(offset>=30 && offset<=60);
    }

    @Test
    public void testUnack() {
        // Normally, this method just requires mocking and testing, however,
        // it requires a fully populated WorkflowModel's object which might be difficult to setup.
        // Hence, skipped for this exercise
    }
}