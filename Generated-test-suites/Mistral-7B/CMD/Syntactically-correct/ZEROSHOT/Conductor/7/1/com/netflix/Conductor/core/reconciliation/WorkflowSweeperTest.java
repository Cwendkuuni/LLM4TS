package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
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
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(properties, workflowExecutor, workflowRepairService, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflow-1";

        when(workflowExecutor.decide(workflowId)).thenReturn(new WorkflowModel());

        doNothing().when(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong()))
                .thenReturn(true);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertTrue(result.get());

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verifyNoInteractions(workflowRepairService);
    }

    @Test
    public void testSweepAsyncWithNotFoundException() throws Exception {
        String workflowId = "workflow-1";

        doThrow(new NotFoundException("Workflow not found"))
                .when(workflowExecutor).decide(workflowId);

        doNothing().when(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong()))
                .thenReturn(true);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertTrue(result.get());

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verifyNoInteractions(workflowRepairService);
        verify(logger).info("Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweepAsyncWithException() throws Exception {
        String workflowId = "workflow-1";

        doThrow(new Exception("Error running sweep for " + workflowId))
                .when(workflowSweeper)
                .sweep(workflowId);

        doNothing().when(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong()))
                .thenReturn(true);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertTrue(result.get());

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verify(monitors).error(eq(CLASS_NAME), eq("sweep"));
        verify(logger).error("Error running sweep for " + workflowId, eq(Exception.class));
    }

    @Test
    public void testSweepWithWorkflowModel() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        doNothing().when(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong()))
                .thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "workflow-1";
        WorkflowModel workflowModel = new WorkflowModel();
        List<TaskModel> tasks = Collections.singletonList(new TaskModel());
        workflowModel.setTasks(tasks);

        when(queueDAO.setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId), anyLong()))
                .thenReturn(true);

        workflowSweeper.unack(workflowModel, 45L);

        verify(queueDAO).setUnackTimeout(eq(DECIDER_QUEQUE), eq(workflowId), eq(Long.parseLong("46")) * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - 15) && result <= (workflowOffsetTimeout + 15));
    }
}