package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.decision.Decision;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowExecution;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.util.Utils;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Logger logger;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final String WORKFLOW_ID = "workflow-1";

    @Before
    public void setup() {
        when(conductorProperties.getAppId()).thenReturn("app-id");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(java.time.Duration.ofSeconds(45));

        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0)).thenReturn(true);

        TaskModel taskModel = new TaskModel();
        when(taskModel.getTaskDefinition()).thenReturn(mock(TaskDef.class));
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(taskModel.getWorkflowId()).thenReturn(WORKFLOW_ID);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(new TaskModel[] { taskModel });
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.join();
        assertNull(workflowSweeper.workflowRepairService);
    }

    @Test
    public void testSweep() throws Exception {
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(true);
        workflowSweeper.sweep(WORKFLOW_ID);

        verifySweeper(true, null);
    }

    @Test
    public void testSweepNotFound() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));
        workflowSweeper.sweep(WORKFLOW_ID);

        verifySweeper(false, WORKFLOW_ID);
    }

    @Test
    public void testSweepException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new Exception(""));
        workflowSweeper.sweep(WORKFLOW_ID);

        Monitors.error("WorkflowSweeper", "sweep");
        verify(logger).error("Error running sweep for " + WORKFLOW_ID, eq(new Exception("")));
    }

    @Test
    public void testUnack() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());

        long workflowOffsetTimeout = 45;
        long postponeDurationSeconds = 46;

        workflowSweeper.unack(new WorkflowModel(), workflowOffsetTimeout);

        queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = new Random().nextInt(9) + 1;
        long workflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffset >= (workflowOffsetTimeout - jitter));
        assertTrue(workflowOffset <= (workflowOffsetTimeout + jitter));
    }

    private void verifySweeper(boolean verifyAndRepair, String workflowId) {
        if (verifyAndRepair) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        }
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

        if (verifyAndRepair) {
            verify(workflowExecutor).decide(WORKFLOW_ID);
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        } else {
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        }

        verifyZeroInteractions(logger);
    }
}