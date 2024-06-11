//Note: Due to the unavailability of some classes and methods, these test cases might not cover all edge cases of your codebase.


package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {
    
    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper underTest;

    @Test
    public void testWorkflowSweeperConstructor() {
        Assert.assertNotNull(underTest);
    }

    @Test
    public void testSweepAsync() throws Exception {
        final String workflowId = "testWorkflow";
        underTest.sweepAsync(workflowId);
    }
    
    @Test
    public void testSweep() {
        //IOException path
        doThrow(new NullPointerException()).when(queueDAO).remove(any(), anyString());
        underTest.sweep("sampleId");

        //other exceptions
        doThrow(new RuntimeException()).when(queueDAO).remove(any(), anyString());
        underTest.sweep("sampleId");

        verify(queueDAO, times(2)).remove(any(), anyString());
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel model = mock(WorkflowModel.class);
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(task.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);
        underTest.unack(model, 5);

        verify(queueDAO, times(1)).setUnackTimeout(any(), any(), anyInt());
    }

    // cover the workflowOffset method with negative, zero, and positive values
    @Test
    public void testworkflowOffsetWithJitter() {
        underTest.workflowOffsetWithJitter(-3);
        Assert.assertEquals(underTest.workflowOffsetWithJitter(0),0);
        underTest.workflowOffsetWithJitter(5);
    }
    
}
