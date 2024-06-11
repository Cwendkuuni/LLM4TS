package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;

import org.junit.Test;
import org.mockito.Mock;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    private String workflowId = "test_workflow";

    @Test
    public void testSweepAsync() throws Exception {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        workflowSweeper.sweepAsync(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(taskModel.getWaitTimeout()).thenReturn(5L);

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);
        when(workflowModel.getStatus()).thenReturn(WorkflowStatus.RUNNING);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        doNothing().when(queueDAO).remove(anyString(), anyString());

        workflowSweeper.sweep(workflowId);
    }

    @Test
    public void testUnack() {
        TaskDef taskDef = mock(TaskDef.class);
        when(taskDef.getPollTimeoutSeconds()).thenReturn(10);

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(taskModel.getTaskDefinition()).thenReturn(Optional.of(taskDef));

        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn(workflowId);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        workflowSweeper.unack(workflowModel, 5000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        workflowSweeper.workflowOffsetWithJitter(5000);
    }
}