package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    private QueueDAO queueDAO = mock(QueueDAO.class);
    private MetadataDAO metadataDAO = mock(MetadataDAO.class);
    private ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    private String taskId = "taskId";
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void executeTaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void executeTaskAlreadyCompleted() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void executeExceedsInProgressLimit() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void executeExceedsRateLimit() {
        TaskModel task = mock(TaskModel.class);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, metadataDAO.getTaskDef(task.getTaskDefName()))).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void executeWorkflowCompletion() {
        TaskModel task = mock(TaskModel.class);
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getStatus()).thenReturn(WorkflowModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(task.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).updateTask(task);
        verify(workflowExecutor, times(1)).decide(workflow.getWorkflowId());
    }
}
