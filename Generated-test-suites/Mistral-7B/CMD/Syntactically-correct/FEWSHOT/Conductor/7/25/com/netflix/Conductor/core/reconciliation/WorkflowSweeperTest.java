package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
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
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.WorkflowOffset;
import com.netflix.conductor.testutils.TestUtils;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper sweeperUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(120L);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException());
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = sweeperUnderTest.sweepAsync("workflowId");
        assertTrue(result.isCompleted());
        assertNull(result.getNow(Throwable.class));
    }

    @Test
    public void testSweepAsyncWithException() {
        when(workflowExecutor.decide(anyString())).thenThrow(new RuntimeException());
        CompletableFuture<Void> result = sweeperUnderTest.sweepAsync("workflowId");
        assertTrue(result.isCompletedExceptionally());
        assertSame(RuntimeException.class, result.getNow(Throwable.class).getClass());
    }

    @Test
    public void testSweep() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);
        when(workflowExecutor.decide(anyString())).thenReturn(workflow);
        sweeperUnderTest.sweep("workflowId");
        verifyNoMoreInteractions(queueDAO);
        verify(monitors).recordUnackTime(anyString(), anyLong());

        WorkflowModel workflowNotFound = null;
        when(workflowExecutor.decide(anyString())).thenReturn(workflowNotFound);
        sweeperUnderTest.sweep("workflowId");
        verify(queueDAO).remove(anyString(), "workflowId");
        verify(monitors).error(anyString(), anyString());
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), anyLong());
    }

    @Test
    public void testUnackWithNullWorkflowModel() {
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(null, 123L);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testUnackWithNullWorkflowId() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testUnackWithNoTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testUnackWithAllTasksInProgress() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task1").setStatus(Status.IN_PROGRESS),
                new TaskModel().setTaskId("task2").setStatus(Status.IN_PROGRESS)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), anyLong());
    }

    @Test
    public void testUnackWithNoTasksInProgress() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task1").setStatus(Status.COMPLETED),
                new TaskModel().setTaskId("task2").setStatus(Status.COMPLETED)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testUnackWithTimeoutSetOnFirstTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task1").setStatus(Status.IN_PROGRESS).setWaitTimeout(10L),
                new TaskModel().setTaskId("task2").setStatus(Status.COMPLETED)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), 21L);
    }

    @Test
    public void testUnackWithTimeoutSetOnLastTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task1").setStatus(Status.COMPLETED),
                new TaskModel().setTaskId("task2").setStatus(Status.IN_PROGRESS).setWaitTimeout(10L)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), 11L);
    }

    @Test
    public void testUnackWithTimeoutSetOnTaskWithResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task1").setStatus(Status.COMPLETED),
                new TaskModel().setTaskId("task2").setStatus(Status.IN_PROGRESS).setResponseTimeoutSeconds(10L)
        );
        workflowModel.setTasks(tasks);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        sweeperUnderTest.unack(workflowModel, 123L);
        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"), 21L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        Random random = new Random();
        long workflowOffsetTimeout = 120L;

        long result = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= (workflowOffsetTimeout - 39L) && result <= (workflowOffsetTimeout + 39L));
    }
}