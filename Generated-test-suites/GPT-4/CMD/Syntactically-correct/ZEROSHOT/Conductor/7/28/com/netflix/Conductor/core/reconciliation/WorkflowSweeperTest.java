
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    @Mock
    WorkflowExecutor workflowExecutor;
    @Mock
    Optional<WorkflowRepairService> workflowRepairService;
    @Mock
    ConductorProperties properties;
    @Mock
    QueueDAO queueDAO;

    @InjectMocks
    WorkflowSweeper workflowSweeper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("testWorkflowId");
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(1);
        workflowSweeper.unack(workflowModel, 1);
        verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 60;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assert (result >= 40) && (result <= 80);
    }
}
