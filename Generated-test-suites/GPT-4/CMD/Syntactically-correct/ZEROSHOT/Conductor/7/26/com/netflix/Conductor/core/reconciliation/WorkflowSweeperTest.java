
package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Random;

import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;
    
    @Mock
    private WorkflowExecutor workflowExecutor;
    
    @Mock
    private WorkflowRepairService workflowRepairService;
    
    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties,
                queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "workflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get();
        verify(workflowSweeper).sweep(workflowId);
        verifyNoMoreInteractions(workflowSweeper);
    }

    @Test
    public void testSweepNotFoundException() {
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Not found."));
        String workflowId = "workflowId";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testWorkflowOffsetWithJitter(){
        long workflowOffsetTimeout = 45L; 
        long returnedOffset = workflowSweeper. workflowOffsetWithJitter(workflowOffsetTimeout);
        assert(returnedOffset >= 30L && returnedOffset <= 60L);
    }

    @Test
    // Also test for other status and task types
    public void testUnackWithInProgressStatusAndWaitTaskType() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        TaskModel mockTaskModel = mock(TaskModel.class);
        when(mockWorkflowModel.getTasks()).thenReturn(Arrays.asList(mockTaskModel));
        when(mockTaskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(mockTaskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(mockTaskModel.getWaitTimeout()).thenReturn(50);
        workflowSweeper.unack(mockWorkflowModel, 30);
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());
    }
}
