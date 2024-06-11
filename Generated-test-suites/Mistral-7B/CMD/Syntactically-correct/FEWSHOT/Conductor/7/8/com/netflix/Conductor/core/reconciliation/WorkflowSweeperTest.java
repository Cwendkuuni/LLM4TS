package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor.WorkflowExecutionResult;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;

@Component
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(new Random(1L));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenAnswer((i) -> createWorkflowModel());
        when(workflowExecutor.execute(anyString(), any())).thenReturn(WorkflowExecutionResult.success());
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong())).thenReturn(true);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        verify(workflowSweeper).sweep(WORKFLOW_ID);
        future.join();
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowSweeper).sweep(WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

        if (workflowModel != null && workflowModel.getStatus() != null && workflowModel.getStatus().isTerminal()) {
            verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        } else {
            long workflowOffsetTimeout = properties.getWorkflowOffsetTimeout().getSeconds();
            long postponeDurationSeconds = 0;
            Optional<TaskDef> taskDefinition = Optional.ofNullable(workflowModel.getTasks().get(0).getTaskDefinition());

            if (taskDefinition.isPresent()) {
                TaskDef taskDef = taskDefinition.get();
                if (taskDef.getPollTimeoutSeconds() != null) {
                    postponeDurationSeconds = taskDef.getPollTimeoutSeconds() + 1;
                } else {
                    postponeDurationSeconds = workflowOffsetTimeout + 1;
                }
            } else {
                postponeDurationSeconds = workflowOffsetTimeout + 1;
            }

            long expectedUnackTimeout = postponeDurationSeconds * 1000;
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, expectedUnackTimeout);
        }
    }

    @Test
    public void testSweepNotFound() {
        doThrow(new NotFoundException(WORKFLOW_ID)).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L);
        verify(logBacker).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
    }

    @Test
    public void testSweepException() {
        doThrow(new Exception("Test Exception")).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(Monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(logBacker).error("Error running sweep for " + WORKFLOW_ID, eq("Test Exception"));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();

        workflowSweeper.unack(workflowModel, 100L);

        TaskModel taskModel = workflowModel.getTasks().get(0);
        long expectedPostponeDurationSeconds =
                (taskModel.getResponseTimeoutSeconds() != null)
                        ? taskModel.getResponseTimeoutSeconds() + 1
                        : 101L;
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_MODEL.getWorkflowId(), expectedPostponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 100L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout / 3));
        assertTrue(jitter <= (workflowOffsetTimeout * 1.5));
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(WorkflowStatus.IN_PROGRESS);
        return workflowModel;
    }
}