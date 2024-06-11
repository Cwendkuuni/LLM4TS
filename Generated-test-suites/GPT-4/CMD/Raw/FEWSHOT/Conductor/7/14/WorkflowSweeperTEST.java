
package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;


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
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }
    
    @Test
    public void testSweep() {
        String workflowId = "1";
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflow);
        doNothing().when(queueDAO).remove(anyString(), anyString());

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testSweepException() {
        String workflowId = "1";
        when(workflowExecutor.decide(anyString())).thenThrow(new RuntimeException());

        try {
            workflowSweeper.sweep(workflowId);
            fail();
        } catch (Exception e) {
            // Exception Expected    
        }
    }
    
    @Test
    public void testSweepAsync() {
        String workflowId = "1";
        WorkflowModel mockWorkflow = new WorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(mockWorkflow);
        doNothing().when(queueDAO).remove(anyString(), anyString());
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertEquals(future.isDone(), true);

        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testUnack() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        long workflowOffsetTimeout = 10;
        doNothing().when(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(jitter >= 30 && jitter <= 60, true);
    }

}
