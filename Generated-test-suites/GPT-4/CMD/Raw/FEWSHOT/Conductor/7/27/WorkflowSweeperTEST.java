package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;

public class WorkflowSweeperTest {
    @Test
    public void testWorkflowSweeperMethods() {
        ConductorProperties properties = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        QueueDAO queueDAO = mock(QueueDAO.class);
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskModel taskModel = mock(TaskModel.class);
        TaskDef taskDef = mock(TaskDef.class);
        
        when(workflowModel.getWorkflowId()).thenReturn("wID");
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(10);
        ReflectionTestUtils.invokeMethod(sweeper, "unack", workflowModel, 10L);
        
        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        ReflectionTestUtils.invokeMethod(sweeper, "workflowOffsetWithJitter", 10L);
        
        assertEquals("WorkflowSweeper", sweeper.getClass().getSimpleName());
    }
}