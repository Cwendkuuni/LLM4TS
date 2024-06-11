
package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

import static org.mockito.Mockito.*;
import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    WorkflowExecutor workflowExecutor;
    
    @Mock
    WorkflowRepairService workflowRepairService;

    @Mock
    ConductorProperties properties;

    @Mock
    QueueDAO queueDAO;

    @Mock
    WorkflowModel workflowModel;
 
    WorkflowSweeper sweeperUnderTest;

    @Before
    public void setUp() {
        sweeperUnderTest = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "12345";
        doNothing().when(workflowExecutor).decide(workflowId);
        sweeperUnderTest.sweepAsync(workflowId).get();
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "12345";
        doNothing().when(workflowExecutor).decide(workflowId);
        sweeperUnderTest.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testUnack() {
        doNothing().when(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
        sweeperUnderTest.unack(workflowModel, anyLong());
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long returnedValue = sweeperUnderTest.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(returnedValue <= 60L && returnedValue >= 30L);
    }

    @Test(expected = NotFoundException.class)
    public void testSweep_WhenWorkflowNotFound() {
        String workflowId = "12345";
        doThrow(new NotFoundException("Workflow Not Found")).when(workflowExecutor).decide(workflowId);
        sweeperUnderTest.sweep(workflowId);
    }
}
