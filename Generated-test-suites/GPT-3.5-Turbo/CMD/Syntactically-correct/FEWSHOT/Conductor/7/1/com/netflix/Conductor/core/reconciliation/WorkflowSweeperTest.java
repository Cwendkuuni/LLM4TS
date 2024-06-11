package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper sweeper;
    private WorkflowExecutor workflowExecutorMock;
    private WorkflowRepairService workflowRepairServiceMock;
    private QueueDAO queueDAOMock;
    private ConductorProperties propertiesMock;

    @Before
    public void setup() {
        workflowExecutorMock = mock(WorkflowExecutor.class);
        workflowRepairServiceMock = mock(WorkflowRepairService.class);
        queueDAOMock = mock(QueueDAO.class);
        propertiesMock = mock(ConductorProperties.class);

        sweeper = new WorkflowSweeper(workflowExecutorMock, Optional.of(workflowRepairServiceMock), propertiesMock, queueDAOMock);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = sweeper.sweepAsync("workflow123");
        assertNotNull(future);
        verify(workflowExecutorMock, times(1)).decide("workflow123");
    }

    @Test
    public void testSweep() {
        sweeper.sweep("workflow456");
        verify(workflowExecutorMock, times(1)).decide("workflow456");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflow789");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setResponseTimeoutSeconds(10);
        workflowModel.getTasks().add(taskModel);

        sweeper.unack(workflowModel, 20);
        verify(queueDAOMock, times(1)).setUnackTimeout(eq("DECIDER_QUEUE"), eq("workflow789"), eq(11 * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = sweeper.workflowOffsetWithJitter(45);
        assertTrue(result >= 30 && result <= 60);
    }
}