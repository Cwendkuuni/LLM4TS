package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MockitoJUnitRunner;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("sampleWorkflowId");
        future.join();
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("sampleWorkflowId");
        when(workflowExecutor.decide("sampleWorkflowId")).thenReturn(workflowModel);
        
        workflowSweeper.sweep("sampleWorkflowId");
        
        when(workflowExecutor.decide("sampleWorkflowId")).thenReturn(null);
        workflowSweeper.sweep("sampleWorkflowId");
        
        doThrow(new NotFoundException()).when(queueDAO).remove(anyString(), anyString());
        workflowSweeper.sweep("sampleWorkflowId");
        
        doThrow(new RuntimeException()).when(workflowExecutor).decide("sampleWorkflowId");
        workflowSweeper.sweep("sampleWorkflowId");
        
        verify(queueDAO, times(3)).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("sampleWorkflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);
        workflowModel.getTasks().add(taskModel);
        
        workflowSweeper.unack(workflowModel, 50);
        
        taskModel.setTaskType(TaskType.TASK_TYPE_SYSTEM);
        taskModel.setResponseTimeoutSeconds(20);
        workflowSweeper.unack(workflowModel, 50);

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);
        workflowSweeper.unack(workflowModel, 50);
        
        verify(queueDAO, times(3)).setUnackTimeout(anyString(), anyString(), anyLong());
    }
    
    @Test
    public void testWorkflowOffsetWithJitter() {
        assertEquals(45, workflowSweeper.workflowOffsetWithJitter(45));
    }

}