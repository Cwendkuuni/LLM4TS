package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {
    
    @InjectMocks
    private WorkflowSweeper sweeper;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private ConductorProperties properties;
    @Mock
    private QueueDAO queueDAO;
    
    private static final String APP_ID = "APP_ID";
    private static final String WORKFLOW_ID = "WORKFLOW_ID";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);
        assertEquals(CompletableFuture.completedFuture(null), result);
        verify(this.workflowExecutor).decide(WORKFLOW_ID);
    }
    
    
    @Test
    public void testSweepWorkflow() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        
        Mockito.when(this.workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);
        
        sweeper.sweep(WORKFLOW_ID);
        
        verify(this.workflowExecutor).decide(WORKFLOW_ID);
        verify(this.queueDAO).remove(any(), any());
    }

    @Test
    public void testSweepWorkflowNotFoundException() {
        Mockito.when(this.workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Test exception"));
        
        sweeper.sweep(WORKFLOW_ID);

        verify(this.queueDAO).remove(any(), any());
    }
    
    @Test
    public void testUnack() {
        WorkflowModel workflow = new WorkflowModel();
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        workflow.setTasks(Arrays.asList(task));

        sweeper.unack(workflow, 30L);

        verify(this.queueDAO).setUnackTimeout(any(), any(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long timeout = 45;
        long jitterTime = sweeper.workflowOffsetWithJitter(timeout);

        assert(jitterTime > 30 && jitterTime < 60);
    }
}