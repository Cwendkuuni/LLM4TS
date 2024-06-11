package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.testutils.TestWorkflowExecutor;
import com.netflix.conductor.testutils.TestWorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowModel.TestTaskDefinition;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private QueueDAO queueDAO;

    @Autowired
    private TestWorkflowExecutor workflowExecutor;

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflow-1";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflow-1";

        // Initialize mocks
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowContext workflowContext = mock(WorkflowContext.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        when(properties.getAppId()).thenReturn("test-app");
        when(workflow.getWorkflowName()).thenReturn("test-workflow");
        when(workflow.getWorkflowId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(Status.RUNNING);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, null, properties, queueDAO);

        sweeper.sweep(workflowId);

        verify(queueDAO, atLeastOnce()).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNotFound() throws Exception {
        String workflowId = "workflow-1";

        // Initialize mocks
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowContext workflowContext = mock(WorkflowContext.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowModel workflow = null;

        when(properties.getAppId()).thenReturn("test-app");
        when(workflowContext.newWorkflowContext()).thenReturn(workflowContext);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, null, properties, queueDAO);

        try {
            sweeper.sweep(workflowId);
            fail();
        } catch (NotFoundException e) {
            verify(queueDAO, atLeastOnce()).remove(DECIDER_QUEUE, workflowId);
        }
    }

    @Test
    public void testSweepError() throws Exception {
        String workflowId = "workflow-1";

        // Initialize mocks
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowContext workflowContext = mock(WorkflowContext.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        Monitors monitors = mock(Monitors.class);
        Logger logger = mock(Logger.class);

        when(properties.getAppId()).thenReturn("test-app");
        when(workflow.getWorkflowName()).thenReturn("test-workflow");
        when(workflow.getWorkflowId()).thenReturn(workflowId);
        when(workflow.getStatus()).thenReturn(Status.RUNNING);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflowRepairService).thenReturn(workflowRepairService);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong())).thenThrow(new Exception());
        when(monitors.error(anyString(), anyString())).thenReturn(monitors);
        when(logger.error(anyString(), any(Throwable.class))).thenReturn(logger);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, workflowRepairService, properties, queueDAO);

        try {
            sweeper.sweep(workflowId);
            fail();
        } catch (Exception e) {
            verify(monitors, times(1)).error(anyString(), anyString());
            verify(logger, times(1)).error(anyString(), any(Throwable.class));
        }
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new TestWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                new TestTaskModel("task1", TaskType.TASK_TYPE_WAIT, 5),
                new TestTaskModel("task2", TaskType.TASK_TYPE_HUMAN),
                new TestTaskModel("task3", TaskType.TASK_TYPE_SCRIPT, 10)
        );
        workflowModel.setTasks(tasks);

        WorkflowModel expectedWorkflowModel = new TestWorkflowModel();
        List<TaskModel> expectedTasks = Arrays.asList(
                new TestTaskModel("task1", TaskType.TASK_TYPE_WAIT, 6),
                new TestTaskModel("task2", TaskType.TASK_TYPE_HUMAN),
                new TestTaskModel("task3", TaskType.TASK_TYPE_SCRIPT, 11)
        );
        expectedWorkflowModel.setTasks(expectedTasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);

        WorkflowSweeper sweeper = new WorkflowSweeper(
                workflowExecutor, workflowRepairService, properties, queueDAO);

        sweeper.unack(workflowModel, 5000);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 6001L);
        assertNotEquals(workflowModel, expectedWorkflowModel);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 120;

        long actualWorkflowOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expectedMinWorkflowOffsetTimeout = 80;
        long expectedMaxWorkflowOffsetTimeout = 160;

        assertThat(actualWorkflowOffsetTimeout).isBetween(expectedMinWorkflowOffsetTimeout, expectedMaxWorkflowOffsetTimeout);
    }
}