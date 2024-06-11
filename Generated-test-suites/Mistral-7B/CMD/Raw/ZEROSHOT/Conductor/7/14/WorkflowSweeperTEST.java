package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
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

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.util.Utils;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.testutils.TestWorkflowExecutor;
import com.netflix.conductor.testutils.TestWorkflowModel;
import com.netflix.conductor.testutils.TestWorkflowModel.TaskModelBuilder;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private TaskDef taskDef;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getWorkflowSweeperExecutionTimeout()).thenReturn(5L);
        when(properties.getSweeperExecutorName()).thenReturn("executorName");

        TestWorkflowExecutor testWorkflowExecutor = new TestWorkflowExecutor();
        when(workflowExecutor).thenReturn(testWorkflowExecutor);

        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenAnswer(invocation -> {
            String workflowId = invocation.getArgument(0);
            LOGGER.info("Removed workflow with id {} from decider queue", workflowId);
        });

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong())).thenAnswer(invocation -> {
            String workflowId = invocation.getArgument(0);
            long timeout = invocation.getArgument(1);
            LOGGER.info("Set unack timeout for workflow {} to {}", workflowId, timeout);
        });

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, anyString())).thenAnswer(invocation -> {
            String workflowId = invocation.getArgument(0);
            return Optional.of((int) (new Random().nextInt(10) + 1) * 1000);
        });

        when(workflowRepairService.verifyAndRepairWorkflowTasks(anyString())).thenReturn(true);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflowId";
        sweeper.sweepAsync(workflowId).get();
        verify(workflowExecutor).decide(workflowId);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "workflowId";

        WorkflowModel workflowModel = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        TaskModel taskModel = new TaskModelBuilder()
                .withName("taskName")
                .withTaskType(TaskType.TASK_TYPE_WAIT)
                .withStatus(Status.IN_PROGRESS)
                .withWaitTimeout(10L)
                .build();

        WorkflowModel workflowModelWithTask = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .withTasks(List.of(taskModel))
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModelWithTask);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        TaskModel taskModelWithResponseTimeout = new TaskModelBuilder()
                .withName("taskName")
                .withTaskType(TaskType.TASK_TYPE_WAIT)
                .withStatus(Status.IN_PROGRESS)
                .withResponseTimeoutSeconds(10)
                .build();

        WorkflowModel workflowModelWithTaskWithResponseTimeout = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .withTasks(List.of(taskModelWithResponseTimeout))
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModelWithTaskWithResponseTimeout);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        TaskModel taskModelWithNoResponseTimeout = new TaskModelBuilder()
                .withName("taskName")
                .withTaskType(TaskType.TASK_TYPE_WAIT)
                .withStatus(Status.IN_PROGRESS)
                .build();

        WorkflowModel workflowModelWithTaskWithNoResponseTimeout = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .withTasks(List.of(taskModelWithNoResponseTimeout))
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModelWithTaskWithNoResponseTimeout);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        WorkflowModel workflowModelWithTerminalStatus = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.FAILED)
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModelWithTerminalStatus);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        WorkflowModel workflowModelWithNotFoundStatus = null;

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException(""));

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);

        WorkflowModel workflowModelWithTasks = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .withTasks(List.of(
                        new TaskModelBuilder()
                                .withName("taskName")
                                .withTaskType(TaskType.TASK_TYPE_WAIT)
                                .withStatus(Status.IN_PROGRESS)
                                .build(),
                        new TaskModelBuilder()
                                .withName("taskName2")
                                .withTaskType(TaskType.TASK_TYPE_HUMAN)
                                .withStatus(Status.IN_PROGRESS)
                                .build()))
                .build();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModelWithTasks);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(false);

        sweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = new TestWorkflowModel.Builder()
                .withName("workflowName")
                .withStatus(Status.RUNNING)
                .build();

        sweeper.unack(workflowModel, 5L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, 5 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 5L;

        assertEquals(5L, sweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
        assertEquals(5L, sweeper.workflowOffsetWithJitter(workflowOffsetTimeout));

        Random random = new Random();
        long range = workflowOffsetTimeout / 3;
        long jitter = random.nextInt((int) (2 * range + 1)) - range;
        long expected = workflowOffsetTimeout + jitter;

        assertEquals(expected, sweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
    }
}