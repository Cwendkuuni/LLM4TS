package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@Component
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;
    @InjectMocks
    private WorkflowSweeper sweeper;

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).join();
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = createWorkflowModel();
        when(queueDAO.get(DECIDER_QUEUE, eq(workflowId))).thenReturn(workflow);

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweepNotFoundException() {
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, eq(workflowId))).thenThrow(new NotFoundException("Workflow not found"));

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(logger, times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId, isA(Exception.class));
    }

    @Test
    public void testSweepException() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = createWorkflowModel();
        when(queueDAO.get(DECIDER_QUEUE, eq(workflowId))).thenReturn(workflow);

        doThrow(new RuntimeException("Error running sweep for " + workflowId))
                .when(sweeper).sweep(workflowId);

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verify(Monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
        verify(logger, times(1)).error("Error running sweep for " + workflowId, isA(RuntimeException.class));
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        String workflowId = "testWorkflowId";
        when(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        sweeper.sweep(workflowId);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel();

        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(1001L * 1000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long actual = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(actual >= (workflowOffsetTimeout - 15) && actual <= (workflowOffsetTimeout + 15));
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setTasks(Arrays.asList(createTaskModel("task1"), createTaskModel("task2")));
        return workflowModel;
    }

    private TaskModel createTaskModel(String name) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("task1");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Optional.of(new TaskDef("TestTaskDef", "testTaskType")));
        taskModel.setWorkflowModel(new WorkflowModel());
        return taskModel;
    }

    @Autowired
    @VisibleForTesting
    private Logger logger;
}