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
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
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

    @InjectMocks
    private WorkflowSweeper sweeper;

    @Configuration
    static class TestConfig {
        @Bean
        public Monitors monitors() {
            return mock(Monitors.class);
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweeperAsync() throws Exception {
        String workflowId = "test-workflow-id";
        sweeper.sweepAsync(workflowId);
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
    }

    @Test
    public void testSweeper() throws Exception {
        String workflowId = "test-workflow-id";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setWorkflowName("test-workflow");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(queueDAO.getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(workflow);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(monitors, times(1)).recordUnackTime(eq("test-workflow"), anyLong());
    }

    @Test
    public void testSweeperNotFound() throws Exception {
        String workflowId = "test-workflow-id";

        doThrow(new NotFoundException("Workflow not found")).when(queueDAO).getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId));

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any(Throwable.class));
        verify(log, times(1)).info("Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweeperException() throws Exception {
        String workflowId = "test-workflow-id";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setWorkflowName("test-workflow");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        when(queueDAO.getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(workflow);

        doThrow(new Exception("Test exception")).when(workflowExecutor).decide(workflowId);

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any(Throwable.class));
        verify(log, times(1)).error("Error running sweep for " + workflowId, any(Throwable.class));
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "test-workflow-id";
        long workflowOffsetTimeout = 1000L;
        long jitter = 500L;

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));
        workflowModel.setWorkflowId(workflowId);

        when(queueDAO.getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId))).thenReturn(workflowModel);

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        long unackTimeout = workflowOffsetTimeout + jitter;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(unackTimeout * 1000L));
    }

    @Test
    public void testUnackWorkflowNotFound() throws Exception {
        String workflowId = "test-workflow-id";

        doThrow(new NotFoundException("Workflow not found")).when(queueDAO).getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId));

        sweeper.unack(null, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(workflowOffsetTimeout * 1000L));
        verify(log, times(1)).warn("Workflow with {} id can not be found. Attempting to unack using the id", workflowId);
    }

    @Test
    public void testUnackException() throws Exception {
        String workflowId = "test-workflow-id";
        long workflowOffsetTimeout = 1000L;

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);

        doThrow(new Exception("Test exception")).when(queueDAO).getWorkflowModel(eq(DECIDER_QUEUE), eq(workflowId));

        sweeper.unack(workflowModel, workflowOffsetTimeout);

        long unackTimeout = workflowOffsetTimeout * 1000L;
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), eq(unackTimeout));
        verify(log, times(1)).warn("Workflow with {} id can not be found. Attempting to unack using the id", workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 1000L;

        long workflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffsetWithJitter > workflowOffsetTimeout - 333);
        assertTrue(workflowOffsetWithJitter < workflowOffsetTimeout + 333);
    }

    @After
    public void cleanup() throws Exception {
        verifyNoMoreInteractions(properties, workflowExecutor, queueDAO, monitors);
    }
}