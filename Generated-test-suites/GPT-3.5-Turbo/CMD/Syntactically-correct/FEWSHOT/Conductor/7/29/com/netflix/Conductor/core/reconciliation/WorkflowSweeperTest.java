package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setWorkflowId("workflowId");

        WorkflowContext workflowContext = new WorkflowContext("appId");
        WorkflowContext.set(workflowContext);

        workflowSweeper.sweep("workflowId");

        // Add assertion here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(Status.IN_PROGRESS);

        workflowSweeper.unack(workflowModel, 60);

        // Add assertion here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= 30 && result <= 60);
    }
    
}