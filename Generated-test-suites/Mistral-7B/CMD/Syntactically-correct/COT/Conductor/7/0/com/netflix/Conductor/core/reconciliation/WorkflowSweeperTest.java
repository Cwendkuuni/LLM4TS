package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.ReconciliationTestUtils;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

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

    @AutowiredAnnotatedField("workflowRepairService")
    @InjectMocks
    private WorkflowSweeper sweeper;
    @AutowiredAnnotatedField("queueDAO")
    private QueueDAO queueDAOMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(workflowRepairService);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        sweeper.sweepAsync(workflowId).join();
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAOMock).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithNotFoundException() {
        doThrow(new NotFoundException("Test exception")).when(workflowExecutor).decide(anyString());
        String workflowId = "testWorkflowId";
        sweeper.sweep(workflowId);
        verify(queueDAOMock).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        doThrow(new RuntimeException("Test exception")).when(workflowExecutor).decide(anyString());
        sweeper.sweep(workflowId);
        verify(queueDAOMock).setUnackTimeout(anyString(), eq(workflowId), anyLong());
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = ReconciliationTestUtils.createDefaultWorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        doReturn(workflowModel).when(workflowRepairService).getWorkflowModel(workflowId);
        sweeper.verifyAndRepairWorkflowTasks(workflowId);
        verify(workflowRepairService, times(1)).getWorkflowModel(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = ReconciliationTestUtils.createDefaultWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel("task1", Status.IN_PROGRESS, null),
                new TaskModel("task2", Status.SCHEDULED, null),
                new TaskModel("task3", Status.ERROR, null)
        );
        workflowModel.setTasks(tasks);

        sweeper.unack(workflowModel, 5L);
        verify(queueDAOMock, times(1)).setUnackTimeout(
                anyString(), eq(workflowModel.getWorkflowId()), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long jitter = 10L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= (workflowOffsetTimeout - jitter));
        assertTrue(result <= (workflowOffsetTimeout + jitter));
    }
}