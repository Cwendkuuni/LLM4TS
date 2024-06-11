package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkflowSweeperTest {

    @Test
    public void testSweep() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);
        workflowSweeper.sweep("test_workflow_id");
    }

    @Test
    public void testSweepAsync() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("test_workflow_id");
        assertEquals(null, future.join());
    }

    @Test
    public void testUnack() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowSweeper.unack(workflowModel, 60);

        // Ensure queueDAO.setUnackTimeout() is called once
        QueueDAO queueDAO = Mockito.mock(QueueDAO.class);
        workflowSweeper.setQueueDAO(queueDAO);
        workflowSweeper.unack(workflowModel, 60);
        Mockito.verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(null, null, null, null);
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        // Ensure result is between [30, 60]
        assertEquals(true, result >= 30 && result <= 60);
    }
}