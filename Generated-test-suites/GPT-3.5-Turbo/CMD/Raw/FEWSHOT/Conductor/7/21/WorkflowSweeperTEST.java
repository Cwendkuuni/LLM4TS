package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowRepairService workflowRepairService;

    @Test
    public void testSweepAsync() {
        WorkflowSweeper workflowSweeper = Mockito.spy(WorkflowSweeper.class);
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        Mockito.doNothing().when(queueDAO).remove(Mockito.any(String.class), Mockito.any(String.class));
        Mockito.when(workflowExecutor.decide(Mockito.any(String.class))).thenReturn(workflowModel);

        workflowSweeper.sweep("testWorkflowId");
        
        // Add assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.unack(workflowModel, 60);
        
        // Add assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        long result = workflowSweeper.workflowOffsetWithJitter(45);

        // Add assertions here
    }

}