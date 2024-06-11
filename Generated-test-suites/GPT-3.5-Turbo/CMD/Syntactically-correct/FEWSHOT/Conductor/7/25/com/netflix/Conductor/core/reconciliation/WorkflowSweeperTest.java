package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";

        when(workflowExecutor.decide(workflowId)).thenReturn(null);

        workflowSweeper.sweep(workflowId);

        // Add more assertions here based on the behavior of the sweep method
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 60L;

        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(new TaskModel()));

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // Add more assertions here based on the behavior of the unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Add assertions for expected results based on the workflowOffsetWithJitter method's behavior
    }
}