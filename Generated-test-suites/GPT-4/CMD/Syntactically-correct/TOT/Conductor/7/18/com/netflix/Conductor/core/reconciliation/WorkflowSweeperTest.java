package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;
  
    @Mock
    private ConductorProperties properties;
  
    @Mock
    private WorkflowExecutor workflowExecutor;
  
    @Mock
    private WorkflowRepairService workflowRepairService;
  
    @Mock
    private QueueDAO queueDAO;

    private String validWorkflowId = "validWorkflowId";
    private String invalidWorkflowId = "invalidWorkflowId";

    @Test
    public void sweepAsyncWithValidWorkflowId() throws Exception {
        doNothing().when(workflowSweeper).sweep(validWorkflowId);
        CompletableFuture<Void> future =  workflowSweeper.sweepAsync(validWorkflowId);
        future.get(); // this will block until the CompletableFuture is done
        verify(workflowSweeper, times(1)).sweep(validWorkflowId);
    }
  
    @Test
    public void sweepAsyncWithInvalidWorkflowId() throws Exception {
        doNothing().when(workflowSweeper).sweep(invalidWorkflowId);
        CompletableFuture<Void> future =  workflowSweeper.sweepAsync(invalidWorkflowId);
        future.get(); // this will block until the CompletableFuture is done
        verify(workflowSweeper, times(1)).sweep(invalidWorkflowId);
    }

    @Test(expected = NotFoundException.class)
    public void sweepWithNotFoundException() throws Exception {
        doThrow(NotFoundException.class).when(workflowSweeper).sweep(invalidWorkflowId);
        workflowSweeper.sweep(invalidWorkflowId);
    }

    // Additional test cases for workflowOffsetWithJitter and unack methods
}

