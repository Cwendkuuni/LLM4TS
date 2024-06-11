package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.workflow.Workflow;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;

import static org.junit.Assert.*;

public class WorkflowSweeperTest {
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, null, new ConductorProperties(), queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflow";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflow";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel(); // have to populate this model
        long workflowOffsetTimeout = 1000;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 3000;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 2000 && result <= 4000);
    }
}
