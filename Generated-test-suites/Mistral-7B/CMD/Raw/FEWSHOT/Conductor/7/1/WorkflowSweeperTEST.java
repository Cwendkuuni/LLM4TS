package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

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
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private WorkflowSweeper workflowSweeper;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        monitors = Mockito.mock(Monitors.class);
        WorkflowContext workflowContext = new WorkflowContext(conductorProperties.getAppId());
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, queueDAO, monitors);
    }

    @Test
    public void testSweepAsync() throws Exception {
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(null);

        workflowSweeper.sweepAsync("workflowId1").get();

        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
    }

    @Test
    public void testSweepWithNotFoundException() throws Exception {
        doThrow(new NotFoundException("Test Exception")).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("workflowId2");

        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId2");
        verify(LOGGER).info("Workflow NOT found for id:{}. Removed it from decider queue", "workflowId2",
                argThat(isStringContaining("Test Exception")));
    }

    @Test
    public void testSweepWithException() throws Exception {
        doThrow(new Exception("Test Exception")).when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("workflowId3");

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId3", anyLong());
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(LOGGER).error("Error running sweep for workflowId3", argThat(isInstanceOf(Exception.class)));
    }

    @Test
    public void testUnackWithTaskInProgress() throws Exception {
        TaskModel taskModel = new TaskModel(new TaskDef("TaskType1"), new Random().nextLong(), Status.IN_PROGRESS, null);
        WorkflowModel workflowModel = new WorkflowModel("workflowName1", Arrays.asList(taskModel), null);

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(null);

        workflowSweeper.unack(workflowModel, 30000L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowName1", 30001L * 1000);
    }

    @Test
    public void testUnackWithTaskScheduled() throws Exception {
        TaskModel taskModel = new TaskModel(new TaskDef("TaskType1"), new Random().nextLong(), Status.SCHEDULED, null);
        WorkflowModel workflowModel = new WorkflowModel("workflowName1", Arrays.asList(taskModel), null);

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(null);

        workflowSweeper.unack(workflowModel, 30000L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowName1", 30001L * 1000);
    }

    @Test
    public void testUnackWithWorkflowDefinition() throws Exception {
        TaskModel taskModel = new TaskModel(new TaskDef("TaskType1"), new Random().nextLong(), Status.IN_PROGRESS, null);
        WorkflowModel workflowModel = new WorkflowModel("workflowName1", Arrays.asList(taskModel), new TaskDef("WorkflowDefinition1"));

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(null);

        workflowSweeper.unack(workflowModel, 30000L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowName1", 30001L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45000;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= 30000 && jitter <= 60000);
    }
}