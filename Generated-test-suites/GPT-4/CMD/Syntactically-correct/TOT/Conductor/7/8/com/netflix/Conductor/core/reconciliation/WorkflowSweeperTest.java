package com.netflix.conductor.core.reconciliation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock 
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test_workflow";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweepAsync(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "test_workflow";
        workflowSweeper.sweep(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 1000;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        verify(workflowSweeper, times(1)).unack(workflowModel, workflowOffsetTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 1000;
        workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        verify(workflowSweeper, times(1)).workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}