
package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import static java.util.Optional.ofNullable;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {
    @Mock
    private ConductorProperties properties;
    
    @Mock
    private QueueDAO queueDAO;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
        
    @Mock 
    private WorkflowRepairService workflowRepairService;
    
    private WorkflowSweeper workflowSweeper;
    
    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, ofNullable(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "12345";
        when(workflowExecutor.decide(workflowId)).thenReturn(null);
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(result >= 30 && result <= 60);
    }

    @Test
    public void testSweep() {
        String workflowId = "12345";
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        workflow.setStatus(WorkflowStatus.RUNNING);
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep_Exception_NotFoundException() {
        String workflowId = "12345";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }
}
