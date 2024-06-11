package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.AnnotationAwareWorkflowContext;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.utils.Utils;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final Logger LOGGER = Mockito.mock(Logger.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("test-app");
        AnnotationAwareWorkflowContext context = new AnnotationAwareWorkflowContext();
        when(workflowExecutor.decide(ArgumentMatchers.anyString())).thenReturn(context);
        when(context.getWorkflowModel()).thenReturn(getWorkflowModel());
        when(workflowExecutor.getDeciderQueueDAO()).thenReturn(queueDAO);
        when(queueDAO.remove(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.longValue())).thenReturn(true);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        when(workflowRepairService).thenReturn(workflowRepairService);
        when(properties.getWorkflowOffsetTimeout()).thenReturn(TestConstants.DEFAULT_WORKFLOW_OFFSET_TIMEOUT);
        doNothing().when(LOGGER);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(properties, workflowExecutor, context, queueDAO, workflowRepairService);
    }

    @Test
    public void testSweeperAsync() {
        workflowSweeper.sweepAsync("testWorkflowId");
        verify(workflowSweeper).sweep("testWorkflowId");
    }

    @Test
    public void testSweeperUnack() {
        // Given
        long workflowOffsetTimeout = 30000L;
        WorkflowModel workflowModel = getWorkflowModel();

        // When
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        // Then
        String queueName = DECIDER_QUEUE;
        long expectedUnackTimeout = workflowOffsetTimeout * 1000 + 3000; // 3 seconds jitter
        verify(queueDAO).setUnackTimeout(queueName, workflowModel.getWorkflowId(), expectedUnackTimeout);
    }

    @Test
    public void testSweeperNoWorkflowFound() {
        // Given
        String workflowId = "nonExistentWorkflowId";
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor)
                .decide(workflowId);

        // When
        workflowSweeper.sweep("nonExistentWorkflowId");

        // Then
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(LOGGER).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweeperWithRepairService() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(tasks);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor).decide(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweeperWithoutRepairService() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getWorkflowModel();

        when(workflowRepairService).thenReturn(null);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @VisibleForTesting
    static WorkflowModel getWorkflowModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        TaskDef taskDefinition = new TaskDef();
        taskModel.setTaskDefinition(Optional.of(taskDefinition));
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));
        return workflowModel;
    }

    static class TestConstants {
        static final long DEFAULT_WORKFLOW_OFFSET_TIMEOUT = 120000L;
    }
}