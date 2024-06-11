package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.utils.Utils;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
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
    private WorkflowSweeper workflowSweeper;

    @VisibleForTesting
    private static final String DECIDER_QUEUE = "deciderQueue";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
    }

    @Test
    public void testSweepAsync() throws InterruptedException {
        String workflowId = "workflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(0L);

        doNothing().when(workflowSweeper).sweep(workflowId);
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweepAsyncConcurrent() throws InterruptedException {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = createWorkflowModel();

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(0L);
        doCallRealMethod().when(workflowSweeper).sweep(workflowId);

        CompletableFuture<Void> future1 = workflowSweeper.sweepAsync(workflowId);
        CompletableFuture<Void> future2 = workflowSweeper.sweepAsync(workflowId);

        future1.get(10, TimeUnit.SECONDS);
        future2.get(10, TimeUnit.SECONDS);

        verify(workflowSweeper, times(2)).sweep(workflowId);
    }

    @Test
    public void testSweepNotFound() throws InterruptedException {
        String workflowId = "workflowId";

        doThrow(new NotFoundException("not found")).when(queueDAO).get(DECIDER_QUEUE, workflowId);
        doNothing().when(workflowSweeper).verifyAndRepairWorkflowTasks(workflowId);

        doCallRealMethod().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowSweeper, times(0)).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testSweepRepair() throws InterruptedException {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = createWorkflowModel();

        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);
        doNothing().when(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);

        doCallRealMethod().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepTerminal() throws InterruptedException {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(Status.SUCCEEDED);

        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);

        doCallRealMethod().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepError() throws InterruptedException {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = createWorkflowModel();

        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);

        doThrow(new RuntimeException("runtime error")).when(workflowExecutor).decide(workflowId);

        doCallRealMethod().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"), any(Throwable.class));
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId.getBytes()), anyLong());
    }

    @Test
    public void testUnack() throws InterruptedException {
        String workflowId = "workflowId";
        WorkflowModel workflowModel = createWorkflowModel();

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType("TaskType1"));

        workflowModel.setTasks(Arrays.asList(taskModel));

        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);

        workflowSweeper.unack(workflowModel, 5L);

        assertEquals(6L, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId));

        taskModel.setStatus(Status.FAILED);
        doReturn(workflowModel).when(queueDAO).get(DECIDER_QUEUE, workflowId);

        workflowSweeper.unack(workflowModel, 5L);

        assertEquals(6L, queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;

        assertEquals(new Random().nextInt((int) (2 * (workflowOffsetTimeout / 3) + 1)) + (workflowOffsetTimeout / 3),
                workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout));
    }

    @Test
    public void testGetDeciderQueue() {
        assertEquals(DECIDER_QUEUE, workflowSweeper.getDeciderQueue());
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setWorkflowName("workflowName");
        workflowModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        return workflowModel;
    }
}