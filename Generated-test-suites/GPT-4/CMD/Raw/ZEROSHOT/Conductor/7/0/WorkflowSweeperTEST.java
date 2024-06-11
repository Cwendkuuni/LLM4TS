
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

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

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
        assert future.isDone();
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_HUMAN);
        workflowSweeper.unack(workflowModel, 1000);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(1000);
        assertTrue(result >= 666 && result <= 1333);
    }
}
