package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String APP_ID = "appId";

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private Logger logger;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn(APP_ID);
        when(properties.getWorkflowOffsetTimeout()).thenReturn(Instant.ofEpochSecond(100));
        when(properties.getWorkflowOffsetTimeout().getSeconds()).thenReturn(100);
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 0L)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 10L)).thenReturn(true);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsyncWithValidWorkflowId() {
        sweeper.sweepAsync(WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweepAsyncWithNotFoundException() {
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(WORKFLOW_ID);
        sweeper.sweepAsync(WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithValidWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.sweep(WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verifyZeroInteractions(workflowRepairService);

        WorkflowContext workflowContext = new WorkflowContext(APP_ID);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        sweeper.sweep(WORKFLOW_ID);
        verifyZeroInteractions(workflowRepairService);

        TaskModel taskModel = new TaskModel();
        workflowModel.setTasks(Collections.singletonList(taskModel));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(false);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 10L)).thenReturn(false);
        sweeper.sweep(WORKFLOW_ID);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweepWithInvalidWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.sweep(WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verifyZeroInteractions(workflowRepairService);

        WorkflowModel invalidWorkflowModel = new WorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(invalidWorkflowModel);
        sweeper.sweep(WORKFLOW_ID);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);

        when(workflowModel.getStatus()).thenReturn(Status.ERROR);
        sweeper.sweep(WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testUnackWithTasksInProgress() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setTasks(Collections.singletonList(taskModel));

        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 11L * 1000);
    }

    @Test
    public void testUnackWithTasksScheduled() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(taskModel));

        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 11L * 1000);
    }

    @Test
    public void testUnackWithNoTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 11L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 100;
        assertEquals(134, sweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
        assertEquals(66, sweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
    }
}