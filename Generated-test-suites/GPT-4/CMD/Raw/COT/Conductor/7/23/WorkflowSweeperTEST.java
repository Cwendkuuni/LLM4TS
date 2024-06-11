package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Mock
    ConductorProperties properties;

    @Mock
    QueueDAO queueDAO;

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    WorkflowRepairService workflowRepairService;

    @Mock
    TaskModel taskModel;

    @Mock
    WorkflowModel workflowModel;

    @Mock
    TaskDef taskDef;

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        Assert.assertNotNull(result);
    }

    @Test
    public void testSweep() {
        MockitoAnnotations.initMocks(this);
        Optional<WorkflowRepairService> service = Optional.of(workflowRepairService);
        when(workflowExecutor.decide(any())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        workflowSweeper.sweep("workflowId");
        verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test
    public void testUnack() {
        MockitoAnnotations.initMocks(this);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        workflowSweeper.unack(workflowModel, 1000L);
        verify(queueDAO, times(1)).setUnackTimeout(any(), any(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(1000L);
        Assert.assertTrue(result >= 667 && result <= 1333);
    }
}