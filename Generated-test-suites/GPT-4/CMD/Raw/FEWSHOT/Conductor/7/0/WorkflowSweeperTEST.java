package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel.Status;

import org.junit.Test;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {
    WorkflowSweeper workflowSweeper;
    ConductorProperties properties;
    WorkflowExecutor workflowExecutor;
    WorkflowRepairService workflowRepairService;
    QueueDAO queueDAO;
    String workflowId = "workflow1";

    @Before
    public void setUp() {
        properties = mock(ConductorProperties.class);
        when(properties.getWorkflowOffsetTimeout()).thenReturn(60L);
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
        assertEquals(CompletableFuture.completedFuture(null), result);
    }

    @Test
    public void testSweepWithWorkflowNotFound() {
        when(workflowExecutor.decide(workflowId)).thenThrow(NotFoundException.class);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testSweepWithException() {
        when(workflowExecutor.decide(workflowId)).thenThrow(RuntimeException.class);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(0)).remove(anyString(), eq(workflowId));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitterResult = workflowSweeper.workflowOffsetWithJitter(60);
        assertTrue(jitterResult >= 40 && jitterResult <= 80);
    }
}