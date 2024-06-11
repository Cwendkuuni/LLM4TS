package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Random;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    Optional<WorkflowRepairService> workflowRepairService = Optional.empty();
    ConductorProperties properties = new ConductorProperties();
    QueueDAO queueDAO = mock(QueueDAO.class);

    @Test
    public void testWorkflowSweeper() {
        
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor,
        workflowRepairService,
        properties,
        queueDAO);

        assertNotNull(sweeper);
    }

    @Test
    public void testSweepAsync() {
        
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor,
        workflowRepairService,
        properties,
        queueDAO);

        CompletableFuture<Void> future = sweeper.sweepAsync("some-workflow-id");
        assertEquals(CompletableFuture.completedFuture(null), future);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
    
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor,
        workflowRepairService,
        properties,
        queueDAO);
   
        long workflowOffsetTimeout = 45;
        long jitterOutput = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(jitterOutput >= 30 && jitterOutput <= 60);
    }
}