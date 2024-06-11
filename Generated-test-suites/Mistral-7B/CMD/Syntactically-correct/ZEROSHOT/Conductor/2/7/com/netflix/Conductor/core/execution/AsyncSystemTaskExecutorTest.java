package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueOperationResult;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.MockWorkflowModel;
import com.netflix.conductor.utils.MockWorkflowSystemTask;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    private WorkflowSystemTask systemTask;
    private TaskModel task;
    private WorkflowModel workflow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemTask = new MockWorkflowSystemTask();
        task = new TaskModel();
        workflow = new MockWorkflowModel();
        when(metadataDAO.getTaskDef(anyString())).thenReturn(workflow.getTaskDefinition());
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong()))
                .thenReturn(QueueOperationResult.QUEUED);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(null);
    }

    @Test
    public void testExecute_taskNotFound() {
        asyncSystemTaskExecutor.execute(systemTask, "nonExistentTaskId");
        verify(executionDAOFacade, never()).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskCompleted() {
        task.setStatus(TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecute_taskScheduled() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), task.getTaskId());
        verify(queueDAO).postpone(anyString(), task.getTaskId(), 0, eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void testExecute_taskInProgress() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO, never()).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecute_taskTerminal() {
        task.setStatus(ExecutionStatus.FAILED);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(anyString(), task.getTaskId());
    }

    @Test
    public void testExecute_taskRateLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflow.getTaskDefinition()))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
        verify(executionDAOFacade, never()).updateTask(task);
    }

    @Test
    public void testExecute_taskConcurrentExecutionLimit() {
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
        verify(queueDAO).remove(anyString(), task.getTaskId());
        verify(executionDAOFacade, never()).updateTask(task);
    }
}