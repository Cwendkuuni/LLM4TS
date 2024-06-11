
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.Task.Type;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    private WalletService walletService = mock(WalletService.class);
    private ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private QueueDAO queueDAO = mock(QueueDAO.class);
    private WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private WorkflowModel workflowModel = mock(WorkflowModel.class);

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), conductorProperties, queueDAO);
    }

    @Test
    public void sweepAsync() {
        workflowSweeper.sweepAsync("id");
        verify(workflowSweeper, times(1)).sweep("id");
    }

    @Test
    public void sweep() {
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(TaskModel.Status.RUNNING);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(new IntervalValue("60s"));
        workflowSweeper.sweep("id");
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void sweep_NotFoundException() {
        when(workflowExecutor.decide(anyString())).thenReturn(null);
        workflowSweeper.sweep("id");
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void sweep_Exception() {
        when(workflowExecutor.decide(anyString())).thenThrow(new RuntimeException());
        workflowSweeper.sweep("id");
        verify(queueDAO, times(0)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void unack_InProgress() {
        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(1L);
        workflowSweeper.unack(workflowModel, 1L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void unack_Scheduled() {
        TaskModel taskModel = mock(TaskModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        when(taskDefinition.getPollTimeoutSeconds()).thenReturn(1L);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.of(taskDefinition));
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        workflowSweeper.unack(workflowModel, 1L);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void workflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        boolean isCorrect = result >= (2*workflowOffsetTimeout/3) && result <= (4*workflowOffsetTimeout/3);
        assertEquals(true, isCorrect);
    }
}
