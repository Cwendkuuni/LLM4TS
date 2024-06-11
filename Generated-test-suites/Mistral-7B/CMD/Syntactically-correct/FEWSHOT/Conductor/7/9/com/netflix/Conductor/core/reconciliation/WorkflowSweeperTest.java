package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.TestUtils;

@Component
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";
    private static final int WAIT_TIMEOUT = 10;
    private static final int RESPONSE_TIMEOUT = 20;

    @Value("${workflowOffsetTimeout}")
    private int workflowOffsetTimeout;

    @Autowired
    private ConductorProperties properties;

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private WorkflowExecutor workflowExecutorMock;

    @Autowired
    private QueueDAO queueDAOMock;

    @Autowired
    private Monitors monitorsMock;

    @Autowired
    private WorkflowRepairService workflowRepairServiceMock;

    @Before
    public void setup() {
        properties.setAppId("test-app");
        queueDAOMock = Mockito.mock(QueueDAO.class);
        monitorsMock = Mockito.mock(Monitors.class);
        workflowExecutorMock = Mockito.mock(WorkflowExecutor.class);
        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());
        workflowSweeper = new WorkflowSweeper(
                workflowExecutorMock,
                workflowRepairServiceMock,
                properties,
                queueDAOMock);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(workflowExecutorMock, queueDAOMock, monitorsMock);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        assertTrue(future.join());
    }

    @Test
    public void testSweep_NotFoundException() {
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAOMock).remove(eq(SchedulerConfiguration.DECIDER_QUEUE), eq(WORKFLOW_ID));
    }

    @Test
    public void testSweep_WorkflowFound_TerminalStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.COMPLETED);
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAOMock).remove(eq(SchedulerConfiguration.DECIDER_QUEUE), eq(WORKFLOW_ID));
    }

    @Test
    public void testSweep_WorkflowFound_NonTerminalStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.RUNNING);
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        workflowSweeper.sweeper(WORKFLOW_ID);
        long workflowOffsetTimeoutWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        verify(queueDAOMock).setUnackTimeout(
                eq(SchedulerConfiguration.DECIDER_QUEUE),
                eq(WORKFLOW_ID),
                lt(workflowOffsetTimeoutWithJitter * 1000));
    }

    @Test
    public void testSweep_WorkflowNotFound_Exception() {
        when(workflowExecutorMock.decide(WORKFLOW_ID)).thenThrow(new Exception());
        workflowSweeper.sweeper(WORKFLOW_ID);
        Monitors.error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
        verify(queueDAOMock).setUnackTimeout(eq(SchedulerConfiguration.DECIDER_QUEUE), eq(WORKFLOW_ID), anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel inProgressTaskModel = new TaskModel();
        inProgressTaskModel.setStatus(Status.IN_PROGRESS);
        TaskModel scheduledTaskModel = new TaskModel();
        scheduledTaskModel.setStatus(Status.SCHEDULED);
        List<TaskModel> tasks = Arrays.asList(inProgressTaskModel, scheduledTaskModel);
        workflowModel.setTasks(tasks);

        long postponeDurationSeconds = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        assertEquals(workflowOffsetTimeout + 1, postponeDurationSeconds);

        inProgressTaskModel.setStatus(Status.FAILED);
        postponeDurationSeconds = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        assertEquals(workflowOffsetTimeout + 1, postponeDurationSeconds);

        scheduledTaskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
        postponeDurationSeconds = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        assertEquals(workflowOffsetTimeout + 1, postponeDurationSeconds);

        workflowModel.setWorkflowDefinition(Mockito.mock(TaskDef.class));
        postponeDurationSeconds = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        assertEquals(workflowOffsetTimeout + 1, postponeDurationSeconds);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        int workflowOffsetTimeout = 5;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(workflowOffsetWithJitter >= (workflowOffsetTimeout - 1) && workflowOffsetWithJitter <= (workflowOffsetTimeout + 1));
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks_NoTasksToRepair() {
        when(workflowRepairServiceMock.getFailedTasks(WORKFLOW_ID)).thenReturn(Collections.emptyList());
        workflowRepairServiceMock.verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        verifyZeroInteractions(workflowRepairServiceMock);
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks_TasksToRepair() {
        List<TaskModel> tasks = Arrays.asList(
            new TaskModel().setStatus(Status.FAILED),
            new TaskModel().setStatus(Status.FAILED)
        );
        when(workflowRepairServiceMock.getFailedTasks(WORKFLOW_ID)).thenReturn(tasks);
        workflowRepairServiceMock.verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        verify(workflowRepairServiceMock).repairTask(eq(tasks.get(0)));
        verify(workflowRepairServiceMock).repairTask(eq(tasks.get(1)));
    }
}