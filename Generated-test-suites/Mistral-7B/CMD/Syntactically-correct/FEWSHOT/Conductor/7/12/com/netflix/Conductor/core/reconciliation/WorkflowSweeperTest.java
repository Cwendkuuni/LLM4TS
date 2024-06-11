package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private WorkflowSweeper workflowSweeper;
    private long workflowOffsetTimeout = 45;

    @Before
    public void setup() {
        properties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO);
    }

    @Test
    public void testSweepAsync() {
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        workflowSweeper.sweepAsync("workflowId")
                .join();

        verify(queueDAO).remove(eq("DECIDER_QUEUE"), eq("workflowId"));
    }

    @Test
    public void testSweep() {
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        when(workflowExecutor.decide(anyString()))
                .thenReturn(workflow);

        workflowSweeper.sweep("workflowId");

        verify(queueDAO).remove(eq("DECIDER_QUEUE"), eq("workflowId"));
        verify(workflowExecutor).decide(eq("workflowId"));
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        doThrow(new NotFoundException(""))
                .when(workflowExecutor)
                .decide(anyString());

        workflowSweeper.sweep("workflowId");

        verify(queueDAO).remove(eq("DECIDER_QUEUE"), eq("workflowId"));
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);

        when(taskModel.getStatus())
                .thenReturn(Status.IN_PROGRESS);

        when(taskModel.getTaskType())
                .thenReturn(TaskType.TASK_TYPE_WAIT);

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(CompletableFuture.completedFuture(null));

        workflowSweeper.unack(workflowModel, 1000);

        verify(queueDAO).setUnackTimeout(eq("DECIDER_QUEUE"), eq(workflowModel.getWorkflowId()), eq(1001L * 1000));
    }

    @Test
    public void testUnackScheduled() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDef = Mockito.mock(TaskDef.class);

        when(taskModel.getStatus())
                .thenReturn(Status.SCHEDULED);

        when(taskModel.getTaskDefinition())
                .thenReturn(Optional.of(taskDef));

        when(taskDef.getPollTimeoutSeconds())
                .thenReturn(Optional.of(10L));

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(CompletableFuture.completedFuture(null));

        workflowSweeper.unack(workflowModel, 1000);

        verify(queueDAO).setUnackTimeout(eq("DECIDER_QUEUE"), eq(workflowModel.getWorkflowId()), eq(11L * 1000));
    }

    @Test
    public void testUnackWorkflowDefinition() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);

        when(taskModel.getStatus())
                .thenReturn(Status.SCHEDULED);

        when(taskModel.getTaskDefinition())
                .thenReturn(Optional.empty());

        when(workflowModel.getWorkflowDefinition())
                .thenReturn(new TaskDef());

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(CompletableFuture.completedFuture(null));

        workflowSweeper.unack(workflowModel, 1000);

        verify(queueDAO).setUnackTimeout(eq("DECIDER_QUEUE"), eq(workflowModel.getWorkflowId()), eq(46L * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = new Random().nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1)) - (workflowOffsetTimeout / 3);

        assertEquals(workflowOffsetTimeout + jitter, workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
    }
}