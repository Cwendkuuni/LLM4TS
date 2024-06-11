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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowContext;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper.WorkflowRepairService;
import com.netflix.conductor.core.scheduler.SchedulerTestUtils;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowExecution;
import com.netflix.conductor.model.WorkflowModel.WorkflowNotFoundException;
import com.netflix.conductor.test.TestMocks;
import com.netflix.conductor.test.TestUtils;
import com.netflix.conductor.utils.Utils;

@Component
@EnableScheduling
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private WorkflowRepairService workflowRepairService;

    private ConductorProperties properties;

    @Before
    public void setup() {
        properties = Mockito.spy(new ConductorProperties());
        when(properties.getAppId()).thenReturn("test-app-id");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        TestMocks.init(properties, workflowExecutor, queueDAO, workflowRepairService);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepEmptyWorkflow() {
        WorkflowModel emptyWorkflow = new WorkflowModel();

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(emptyWorkflow);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWorkflowNotFoundInExecutor() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWorkflowTerminal() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.TERMINATED);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWorkflowInProgress() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        long workflowOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(5L);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeout * 1000);
    }

    @Test
    public void testSweepWorkflowScheduled() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.SCHEDULED);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        long workflowOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(5L);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeout * 1000);
    }

    @Test
    public void testSweepWorkflowInProgressWithTasks() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskDefinition(new TaskDef());

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);
        taskModel2.setTaskDefinition(new TaskDef());

        List<TaskModel> tasks = Arrays.asList(taskModel1, taskModel2);
        workflowModel.setTasks(tasks);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        long workflowOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(5L);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeout * 1000);
    }

    @Test
    public void testSweepWorkflowTerminated() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.TERMINATED);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWorkflowFailed() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.FAILED);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWorkflowUnknownStatus() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(new Status("UNKNOWN"));

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepWorkflowNotFoundInQueue() {
        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testSweepConcurrent() {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(queueDAO.retrieveFromQueue(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        WorkflowSweeper sweeper1 = Mockito.spy(workflowSweeper);
        WorkflowSweeper sweeper2 = Mockito.spy(workflowSweeper);

        CompletableFuture<Void> future1 = sweeper1.sweepAsync(WORKFLOW_ID);
        CompletableFuture<Void> future2 = sweeper2.sweepAsync(WORKFLOW_ID);

        List<WorkflowExecution> executions = TestUtils.createWorkflowExecutions(2);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowModel.getWorkflowExecutions()).thenReturn(executions);

        future1.join();
        future2.join();

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }
}