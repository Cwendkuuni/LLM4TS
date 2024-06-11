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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private Monitors monitors;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() throws InterruptedException, Exception {
        String workflowId = "testWorkflowId";
        doNothing().when(workflowSweeper).sweep(workflowId);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get(1, TimeUnit.SECONDS);

        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweepWorkflowNotFound() {
        String workflowId = "testWorkflowId";

        when(queueDAO.remove(Utils.DECIDER_QUEUE, workflowId)).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, workflowId);
        verify(workflowSweeper, times(0)).sweep(anyString());
    }

    @Test
    public void testSweepWorkflowFoundTerminal() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getWorkflowModel();

        when(queueDAO.remove(Utils.DECIDER_QUEUE, workflowId)).thenReturn(false);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        when(workflowModel.getStatus().isTerminal()).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, workflowId);
        verify(workflowSweeper, times(0)).sweep(anyString());
    }

    @Test
    public void testSweepWorkflowFound() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getWorkflowModel();

        when(queueDAO.remove(Utils.DECIDER_QUEUE, workflowId)).thenReturn(false);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        when(workflowModel.getStatus().isTerminal()).thenReturn(false);

        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = getWorkflowModel();
        long workflowOffsetTimeout = 45L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.IN_PROGRESS);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(),
                workflowOffsetTimeout * 1000);
    }

    @Test
    public void testUnackScheduled() {
        WorkflowModel workflowModel = getWorkflowModel();
        long workflowOffsetTimeout = 45L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.SCHEDULED);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(),
                workflowOffsetTimeout * 1000);
    }

    @Test
    public void testUnackFailed() {
        WorkflowModel workflowModel = getWorkflowModel();
        long workflowOffsetTimeout = 45L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.FAILED);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO, never()).setUnackTimeout(Utils.DECIDER_QUEUE, workflowModel.getWorkflowId(),
                workflowOffsetTimeout * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= workflowOffsetTimeout - 15L && jitter <= workflowOffsetTimeout + 15L);
    }

    private WorkflowModel getWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setTasks(getTaskModels());
        return workflowModel;
    }

    private List<TaskModel> getTaskModels() {
        return Arrays.asList(new TaskModel(), new TaskModel());
    }
}