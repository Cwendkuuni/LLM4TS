package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor mockWorkflowExecutor;
    private WorkflowRepairService mockWorkflowRepairService;
    private QueueDAO mockQueueDAO;
    private ConductorProperties mockProperties;
    private Monitors mockMonitors;

    @Before
    public void setUp() {
        mockWorkflowExecutor = mock(WorkflowExecutor.class);
        mockWorkflowRepairService = mock(WorkflowRepairService.class);
        mockQueueDAO = mock(QueueDAO.class);
        mockProperties = mock(ConductorProperties.class);
        mockMonitors = mock(Monitors.class);

        when(mockProperties.getAppId()).thenReturn("testAppId");
        when(mockProperties.getWorkflowOffsetTimeout()).thenReturn(30L);

        workflowSweeper = new WorkflowSweeper(
                mockWorkflowExecutor,
                Optional.of(mockWorkflowRepairService),
                mockProperties,
                mockQueueDAO);
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync("testWorkflowId");
        verify(mockWorkflowExecutor, times(1)).decide("testWorkflowId");
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setWorkflowId("testWorkflowId");

        when(mockQueueDAO.get(DECIDER_QUEUE, "testWorkflowId")).thenReturn(workflowModel);
        when(mockWorkflowExecutor.decide("testWorkflowId")).thenReturn(workflowModel);

        workflowSweeper.sweep("testWorkflowId");

        ArgumentCaptor<WorkflowModel> workflowModelCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        verify(mockQueueDAO, times(1)).remove(DECIDER_QUEUE, "testWorkflowId");
        verify(mockWorkflowExecutor, times(1)).decide("testWorkflowId");
        verify(mockQueueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, "testWorkflowId", anyLong());
    }

    @Test
    public void testSweepNotFoundException() {
        when(mockQueueDAO.get(DECIDER_QUEUE, "testWorkflowId")).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep("testWorkflowId");

        verify(mockQueueDAO, times(1)).remove(DECIDER_QUEUE, "testWorkflowId");
        verify(mockLogger, times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", "testWorkflowId", isA(NotFoundException.class));
    }

    @Test
    public void testSweepException() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setWorkflowId("testWorkflowId");

        when(mockQueueDAO.get(DECIDER_QUEUE, "testWorkflowId")).thenReturn(workflowModel);
        when(mockWorkflowExecutor.decide("testWorkflowId")).thenThrow(new RuntimeException("Exception during workflow execution"));

        workflowSweeper.sweep("testWorkflowId");

        verify(mockQueueDAO, times(1)).remove(DECIDER_QUEUE, "testWorkflowId");
        verify(mockWorkflowExecutor, times(1)).decide("testWorkflowId");
        verify(mockMonitors, times(1)).error(
                "Error running sweep for testWorkflowId", isA(RuntimeException.class));
        verify(mockLogger, times(1)).error("Error running sweep for testWorkflowId", isA(RuntimeException.class));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setWorkflowId("testWorkflowId");

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskName("testTaskName");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskDefinition(Optional.of(new TaskDef()));

        workflowModel.setTasks(Collections.singletonList(taskModel));

        when(mockQueueDAO.get(DECIDER_QUEUE, "testWorkflowId")).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 30L);

        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(mockQueueDAO, times(1)).setUnackTimeout(queueNameCaptor.capture(), timeoutCaptor.capture(), anyLong());
        assertEquals("DECIDER_QUEUE", queueNameCaptor.getValue());
        assertEquals(30L * 1000, timeoutCaptor.getValue());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long returnedValue = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(returnedValue >= 30L && returnedValue <= 60L);
    }
}