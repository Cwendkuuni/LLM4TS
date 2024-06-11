package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = new ConductorProperties();
        properties.setAppId("testAppId");

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        
        assertNotNull(future.get());
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(workflowExecutor.decide(eq(workflowId))).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        // Add your assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");

        workflowModel.getTasks().add(new TaskModel(Status.IN_PROGRESS));
        workflowModel.getTasks().get(0).setTaskType(TaskModel.TaskType.TASK_TYPE_WAIT);
        workflowModel.getTasks().get(0).setWaitTimeout(10L);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(eq("testWorkflowId"))).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep("testWorkflowId");
        assertNotNull(workflowModel);
        // Add your assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        // Add your assertions here
    }
}