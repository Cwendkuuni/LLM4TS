package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.testutils.ConductorTestUtils;

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
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, "workflowId1")).thenReturn(0L);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, "workflowId1", 1000L)).thenReturn(true);

        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId1");
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setStatus(Status.IN_PROGRESS);
        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("taskId1");
        taskModel1.setTaskDefinition(new TaskDef());
        taskModel1.setStatus(Status.IN_PROGRESS);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel1));

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowModel.getWorkflowId())).thenReturn(null);
    }

    @After
    public void cleanup() {
        // Cleanup mocks and stubs
    }

    @Test
    public void testSweepAsync_NoWorkflowFound() {
        when(queueDAO.get(DECIDER_QUEUE, "workflowId2")).thenReturn(null);
        sweeper.sweepAsync("workflowId2");
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId2");
    }

    @Test
    public void testSweepAsync_WorkflowNotFoundException() {
        when(queueDAO.get(DECIDER_QUEUE, "workflowId3"))
                .thenThrow(new NotFoundException("Workflow not found"));
        sweeper.sweepAsync("workflowId3");
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId3");
    }

    @Test
    public void testSweepAsync_UnackWorkflow() throws InterruptedException, ExecutionException {
        Instant startTime = Instant.now();
        long workflowOffsetTimeout = 5L;
        long postponeDurationSeconds = 6L;

        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(new TaskModel()));

        sweeper.sweep("workflowId4");

        // Unack the workflow
        unack(workflowModel, workflowOffsetTimeout);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
        verify(monitors).recordUnackTime(workflowModel.getWorkflowName(), (int) ((Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000));
    }

    @Test
    public void testUnack_WorkflowNotFound() {
        WorkflowModel workflowModel = null;
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong());
    }

    @Test
    public void testUnack_NoTaskToPostpone() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId5");
        workflowModel.setTasks(Arrays.asList(new TaskModel()));
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong());
    }

    @Test
    public void testUnack_TaskWithWaitType() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId6");
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId2");
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponeDurationSeconds = 11L;
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_TaskWithHumanType() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId7");
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId3");
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponeDurationSeconds = 12L;
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_TaskWithResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId8");
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId4");
        taskModel.setTaskDefinition(new TaskDef());
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setResponseTimeoutSeconds(13L);
        workflowModel.setTasks(Arrays.asList(taskModel));

        long postponeDurationSeconds = 14L;
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_WorkflowWithTimeout() {
        WorkflowModel workflowModel = new WorkflowModelImpl();
        workflowModel.setWorkflowId("workflowId9");
        workflowModel.setTasks(Arrays.asList(new TaskModel()));
        when(workflowModel.getWorkflowDefinition()).thenReturn(new TaskDef());
        workflowModel.getWorkflowDefinition().setTimeoutSeconds(15L);

        long postponeDurationSeconds = 16L;
        sweeper.unack(workflowModel, 10L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    private void unack(WorkflowModel workflowModel, long workflowOffsetTimeout) {
        sweeper.unack(workflowModel, workflowOffsetTimeout);
    }

}