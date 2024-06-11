package com.netflix.conductor.core.reconciliation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-id";
    private static final String WORKFLOW_NAME = "workflow-name";
    private WorkflowSweeper sweeper;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private Monitors monitors;
    private WorkflowModel workflowModel;
    private Random random;

    @Before
    public void setUp() {
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        monitors = mock(Monitors.class);
        sweeper = new WorkflowSweeper(workflowExecutor, null, null, queueDAO, monitors);
        random = new Random();
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
    }

    @Test
    public void testSweepSuccess() {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        sweeper.sweep(WORKFLOW_ID);
        assertThat(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID), is(nullValue()));
    }

    @Test
    public void testSweepNotFound() {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());
        sweeper.sweep(WORKFLOW_ID);
        assertThat(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID), is(nullValue()));
    }

    @Test
    public void testSweepException() {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenThrow(new NotFoundException("Workflow not found"));
        sweeper.sweep(WORKFLOW_ID);
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void testUnackTaskInProgress() {
        workflowModel.getTasks().add(new TaskModel());
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.unack(workflowModel, 5000);
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void testUnackTaskScheduled() {
        workflowModel.getTasks().add(new TaskModel());
        workflowModel.getTasks().get(0).setStatus(TaskModel.Status.SCHEDULED);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(workflowModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.unack(workflowModel, 5000);
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void testUnackWorkflowNotFound() {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());
        sweeper.unack(workflowModel, 5000);
        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = random.nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1))
                - (workflowOffsetTimeout / 3);
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertThat(result, is(workflowOffsetTimeout + jitter));
    }
}