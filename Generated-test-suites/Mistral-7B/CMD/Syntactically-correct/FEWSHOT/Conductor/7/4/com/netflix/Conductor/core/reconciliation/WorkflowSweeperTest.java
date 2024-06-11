package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(Duration.ofSeconds(45));
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(workflowRepairService).thenReturn(mock(WorkflowRepairService.class));
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException(""));
    }

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflowSweeper.sweep(workflowId);

        verify(logger).debug("Running sweeper for workflow {}", workflowId);

        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        }

        assertNull(workflow);
    }

    @Test
    public void testSweepWithWorkflow() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        WorkflowModel expectedWorkflow = new WorkflowModel();
        expectedWorkflow.setWorkflowId(workflowId);
        expectedWorkflow.setStatus(WorkflowModel.Status.TERMINAL);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, never()).decide(workflowId);
        verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(workflowId);
        verify(monitors).recordUnackTime(eq("testAppId"), anyLong());
    }

    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(logger).info("Workflow NOT found for id:{}. Removed it from decider queue",
                workflowId);
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException(""));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), "sweep");
        verify(logger).error("Error running sweep for " + workflowId, isA(RuntimeException.class));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();

        workflowSweeper.unack(workflowModel, 1L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 1001L);
    }

    @Test
    public void testUnackWithWaitTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);

        workflowSweeper.unack(workflowModel, 1L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 46L);
    }

    @Test
    public void testUnackWithHumanTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);

        workflowSweeper.unack(workflowModel, 1L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 46L);
    }

    @Test
    public void testUnackWithScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        workflowSweeper.unack(workflowModel, 1L);

        TaskDef taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(30);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 77L);
        verify(taskDef).setPollTimeoutSeconds(31);
    }

    @Test
    public void testUnackWithNoTaskDefinition() {
        WorkflowModel workflowModel = new WorkflowModel();

        workflowSweeper.unack(workflowModel, 1L);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(30);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 71L);
        verify(workflowDefinition).setTimeoutSeconds(31);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long expectedJitter = new Random().nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1)) - (workflowOffsetTimeout / 3);

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - expectedJitter) && result <= (workflowOffsetTimeout + expectedJitter));
    }
}