
package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowRepairService workflowRepairService;
    
    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), 
            properties, queueDAO);
        String workflowId= "testID";

        sweeper.sweepAsync(workflowId);

        verify(sweeper, times(1)).sweep(workflowId);
    }


    @Test
    public void testSweepWorkflow_NotFound() {
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not found"));
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), 
            properties, queueDAO);
        String workflowId= "testID";

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWorkflow_Found() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), 
            properties, queueDAO);
        String workflowId= "testID";

        sweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 
            sweeper.workflowOffsetWithJitter(properties.getWorkflowOffsetTimeout().getSeconds()) * 1000);
    }
}
