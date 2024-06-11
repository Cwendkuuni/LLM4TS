package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.TestWorkflowModel;
import com.netflix.conductor.test.TestWorkflowModel.TaskModelBuilder;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowContext workflowContext;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Logger logger;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(workflowContext.newWorkflowContext()).thenReturn(workflowContext);
        when(workflowContext.set(String.class, String.class)).thenReturn(workflowContext);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(logger.isInfoEnabled()).thenReturn(true);
        when(queueDAO.remove(String.class, String.class)).thenReturn(true);
        when(queueDAO.setUnackTimeout(String.class, String.class, long.class)).thenReturn(true);
        when(monitors.error(String.class, String.class)).thenReturn(null);
        when(monitors.recordUnackTime(String.class, long.class)).thenReturn(null);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        sweeper.sweepAsync(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testSweepWorkflowFoundAndTerminal() {
        String workflowId = "workflowId";
        WorkflowModel workflow = new TestWorkflowModel.Builder().build();
        workflow.setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.sweep(workflowId);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "workflowId";

        doThrow(new NotFoundException("Workflow not found"))
                .when(workflowExecutor)
                .decide(workflowId);

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(logger).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweepException() {
        String workflowId = "workflowId";
        WorkflowModel workflow = new TestWorkflowModel.Builder().build();

        doThrow(new RuntimeException("Test exception"))
                .when(workflowExecutor)
                .decide(workflowId);

        sweeper.sweep(workflowId);

        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(logger).error("Error running sweep for " + workflowId, eq("Test exception"));
    }

    @Test
    public void testUnack() {
        String workflowId = "workflowId";
        WorkflowModel workflow = new TestWorkflowModel.Builder().build();
        List<TaskModel> tasks = Arrays.asList(
                new TaskModelBuilder().withStatus(Status.IN_PROGRESS).build(),
                new TaskModelBuilder().withStatus(Status.SCHEDULED).build());
        workflow.setTasks(tasks);

        sweeper.unack(workflow, 10L);

        for (TaskModel task : tasks) {
            if (task.getStatus() == Status.IN_PROGRESS) {
                verify(queueDAO).setUnackTimeout(DECIder_QUEUE, workflowId, 11L * 1000);
                break;
            }
        }

        if (tasks.get(1).getStatus() == Status.SCHEDULED) {
            TaskDef taskDef = new TestTaskDef();
            when(tasks.get(1).getTaskDefinition()).thenReturn(taskDef);

            verify(queueDAO).setUnackTimeout(DECIder_QUEUE, workflowId, 11L * 1000);
        } else {
            verify(queueDAO).setUnackTimeout(DECIder_QUEUE, workflowId, 11L * 1000);
        }
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;

        long jitter = new Random().nextInt(11);

        long expected = workflowOffsetTimeout + jitter;

        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(expected, result);
    }
}