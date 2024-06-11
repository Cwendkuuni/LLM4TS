package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
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
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOTestImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModelTestImpl;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static com.netflix.conductor.core.utils.Utils.DECIDER_QUEUE;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private static final String WORKFLOW_ID = "workflowId";
    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        properties = new ConductorProperties();
        queueDAO = new QueueDAOTestImpl();
        monitors = Mockito.mock(Monitors.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        sweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO);
        doReturn(properties).when(sweeper).getProperties();
        doReturn(queueDAO).when(sweeper).getQueueDAO();
        doReturn(monitors).when(sweeper).getMonitors();
    }

    @After
    public void teardown() {
        sweeper = null;
        properties = null;
        queueDAO = null;
        workflowExecutor = null;
        workflowRepairService = null;
        monitors = null;
    }

    @Test
    public void testSweepAsync() {
        sweeper.sweepAsync(WORKFLOW_ID).join();
        verify(workflowExecutor).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweepMethodNormalFlow() {
        WorkflowModel workflowModel = new WorkflowModelTestImpl();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.sweep(WORKFLOW_ID);

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testSweepMethodNotFoundFlow() {
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(WORKFLOW_ID);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnackMethodNormalFlow() {
        WorkflowModel workflowModel = new WorkflowModelTestImpl();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));

        sweeper.unack(workflowModel, 10L);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 11L * 1000);
    }

    @Test
    public void testUnackMethodWorkflowNotFoundFlow() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        sweeper.unack(null, 10L);

        verify(queueDAO, never()).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, 10L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitterMethodNormalFlow() {
        long workflowOffsetTimeout = 45;

        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }
}