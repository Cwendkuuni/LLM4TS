package com.netflix.conductor.core.reconciliation;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.Async;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private final WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
    private final ConductorProperties properties = mock(ConductorProperties.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);

    @BeforeEach
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String id = "1";
        WorkflowModel model = mock(WorkflowModel.class);
        when(workflowExecutor.decide(id)).thenReturn(model);
        when(model.getStatus()).thenReturn(Status.COMPLETED);
        workflowSweeper.sweep(id);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, id);
    }

    @Test
    public void testSweepNotFound() {
        String id = "1";
        when(workflowExecutor.decide(id)).thenThrow(new NotFoundException("Workflow not found."));
        workflowSweeper.sweep(id);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, id);
    }

    @Test
    public void testSweepError() {
        String id = "1";
        when(workflowExecutor.decide(id)).thenThrow(new RuntimeException());
        workflowSweeper.sweep(id);
        verify(queueDAO, times(1)).setUnackTimeout(any(), any(), anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel model = mock(WorkflowModel.class);
        workflowSweeper.unack(model, 10000);
        verify(queueDAO, times(1)).setUnackTimeout(any(), any(), anyLong());
    }
}
