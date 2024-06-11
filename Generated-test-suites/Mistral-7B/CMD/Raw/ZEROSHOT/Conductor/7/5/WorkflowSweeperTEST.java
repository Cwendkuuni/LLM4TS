package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper.WorkflowOffsetWithJitterFunction;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

@Configuration
public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String APPLICATION_ID = "test-application";
    private WorkflowSweeper workflowSweeper;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowOffsetWithJitterFunction workflowOffsetWithJitterFunction;

    @Before
    public void setUp() {
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowOffsetWithJitterFunction = mock(WorkflowOffsetWithJitterFunction.class);
        when(conductorProperties.getAppId()).thenReturn(APPLICATION_ID);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(workflowOffsetWithJitterFunction.workflowOffsetWithJitter(10L)).thenCallRealMethod();

        WorkflowContext workflowContext = new WorkflowContext(APPLICATION_ID);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Mockito.optional(null),
                conductorProperties,
                queueDAO,
                monitors,
                workflowOffsetWithJitterFunction);
    }

    @After
    public void tearDown() {
        workflowSweeper = null;
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.join();
        verifyNoTasks();
        verifyNoWorkflow();
        verifyNoWorkflowInQueue();
        verifyNoUnackTimeout();
    }

    @Test
    public void testSweepWithWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);
        verifyTasksInWorkflow();
        verifyWorkflow();
        verifyWorkflowInQueue();
        verifyUnackTimeout();
    }

    @Test
    public void testSweepWithoutWorkflow() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(WORKFLOW_ID);
        verifyNoTasks();
        verifyNoWorkflow();
        verifyNoWorkflowInQueue();
        verifyNoUnackTimeout();
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 12L);
        verifyQueuedUnackTimeout();
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10L;
        long expectedJitter = 3L;
        when(workflowOffsetWithJitterFunction.workflowOffsetWithJitter(workflowOffsetTimeout)).thenReturn(13L);

        long actualJitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(expectedJitter, actualJitter);
    }

    private void verifyNoTasks() {
        assertTrue(workflowSweeper.tasksAreEmpty());
    }

    private void verifyTasksInWorkflow() {
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(new TaskModel());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().setTasks(tasks));

        workflowSweeper.sweep(WORKFLOW_ID);
        assertTrue(workflowSweeper.tasksAreEmpty());
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    private void verifyNoWorkflow() {
        assertNull(workflowSweeper.getWorkflow());
    }

    private void verifyWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);
        assertEquals(workflowModel, workflowSweeper.getWorkflow());
    }

    private void verifyNoWorkflowInQueue() {
        assertFalse(queueDAO.contains(DECIDER_QUEUE, WORKFLOW_ID));
    }

    private void verifyWorkflowInQueue() {
        workflowSweeper.sweep(WORKFLOW_ID);
        assertTrue(queueDAO.contains(DECIDER_QUEUE, WORKFLOW_ID));
    }

    private void verifyNoUnackTimeout() {
        assertFalse(queueDAO.hasUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID));
    }

    private void verifyQueuedUnackTimeout() {
        long unackTimeout = 12L;
        workflowSweeper.unack(new WorkflowModel(), unackTimeout);
        assertTrue(queueDAO.hasUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID));
    }

    private void verifyUnackTimeout() {
        long workflowOffsetTimeout = 10L;
        long unackTimeout = 12L;
        workflowSweeper.sweep(WORKFLOW_ID);
        assertEquals(unackTimeout, queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID) / 1000);
    }

    private void verify(QueueDAO queueDAO) {
        Mockito.verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    private void assertEquals(long expected, long actual) {
        assertTrue(expected == actual);
    }
}

@Configuration
public class TestConfiguration {

    @Bean
    public Monitors monitors() {
        return mock(Monitors.class);
    }

    @Bean
    public WorkflowOffsetWithJitterFunction workflowOffsetWithJitterFunction() {
        return mock(WorkflowOffsetWithJitterFunction.class);
    }
}