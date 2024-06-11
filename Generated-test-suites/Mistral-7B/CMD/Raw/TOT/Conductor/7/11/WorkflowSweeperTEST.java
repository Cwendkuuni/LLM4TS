package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.decider.Decision;
import com.netflix.conductor.core.decider.WorkflowDecider;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.queue.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.scheduler.SchedulerConfiguration;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDefinition;
import com.netflix.conductor.utils.Utils;

public class WorkflowSweeperTest {

    private WorkflowSweeper sweeper;
    private WorkflowExecutor mockWorkflowExecutor;
    private QueueDAO mockQueueDAO;
    private WorkflowDecider mockWorkflowDecider;
    private ConductorProperties mockProperties;
    private WorkflowRepairService mockWorkflowRepairService;

    @Before
    public void setup() {
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockWorkflowDecider = mock(WorkflowDecider.class);
        mockProperties = mock(ConductorProperties.class);
        mockWorkflowRepairService = mock(WorkflowRepairService.class);

        sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);

        when(mockProperties.getAppId()).thenReturn("testAppId");
        when(mockProperties.getWorkflowOffsetTimeout()).thenReturn(5L);

        when(mockWorkflowExecutor.decide(anyString())).thenAnswer(invocation -> {
            String workflowId = invocation.getArgument(0);
            WorkflowModel workflowModel = new WorkflowModel();
            workflowModel.setWorkflowId(workflowId);
            workflowModel.setWorkflowName("testWorkflow");
            workflowModel.setStatus(WorkflowModel.Status.RUNNING);
            return workflowModel;
        });

        when(mockQueueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(mockQueueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);
    }

    @Test
    public void testSweep_WorkflowFoundRunning() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);
        String workflowId = "testWorkflowId";

        sweeper.sweep(workflowId);

        verify(mockQueueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowId),
                anyLong());
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);
        String workflowId = "testWorkflowId";

        doThrow(new NotFoundException("Workflow not found")).when(mockWorkflowExecutor)
                .decide(workflowId);

        sweeper.sweep(workflowId);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweep_WorkflowTerminal() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowName("testWorkflow");
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        when(mockWorkflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        sweeper.sweep(workflowId);

        verify(mockQueueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testUnack_TaskInProgress_NoWaitType() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        workflowModel.setTasks(new TaskModel[] {taskModel});

        long workflowOffsetTimeout = sweeper.workflowOffsetWithJitter(5L);

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(mockQueueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testUnack_TaskInProgress_WaitType() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Mockito.mock(TaskDef.class));
        taskModel.setTaskDefinition().setPollTimeoutSeconds(10L);
        workflowModel.setTasks(new TaskModel[] {taskModel});

        long workflowOffsetTimeout = sweeper.workflowOffsetWithJitter(5L);

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(mockQueueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testUnack_TaskScheduled_NoWaitType() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.setTasks(new TaskModel[] {taskModel});
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(10L);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        long workflowOffsetTimeout = sweeper.workflowOffsetWithJitter(5L);

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(mockQueueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testUnack_TaskScheduled_WorkflowTimeoutType() {
        WorkflowSweeper sweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setWorkflowId(workflowModel.getWorkflowId());
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        workflowModel.setTasks(new TaskModel[] {taskModel});
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(10L);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        long workflowOffsetTimeout = sweeper.workflowOffsetWithJitter(5L);

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(mockQueueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper();
        long workflowOffsetTimeout = sweeper.workflowOffsetWithJitter(5L);

        assertTrue(workflowOffsetTimeout >= 3L && workflowOffsetTimeout <= 8L);
    }
}