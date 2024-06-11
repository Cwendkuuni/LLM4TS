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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Logger logger;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(60L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync_ShouldCallSweepAndReturnCompletedFuture() throws Exception {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).join();
        verify(sweeper).sweep(workflowId);
    }

    @Test
    public void testSweep_ShouldSweepWorkflow() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        // Verify and repair tasks in the workflow.
        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        }

        // Check if workflow exists
        verify(workflowExecutor, times(1)).decide(workflowId);
        assertTrue(workflowModel.getTasks().isEmpty());
        assertEquals(Status.NOT_FOUND, workflowModel.getStatus());

        // Remove workflow from decider queue
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_ShouldUnackWorkflow() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        // Unack workflow
        long workflowOffsetTimeout = 60L;
        long postponeDurationSeconds = 0;
        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() == Status.IN_PROGRESS) {
                if (taskModel.getTaskType().equals(TaskType.TASK_TYPE_WAIT)
                        || taskModel.getTaskType().equals(TaskType.TASK_TYPE_HUMAN)) {
                    postponeDurationSeconds = taskModel.getWaitTimeout() + 1;
                } else {
                    postponeDurationSeconds = taskModel.getResponseTimeoutSeconds() + 1;
                }
                break;
            }
        }
        long workflowOffsetTimeoutWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test(expected = NotFoundException.class)
    public void testSweep_ShouldNotFound() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

        sweeper.sweep(workflowId);

        // Verify and repair tasks in the workflow.
        if (workflowRepairService != null) {
            verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(workflowId);
        }

        // Check if workflow exists and remove it from decider queue
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep_ShouldMonitorError() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        try {
            uncheckedExceptionThrowingMethod(sweeper);
        } catch (Exception e) {
            Monitors.error(WorkflowSweeper.class, "sweep");
            verify(logger, times(1)).error("Error running sweep for " + workflowId, e);
        }
    }

    @VisibleForTesting
    private void uncheckedExceptionThrowingMethod(WorkflowSweeper sweeper) throws Exception {
        throw new Exception("Test exception");
    }

    @Test
    public void testUnack_ShouldUnackAllTasks() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(new ArrayList<>());
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        long workflowOffsetTimeout = 60L;

        sweeper.sweep(workflowId);
        sweeper.unack(workflowModel, workflowOffsetTimeout);

        for (TaskModel taskModel : workflowModel.getTasks()) {
            if (taskModel.getStatus() != Status.UNACKED) {
                fail("Task [" + taskModel.getId() + "] should be unack now.");
            }
        }

        long workflowOffsetTimeoutWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), workflowOffsetTimeoutWithJitter * 1000);
    }

    @Test
    public void testUnack_ShouldNotUnackCompletedWorkflow() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.COMPLETED);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        long workflowOffsetTimeout = 60L;

        sweeper.sweep(workflowId);
        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());
    }

    @Test
    public void testUnack_ShouldNotUnackNotFoundWorkflow() throws Exception {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

        sweeper.sweep(workflowId);
        sweeper.unack(null, 60L);

        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }
}