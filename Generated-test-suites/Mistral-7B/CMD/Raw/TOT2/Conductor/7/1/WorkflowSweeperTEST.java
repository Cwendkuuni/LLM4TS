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
import org.slf4j.Logger;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowContext;
import com.netflix.conductor.testutils.TestWorkflowExecutor;
import com.netflix.conductor.testutils.TestWorkflowModel;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        properties = Mockito.mock(ConductorProperties.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        monitors = Mockito.mock(Monitors.class);
        workflowExecutor = new TestWorkflowExecutor();
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        sweeper.setLogger(LOGGER);
    }

    @After
    public void teardown() {
        reset(properties, queueDAO, workflowExecutor, workflowRepairService, monitors);
    }

    @Test
    public void testSweepAsync() throws Exception {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        givenWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME, Status.IN_PROGRESS);

        sweeper.sweepAsync(WORKFLOW_ID)
                .get();

        verify(queueDAO).remove(anyString(), WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() throws Exception {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        doThrow(new NotFoundException("")).when(workflowExecutor).decide(WORKFLOW_ID);

        sweeper.sweepAsync(WORKFLOW_ID)
                .get();

        verify(queueDAO).remove(anyString(), WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(loggers(), times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
    }

    @Test
    public void testSweepAsync_ExceptionDuringSweep() throws Exception {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        givenWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME, Status.IN_PROGRESS);
        doThrow(new Exception("Sweep Error")).when(workflowExecutor).decide(WORKFLOW_ID);

        sweeper.sweepAsync(WORKFLOW_ID)
                .join();

        verify(queueDAO).remove(anyString(), WORKFLOW_ID);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getName()), eq("sweep"), any(Throwable.class));
        verify(loggers(), times(1)).error(
                "Error running sweep for " + WORKFLOW_ID, any(Throwable.class));
    }

    @Test
    public void testSweep_WorkflowFoundWithTerminalStatus() throws Exception {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        givenWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME, Status.COMPLETED);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(anyString(), WORKFLOW_ID);
        verify(workflowExecutor, never()).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowFoundWithNotFoundStatus() throws Exception {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        givenWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME, Status.NOT_FOUND);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(anyString(), WORKFLOW_ID);
        verify(workflowExecutor, never()).decide(WORKFLOW_ID);
        verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        verify(loggers(), times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = new TestWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TestTaskModel(), new TestTaskModel());
        workflowModel.setTasks(tasks);

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.unack(workflowModel, 5L);

        for (TaskModel taskModel : tasks) {
            verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(WORKFLOW_ID),
                    gt(5L * 1000));
        }
    }

    @Test
    public void testUnack_NoTasks() throws Exception {
        WorkflowModel workflowModel = new TestWorkflowModel();

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.unack(workflowModel, 5L);

        verify(queueDAO, never()).setUnackTimeout(anyString(), anyString(), gt(0L));
        verify(loggers(), times(1)).warn(
                "Workflow with {} id can not be found. Attempting to unack using the id", WORKFLOW_ID);
    }

    private void givenWorkflowModel(String workflowId, String workflowName, Status status) {
        WorkflowModel workflowModel = new TestWorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowName(workflowName);
        workflowModel.setStatus(status);
        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);
    }

    private Logger loggers() {
        return Mockito.mock(Logger.class);
    }

    private static class TestWorkflowModel extends WorkflowModel {
        public TestWorkflowModel() {
            super();
            setWorkflowId("testWorkflowId");
            setWorkflowName("testWorkflowName");
            setStatus(Status.IN_PROGRESS);
        }
    }

    private static class TestTaskModel extends TaskModel {
        public TestTaskModel() {
            super();
            setTaskId("testTaskId");
            setTaskDefinition(new TestTaskDef());
            setStatus(Status.IN_PROGRESS);
        }
    }

    private static class TestTaskDef implements TaskDef {
        public TestTaskDef() {
            setTaskType(TaskType.TASK_TYPE_WAIT);
            setWaitTimeout(10L);
        }
    }
}