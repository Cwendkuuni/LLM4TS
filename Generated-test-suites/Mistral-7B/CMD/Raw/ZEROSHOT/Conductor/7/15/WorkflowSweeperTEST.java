package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.testutils.TestUtils;

@Component
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getSchedulerConfiguration().getSweeperExecutorName()).thenReturn("sweeperExecutor");
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = TestUtils.getTestWorkflowModel();
        when(queueDAO.remove(DECIDER_QUEUE, eq(workflowId))).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, eq(workflowId), anyLong()))
                .thenReturn(true);
        when(workflowExecutor.decide(eq(workflowId))).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
        verify(workflowExecutor, times(1)).decide(workflowId);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        WorkflowModel failedWorkflowModel = TestUtils.getTestWorkflowModelWithStatus(Status.FAILED);
        when(queueDAO.remove(DECIDER_QUEUE, eq(workflowId))).thenReturn(false);
        when(workflowExecutor.decide(eq(workflowId))).thenReturn(failedWorkflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);

        when(workflowModel.getStatus()).thenReturn(Status.TERMINAL);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        long workflowOffsetTimeout = 5L;
        WorkflowModel workflowModel = TestUtils.getTestWorkflowModel();
        long startTime = Instant.now().toEpochMilli() - 1000;
        long endTime = Instant.now().toEpochMilli();

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));

        when(workflowModel.getTasks()).thenReturn(TestUtils.getTestTaskModels());

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        TaskModel inProgressTaskModel = TestUtils.getTestTaskModelWithStatus(Status.IN_PROGRESS);
        TaskModel scheduledTaskModel = TestUtils.getTestTaskModelWithStatus(Status.SCHEDULED);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(inProgressTaskModel, scheduledTaskModel));

        long postponeDurationSeconds = 10L;
        when(inProgressTaskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(inProgressTaskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(inProgressTaskModel.getWaitTimeout()).thenReturn(5L);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                eq(postponeDurationSeconds * 1000));

        when(inProgressTaskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                eq(postponeDurationSeconds * 1000 + workflowOffsetTimeout * 1000));

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(scheduledTaskModel, inProgressTaskModel));

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        when(scheduledTaskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(scheduledTaskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(scheduledTaskModel.getWaitTimeout()).thenReturn(10L);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        when(scheduledTaskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(scheduledTaskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                eq(scheduledTaskModel.getWaitTimeout() * 1000 + workflowOffsetTimeout * 1000));

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(scheduledTaskModel, inProgressTaskModel));

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        when(workflowModel.getWorkflowDefinition()).thenReturn(TestUtils.getTestWorkflowDefinition());

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(),
                eq(workflowModel.getWorkflowDefinition().getTimeoutSeconds() * 1000 + workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long actualWorkflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(actualWorkflowOffset > (workflowOffsetTimeout - 15L));
        assertTrue(actualWorkflowOffset < (workflowOffsetTimeout + 15L));
    }
}