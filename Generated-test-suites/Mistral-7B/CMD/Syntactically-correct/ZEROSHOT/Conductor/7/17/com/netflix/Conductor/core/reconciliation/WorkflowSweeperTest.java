package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final ConductorProperties properties = mock(ConductorProperties.class);
    private static final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private static final QueueDAO queueDAO = mock(QueueDAO.class);
    private static final WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);

    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties,
                queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).join();
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(workflowExecutor).decide(workflowIdCaptor.capture());
        assertEquals(workflowId, workflowIdCaptor.getValue());
    }

    @Test
    public void testSweepWithoutWorkflow() {
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), workflowId);
    }

    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "testWorkflowId";
        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        sweeper.sweep(workflowId);
        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), workflowId);
        Mockito.verify(LOGGER, Mockito.times(1)).info("Workflow NOT found for id:{}. Removed it from decider queue",
                workflowId);
    }

    @Test
    public void testSweepAndVerifyTasks() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.STARTED);
        workflowModel.setWorkflowId(workflowId);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel1.setTaskDefinition(new TaskDef());
        taskModel1.setTaskId("task1");
        List<TaskModel> tasks = Arrays.asList(taskModel1);
        workflowModel.setTasks(tasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);

        sweeper.sweep(workflowId);
        Mockito.verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), workflowId);
    }

    @Test
    public void testSweepWithVerificationFailure() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.STARTED);
        workflowModel.setWorkflowId(workflowId);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel1.setTaskDefinition(new TaskDef());
        taskModel1.setTaskId("task1");
        List<TaskModel> tasks = Arrays.asList(taskModel1);
        workflowModel.setTasks(tasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(false);

        sweeper.sweep(workflowId);
        Mockito.verify(queueDAO).remove(Mockito.eq(DECIDER_QUEUE), workflowId);
        Mockito.verify(monitors).error(Mockito.eq(WorkflowSweeper.class.getSimpleName()),
                Mockito.eq("sweep"), Mockito.any(Throwable.class));
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowStatus.STARTED);
        workflowModel.setWorkflowId(workflowId);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel1.setTaskDefinition(new TaskDef());
        taskModel1.setTaskId("task1");
        List<TaskModel> tasks = Arrays.asList(taskModel1);
        workflowModel.setTasks(tasks);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);

        sweeper.unack(workflowModel, 5L);
        Mockito.verify(queueDAO).setUnackTimeout(Mockito.eq(DECIDER_QUEUE), workflowId,
                Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;
        long jitter = 2L;
        long expectedWorkflowOffset = workflowOffsetTimeout + jitter;

        long actualWorkflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(expectedWorkflowOffset, actualWorkflowOffset);
    }
}