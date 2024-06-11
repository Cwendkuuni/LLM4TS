package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private QueueDAO queueDAO;

    @Test
    public void testWorkflowSweeperInitialization() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        verify(workflowSweeper, times(1)).WorkflowSweeper();
    }

    @Test
    public void testSweepAsync() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        workflowSweeper.sweepAsync("testId");
        verify(workflowSweeper, times(1)).sweepAsync("testId");
    }

    @Test
    public void testSweep() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        doNothing().when(workflowExecutor).decide("testId");
        workflowSweeper.sweep("testId");
        verify(workflowSweeper, times(1)).sweep("testId");
    }

    @Test
    public void testUnack() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        workflowSweeper.unack(mock(WorkflowModel.class), 100);
        verify(workflowSweeper, times(1)).unack(mock(WorkflowModel.class), 100);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        workflowSweeper.workflowOffsetWithJitter(45);
        verify(workflowSweeper, times(1)).workflowOffsetWithJitter(45);
    }
}
