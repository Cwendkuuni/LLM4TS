package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueNameProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueNameProvider queueNameProvider;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-id";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(workflowExecutor, never()).decide(anyString());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, never()).execute(workflow, task, workflowExecutor);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskTerminal() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.TERMINAL);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, never()).execute(workflow, task, workflowExecutor);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskScheduled() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        Optional<TaskModel> loadedTaskOptional = Optional.ofNullable(mock(TaskModel.class));
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.remove(anyString(), anyString())).thenThrow(new RuntimeException("Queue remove failed"));
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(false);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, times(1)).execute(workflow, task, workflowExecutor);
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyLong());
        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskPostponed() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        Optional<TaskModel> loadedTaskOptional = Optional.ofNullable(mock(TaskModel.class));
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.remove(anyString(), anyString())).thenThrow(new RuntimeException("Queue remove failed"));
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(false);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, never()).execute(workflow, task, workflowExecutor);
        verify(queueDAO, never()).postpone(anyString(), taskId, anyInt(), anyLong());
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test
    public void testExecute_taskRateLimitExceeded() {
        String taskId = "task-id";
        String workflowId = "workflow-id";
        Optional<TaskModel> loadedTaskOptional = Optional.ofNullable(mock(TaskModel.class));
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        when(queueDAO.remove(anyString(), anyString())).thenThrow(new RuntimeException("Queue remove failed"));
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).getWorkflowModel(workflowId, false);
        verify(systemTask, never()).execute(workflow, task, workflowExecutor);
        verify(queueDAO, never()).postpone(anyString(), taskId, anyInt(), anyLong());
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(workflowId);
    }
}