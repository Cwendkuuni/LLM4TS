package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        
        assertNotNull(result);
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove("DECIDER_QUEUE", workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "123";
        
        workflowSweeper.sweep(workflowId);
        
        verify(workflowExecutor).decide(workflowId);
        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
        verify(queueDAO).remove("DECIDER_QUEUE", workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        long workflowOffsetTimeout = 45;
        
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        
        verify(queueDAO).setUnackTimeout("DECIDER_QUEUE", workflowModel.getWorkflowId(), 46000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        assertEquals(30, result, 15);
    }
}