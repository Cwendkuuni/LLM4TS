package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private ConductorProperties properties;

    @Before
    public void setUp() {
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        properties = new ConductorProperties();
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertNotNull("Result should not be null", result);
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setStatus(new WorkflowModel.Status());
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setTimeoutSeconds(60);
        workflowModel.setWorkflowDefinition(workflowDef);
        
        WorkflowContext workflowContext = new WorkflowContext("appId");
        WorkflowContext.set(workflowContext);
        
        WorkflowModel workflowModelMock = mock(WorkflowModel.class);
        NotFoundException nfe = new NotFoundException();
        
        // Test with NotFoundException
        try {
            workflowSweeper.sweep("invalidWorkflowId");
        } catch (NotFoundException e) {
            assertEquals("Workflow NOT found for id:invalidWorkflowId. Removed it from decider queue", e.getMessage());
        }
        
        // Test with Exception
        try {
            workflowSweeper.sweep("workflowId");
        } catch (Exception e) {
            assertEquals("Error running sweep for workflowId", e.getMessage());
        }
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.getTasks().add(taskModel);
        
        workflowSweeper.unack(workflowModel, 60);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Check if the result is within the expected range
        // Example: result should be between 30 and 60
        assert(result >= 30 && result <= 60);
    }
}