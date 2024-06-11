package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.workflow.WorkflowTestUtils;

public class WorkflowSweeperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setUp() {
        when(properties.getAppId()).thenReturn("testAppId");
    }

    @After
    public void cleanUp() {
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(workflowRepairService);
    }

    @Test
    public void testSweepWithNotFoundWorkflow() {
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId1")).thenReturn(Optional.empty());
        sweeper.sweep("workflowId1");
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId1");
        assertNull(sweeper.workflow);
    }

    @Test
    public void testSweepWithTerminalWorkflow() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId2");
        workflowModel.setStatus(Status.FAILED);
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId2")).thenReturn(Optional.of(workflowModel));
        sweeper.sweeper = mock(WorkflowSweeper.class);
        doReturn(new Random()).when(sweeper).new Random();

        sweeper.sweep("workflowId2");
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId2");
        assertNull(sweeper.workflow);
    }

    @Test
    public void testSweepWithRunningWorkflow() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId3");
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId3")).thenReturn(Optional.of(workflowModel));

        sweeper.sweep("workflowId3");

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());
        assertSame(workflowModel, sweeper.workflow);
    }

    @Test
    public void testSweepWithEmptyWorkflow() {
        WorkflowModel workflowModelEmpty = new WorkflowModel();
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId4")).thenReturn(Optional.of(workflowModelEmpty));

        sweeper.sweep("workflowId4");

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModelEmpty.getWorkflowId(), anyLong());
        assertNull(sweeper.workflow);
    }

    @Test
    public void testSweepWithVerifiedAndRepairedWorkflow() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId5");
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId5")).thenReturn(Optional.of(workflowModel));
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowModel.getWorkflowId())).thenReturn(Collections.emptyList());

        sweeper.sweep("workflowId5");

        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId5");
        assertNull(sweeper.workflow);
    }

    @Test
    public void testSweepWithUnacknowledgedTask() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId6");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel("task1", "Task1", TaskType.TASK_TYPE_BATCH, Status.IN_PROGRESS),
                new TaskModel("task2", "Task2", TaskType.TASK_TYPE_HUMAN, Status.SCHEDULED),
                new TaskModel("task3", "Task3", TaskType.TASK_TYPE_SCRIPT, Status.FAILED)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId6")).thenReturn(Optional.of(workflowModel));

        sweeper.sweep("workflowId6");

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());
        verify(queueDAO, atLeastOnce()).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 1000L);
        assertSame(workflowModel, sweeper.workflow);
    }

    @Test
    public void testSweepWithTaskInProgress() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId7");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel("task1", "Task1", TaskType.TASK_TYPE_BATCH, Status.IN_PROGRESS),
                new TaskModel("task2", "Task2", TaskType.TASK_TYPE_HUMAN, Status.SCHEDULED),
                new TaskModel("task3", "Task3", TaskType.TASK_TYPE_SCRIPT, Status.FAILED)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId7")).thenReturn(Optional.of(workflowModel));

        sweeper.sweep("workflowId7");

        verify(queueDAO, atLeastOnce()).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 46001L);
        assertFalse(queueDAO.setUnackTimeout.called(3));
        assertSame(workflowModel, sweeper.workflow);
    }

    @Test
    public void testSweepWithTaskScheduled() {
        WorkflowModel workflowModel = WorkflowTestUtils.createValidWorkflowModel("workflowId8");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel("task1", "Task1", TaskType.TASK_TYPE_BATCH, Status.IN_PROGRESS),
                new TaskModel("task2", "Task2", TaskType.TASK_TYPE_HUMAN, Status.SCHEDULED),
                new TaskModel("task3", "Task3", TaskType.TASK_TYPE_SCRIPT, Status.FAILED)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.findById(DECIDER_QUEUE, "workflowId8")).thenReturn(Optional.of(workflowModel));

        sweeper.sweep("workflowId8");

        verify(queueDAO, atLeastOnce()).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), 46001L);
        assertFalse(queueDAO.setUnackTimeout.called(3));
        assertSame(workflowModel, sweeper.workflow);
    }
}