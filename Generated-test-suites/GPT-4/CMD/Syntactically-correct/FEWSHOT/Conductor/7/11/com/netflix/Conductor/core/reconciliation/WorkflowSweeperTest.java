
package com.netflix.conductor.core.reconciliation;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

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
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        workflowSweeper.sweepAsync("testWorkflowId");
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(any(String.class));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        long jitter = workflowSweeper.workflowOffsetWithJitter(3);
        assert(jitter >= 2 && jitter <= 4);
    }

    @Test 
    public void testSweep(){
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.RUNNING);
        
        when(workflowExecutor.decide(any(String.class))).thenReturn(workflowModel);
        workflowSweeper.sweep("testWorkflowId");
        
        verify(workflowExecutor, times(1)).decide(any(String.class));
        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(any(String.class));
    }
}
