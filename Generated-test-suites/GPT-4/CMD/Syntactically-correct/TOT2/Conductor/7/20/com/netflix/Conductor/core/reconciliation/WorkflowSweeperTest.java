package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;

public class WorkflowSweeperTest {
    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, workflowRepairService, properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
        assertEquals(future.get(), null);  
    }

    @Test
    public void testSweep() throws Exception {
        // Assuming workflowExecutor is throwing a NotFoundException
        when(workflowExecutor.decide(anyString())).thenThrow(NotFoundException.class);
        // Here we are just verifying that sweep() method calls the remove() of queueDAO in case of NotFoundException thrown
        workflowSweeper.sweep("testWorkflowId");
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {   
        long offsetTimeOut = 50L;
        long resultOffsetTimeOut = workflowSweeper.workflowOffsetWithJitter(offsetTimeOut);
        // Assert the offset time is within the expected range 
        assertTrue(resultOffsetTimeOut >= (offsetTimeOut - offsetTimeOut / 3) && resultOffsetTimeOut <= (offsetTimeOut + offsetTimeOut / 3));
    }
}
