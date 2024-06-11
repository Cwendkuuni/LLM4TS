package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService repairServiceMock;
    private ConductorProperties conductorPropertiesMock;
    private QueueDAO queueDAOMock;

    private WorkflowSweeper sweeper;

    @Before
    public void setUp() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        repairServiceMock = mock(WorkflowRepairService.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        queueDAOMock = mock(QueueDAO.class);

        sweeper = new WorkflowSweeper(
                workflowExecutorMock,
                Optional.of(repairServiceMock),
                conductorPropertiesMock,
                queueDAOMock);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "workflowId1";

        CompletableFuture<Void> future = sweeper.sweepAsync(workflowId);
        future.get();

        // Verify that 'sweep' was called once
        verify(sweeper, times(1)).sweep(anyString());
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "workflowId1";

        // When 'decide' on WorkflowExecutor is called, throw an Exception
        Mockito.when(workflowExecutorMock.decide(workflowId))
                .thenThrow(new RuntimeException("Test exception"));

        sweeper.sweep(workflowId);

        // Verify that 'error' on the Monitor was called once
        verify(Monitors, times(1)).error("WorkflowSweeper", "sweep");
    }
}
