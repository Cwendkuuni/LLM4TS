package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;

import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowModel workflowModel;
    private ConductorProperties properties;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setUp() {
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        workflowModel = mock(WorkflowModel.class);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflow_1";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertEquals(result.get(), null);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(result >= 40 && result <= 80);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflow_1";
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, atLeastOnce()).remove(anyString(), anyString());
    }

    @Test
    public void testUnAck() {
        TaskModel taskModel = mock(TaskModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("workflow_1");
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        workflowSweeper.unack(workflowModel, 60);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }
}
