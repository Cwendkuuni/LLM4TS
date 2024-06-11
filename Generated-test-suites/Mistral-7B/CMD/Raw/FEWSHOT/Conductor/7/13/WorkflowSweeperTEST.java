package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
import com.netflix.conductor.metrics.Monitors;

@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private WorkflowSweeper workflowSweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        initMocks(this);
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        WorkflowContext workflowContext = new WorkflowContext("appId");
        when(properties.getAppId()).thenReturn("appId");

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflowId";
        doReturn(CompletableFuture.completedFuture(null)).when(workflowSweeper).sweep(workflowId);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertTrue(result.join());
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).recordUnackTime(eq("workflowName"), anyLong());

        if (workflowRepairService != null) {
            verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        }
    }

    @Test
    public void testSweepNotFound() throws Exception {
        String workflowId = "workflowId";
        doThrow(new NotFoundException("")).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getName()), eq("sweep"));
        verify(loggers, times(1)).info(
                eq("Workflow NOT found for id:{}. Removed it from decider queue"), eq(workflowId), isA(NotFoundException.class));
    }

    @Test
    public void testSweepException() throws Exception {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        doReturn(workflowModel).when(workflowExecutor).decide(workflowId);

        doThrow(new Exception("Exception")).when(workflowSweeper).unack(workflowModel, anyLong());

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), anyLong());
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getName()), eq("sweep"));
        verify(loggers, times(1)).error(
                eq("Error running sweep for " + workflowId), isA(Exception.class));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        workflowModel.setTasks(tasks);

        long workflowOffsetTimeout = 45L;
        when(workflowModel.getWorkflowDefinition()).thenReturn(new TaskDef());

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds = 46L;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testUnackTaskTypeTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.SCHEDULED);
        taskModel1.setTaskType(TaskType.TASK_TYPE_TASK);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        workflowModel.setTasks(tasks);

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds = 46L;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testUnackTaskTypeHuman() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.SCHEDULED);
        taskModel1.setTaskType(TaskType.TASK_TYPE_HUMAN);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        workflowModel.setTasks(tasks);

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds = 46L;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testUnackWorkflowDefinition() {
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = new ArrayList<>();
        workflowModel.setTasks(tasks);

        long workflowOffsetTimeout = 45L;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        long postponeDurationSeconds = 46L;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(postponeDurationSeconds * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expectedMin = 30L;
        long expectedMax = 60L;
        long jitter = new Random().nextInt((int) (2 * (expectedMax - expectedMin + 1)) - (expectedMax - expectedMin));
        long expected = workflowOffsetTimeout + jitter;

        assertEquals(expected, result);
    }
}