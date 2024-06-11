package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.ReconciliationTestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);
    private static final String CLASS_NAME = WorkflowSweeper.class.getSimpleName();
    private static final String WORKFLOW_ID = "workflow-1";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 60 * 60; // 1 hour
    private static final int JITTER_RANGE = WORKFLOW_OFFSET_TIMEOUT / 3;

    private WorkflowSweeper sweeper;
    private WorkflowExecutor mockWorkflowExecutor;
    private QueueDAO mockQueueDAO;
    private ConductorProperties mockProperties;
    private WorkflowRepairService mockWorkflowRepairService;

    @Before
    public void setUp() {
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockProperties = Mockito.spy(new ConductorProperties());
        mockWorkflowRepairService = mock(WorkflowRepairService.class);
        sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> sweeperFuture = sweeper.sweepAsync(WORKFLOW_ID);
        sweeperFuture.join();
        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
    }

    @Test
    public void testSweep() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowName("test-workflow");
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.RUNNING);

        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflow);
        when(mockWorkflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        sweeper.sweep(WORKFLOW_ID);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));

        WorkflowModel returnedWorkflow = workflow;
        if (workflowRepairService != null) {
            workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID);
            returnedWorkflow = workflowExecutor.decide(WORKFLOW_ID);
        }
        assertEquals("test-workflow", returnedWorkflow.getWorkflowName());

        if (workflow != null && workflow.getStatus().isTerminal()) {
            verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
        } else {
            long workflowOffsetTimeout =
                    sweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);
            long startTime = Instant.now().toEpochMilli();
            sweeper.unack(workflow, workflowOffsetTimeout);
            long endTime = Instant.now().toEpochMilli();
            long unackTime = endTime - startTime;
            Monitors monitors = Mockito.mock(Monitors.class);
            Monitors.recordUnackTime(eq(returnedWorkflow.getWorkflowName()), eq(unackTime));
            verify(monitors, times(1)).recordUnackTime(eq(returnedWorkflow.getWorkflowName()), eq(unackTime));
        }
    }

    @Test
    public void testNotFoundWorkflow() {
        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new NotFoundException());
        sweeper.sweep(WORKFLOW_ID);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
        verify(mockLogger, times(1)).info(containsString("Workflow NOT found for id:" + WORKFLOW_ID));
    }

    @Test
    public void testExceptionDuringSweep() {
        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);
        doThrow(new RuntimeException())
                .when(mockWorkflowExecutor)
                .decide(WORKFLOW_ID);

        sweeper.sweep(WORKFLOW_ID);

        verify(mockQueueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID), anyLong());
        verify(mockMonitors, times(1)).error(eq(CLASS_NAME), eq("sweep"));
        verify(mockLogger, times(1)).error(containsString("Error running sweep for " + WORKFLOW_ID));

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowName("test-workflow");
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.RUNNING);

        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflow);

        doThrow(new RuntimeException())
                .when(mockWorkflowRepairService)
                .verifyAndRepairWorkflowTasks(WORKFLOW_ID);

        sweeper.sweep(WORKFLOW_ID);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
        WorkflowModel returnedWorkflow = workflow;
        WorkflowModel newWorkflow = new WorkflowModel();
        newWorkflow.setWorkflowName("test-workflow-new");
        newWorkflow.setWorkflowId(WORKFLOW_ID);
        newWorkflow.setStatus(Status.RUNNING);

        when(mockWorkflowExecutor.decide(WORKFLOW_ID)).thenReturn(newWorkflow);

        sweeper.sweep(WORKFLOW_ID);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(WORKFLOW_ID));
        verify(mockQueueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID), anyLong());
    }

    @Test
    public void testUnackTimeoutWithWorkflow() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowName("test-workflow");
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setStatus(Status.RUNNING);

        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflow);
        when(mockWorkflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        long workflowOffsetTimeout =
                sweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT);

        sweeper.unack(workflow, workflowOffsetTimeout);

        long workflowUnackTimeout = queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        assertEquals(workflowOffsetTimeout * 1000, workflowUnackTimeout);
    }

    @Test
    public void testUnackTimeoutWithNullWorkflow() {
        when(mockQueueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        sweeper.unack(null, WORKFLOW_OFFSET_TIMEOUT);

        long workflowUnackTimeout = queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        assertNull(workflowUnackTimeout);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60 * 60; // 1 hour
        long jitter = new Random().nextInt((int) (2 * JITTER_RANGE + 1)) - JITTER_RANGE;

        long expectedWorkflowOffsetTimeout = workflowOffsetTimeout + jitter;

        long actualWorkflowOffsetTimeout = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(expectedWorkflowOffsetTimeout, actualWorkflowOffsetTimeout);
    }
}