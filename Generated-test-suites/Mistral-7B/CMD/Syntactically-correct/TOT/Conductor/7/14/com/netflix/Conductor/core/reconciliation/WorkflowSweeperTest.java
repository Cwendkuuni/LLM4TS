package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.sweeper.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Autowired
    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testApp");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
    }

    @Test
    public void testSweep_workflowExistsAndTerminal() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        // When
        sweeper.sweep("workflowId");

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
    }

    @Test
    public void testSweep_workflowNotFound() {
        // Given

        // When
        sweeper.sweep("workflowId");

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, "workflowId");
        verify(LOGGER).info("Workflow NOT found for id:{}. Removed it from decider queue", "workflowId");
    }

    @Test
    public void testSweep_workflowProcessing() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        // When
        sweeper.sweep("workflowId");

        // Then
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", anyLong());
    }

    @Test
    public void testUnack() {
        // Given
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        // When
        sweeper.unack(workflowModel, 5L);

        // Then
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, "workflowId", 6L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter_positiveValue() {
        // Given
        long workflowOffsetTimeout = 5L;

        // When
        long workflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertTrue(workflowOffset >= (workflowOffsetTimeout - 2));
        assertTrue(workflowOffset <= (workflowOffsetTimeout + 2));
    }

    @Test
    public void testWorkflowOffsetWithJitter_zeroValue() {
        // Given
        long workflowOffsetTimeout = 0L;

        // When
        long workflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertEquals(workflowOffsetTimeout, workflowOffset);
    }

    @Test
    public void testAsyncSweep() {
        // Given
        doReturn(CompletableFuture.completedFuture(null)).when(sweeper)
                .sweepAsync("workflowId");

        // When
        sweeper.sweepAsync("workflowId").join();

        // Then
        verify(sweeper).sweep("workflowId");
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName("Test Workflow");
        workflowModel.setWorkflowId("workflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        List<TaskModel> tasks = Collections.singletonList(taskModel);
        workflowModel.setTasks(tasks);
        return workflowModel;
    }
}