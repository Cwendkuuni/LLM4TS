package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {
    
    WorkflowSweeper sweeper;
    ConductorProperties properties;
    WorkflowExecutor workflowExecutor;
    QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        sweeper = mock(WorkflowSweeper.class);
        when(sweeper.sweepAsync("workflowId")).thenReturn(CompletableFuture.completedFuture(null));
        sweeper.sweepAsync("workflowId");
    }

    @Test
    public void testSweep() {
        WorkflowContext workflowContext = new WorkflowContext("testAppId");
        WorkflowContext.set(workflowContext);
        
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        queueDAO = mock(QueueDAO.class);
        
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        try {
            when(workflowExecutor.decide("workflowId")).thenReturn(new WorkflowModel());
            sweeper.sweep("workflowId");
        } catch (NotFoundException e) {
            // Expected NotFoundException.
        }
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setTasks(new ArrayList<TaskModel>());
        
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        sweeper.unack(workflowModel, 60);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        long result = sweeper.workflowOffsetWithJitter(45);
        assertTrue("Result should be within range", result >= 30 && result <= 60);
    }
}