package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.QueueDAO;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(properties, workflowExecutor, queueDAO, monitors);
    }

    @Test
    public void testSweepWithValidWorkflowId() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO, times(1)).remove(anyString(), workflowId);
        if (workflowModel.getStatus().isTerminal()) {
            verifyNoMoreInteractions(queueDAO);
        } else {
            verify(queueDAO, times(1)).setUnackTimeout(anyString(), workflowId, anyLong());
        }
    }

    @Test
    public void testSweepWithNotFoundWorkflowId() {
        // Given
        String workflowId = "test-not-found-workflow-id";

        // When
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO, times(1)).remove(anyString(), workflowId);
    }

    @Test
    public void testSweepWithException() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException());

        // When
        sweeper.sweep(workflowId);

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(
                anyString(), workflowId, workflowOffsetWithJitter(5L) * 1000);
    }

    @Test
    public void testSweepWithExistingUnackTimeout() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        long workflowOffsetTimeout = 10L;
        long existingUnackTimeout = 100L;

        // When
        sweeper.sweep(workflowId);

        // Then
        ArgumentCaptor<Long> unackTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), workflowId, unackTimeoutCaptor.capture());
        long newUnackTimeout = unackTimeoutCaptor.getValue();
        assertEquals(workflowOffsetTimeout + 1, newUnackTimeout);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testUnackWithTaskInProgress() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModelWithTaskInProgress();

        // When
        sweeper.unack(workflowModel, workflowOffsetWithJitter(5L));

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(
                anyString(), workflowId, (5L + (new Random().nextInt(11) - 5) * 1000));
    }

    @Test
    public void testUnackWithTaskScheduled() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModelWithTaskScheduled();

        // When
        sweeper.unack(workflowModel, workflowOffsetWithJitter(5L));

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(
                anyString(), workflowId, (5L + (new Random().nextInt(11) - 5) * 1000));
    }

    @Test
    public void testUnackWithNoTasks() {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = createWorkflowModel();

        // When
        sweeper.unack(workflowModel, workflowOffsetWithJitter(5L));

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(
                anyString(), workflowId, (5L + (new Random().nextInt(11) - 5) * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Given
        long workflowOffsetTimeout = 45L;

        // When
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertTrue(result >= (workflowOffsetTimeout - 15) && result <= (workflowOffsetTimeout + 15));
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel();
    }

    private WorkflowModel createWorkflowModelWithTaskInProgress() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        return workflowModel;
    }

    private WorkflowModel createWorkflowModelWithTaskScheduled() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.setTasks(Collections.singletonList(taskModel));
        return workflowModel;
    }

    private long workflowOffsetWithJitter(long workflowOffsetTimeout) {
        return sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
    }
}