package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor.Decision;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private TaskDef taskDef;

    @Mock
    private WorkflowContext workflowContext;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(workflowExecutor.decide(anyString())).thenAnswer(invocation -> {
            String workflowId = invocation.getArgument(0);
            if (workflowId.equals("workflow1")) {
                return new Decision(workflowModel, Arrays.asList(taskModel));
            } else if (workflowId.equals("workflow2")) {
                return new Decision(workflowModel, Arrays.asList(taskModel, taskModel));
            } else {
                throw new NotFoundException("Workflow " + workflowId + " not found");
            }
        });
        when(workflowModel.getWorkflowId()).thenReturn("workflow1");
        when(workflowModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(workflowModel.getWorkflowDefinition()).thenReturn(
                new WorkflowDefinition("workflowDefinition", 10L));
        when(taskModel.getTaskId()).thenReturn("task1");
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_NORMAL);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(3L);
        when(taskModel.getWaitTimeout()).thenReturn(4L);
        when(taskDef.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(taskDef.getPollTimeoutSeconds()).thenReturn(2L);
    }

    @Test
    public void testSweep() {
        when(queueDAO.get(DECIDER_QUEUE, "workflow1")).thenReturn(Optional.of(workflowModel));
        when(workflowRepairService.verifyAndRepairWorkflowTasks("workflow1")).thenReturn(false);

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.sweep("workflow1");

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflow1", 7L * 1000);
        verifyZeroInteractions(queueDAO, workflowExecutor, workflowRepairService, LOGGER);
    }

    @Test
    public void testSweepNotFound() {
        when(queueDAO.get(DECIDER_QUEUE, "workflow2")).thenReturn(Optional.empty());

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.sweep("workflow2");

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflow2", 5L * 1000);
        verifyZeroInteractions(queueDAO, workflowExecutor, workflowRepairService, LOGGER);
    }

    @Test
    public void testSweepWorkflowRepaired() {
        when(queueDAO.get(DECIDER_QUEUE, "workflow1")).thenReturn(Optional.of(workflowModel));
        when(workflowRepairService.verifyAndRepairWorkflowTasks("workflow1")).thenReturn(true);

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.sweep("workflow1");

        verifyZeroInteractions(queueDAO, workflowExecutor, workflowRepairService, LOGGER);
    }

    @Test
    @Async("sweeperExecutorName")
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflow1");
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflow1", 7L * 1000);
        result.join();
        verifyZeroInteractions(queueDAO, workflowExecutor, workflowRepairService, LOGGER);
    }

    @Test
    public void testUnack() {
        when(queueDAO.get(DECIDER_QUEUE, "workflow1")).thenReturn(Optional.of(workflowModel));

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflow1", 5L * 1000);
    }

    @Test
    public void testUnackFail() {
        when(queueDAO.get(DECIDER_QUEUE, "workflow1")).thenReturn(Optional.empty());

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflow1", 5L * 1000);
        verify(queueDAO).remove(DECIDER_QUEUE, "workflow1");
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;
        long workflowOffsetWithJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffsetWithJitter >= 3L && workflowOffsetWithJitter <= 9L);
    }
}