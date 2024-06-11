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
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ConductorEvent;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("resulttype")
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_shouldRemoveTaskFromQueue() {
        String taskId = "task-1";
        String queueName = "queue-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.COMPLETED);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setWorkflowInstanceId("workflow-1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowPriority(1);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_taskIsAlreadyTerminal_shouldRemoveTaskFromQueue() {
        String taskId = "task-1";
        String queueName = "queue-1";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.FAILED);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setWorkflowInstanceId("workflow-1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowPriority(1);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.remove(queueName, taskId)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_taskIsNull_shouldThrowException() {
        String taskId = "task-1";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, null);
    }

    @Test
    public void testExecute_taskPostponed_shouldPostponeTask() {
        String taskId = "task-1";
        String queueName = "queue-1";
        WorkflowModel workflowModel = new WorkflowModel();
        taskModel.setId(taskId);
        taskModel.setWorkflowInstanceId("workflow-1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowPriority(1);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(queueName, taskId, taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_taskInProgress_shouldExecuteTask() {
        String taskId = "task-1";
        String queueName = "queue-1";
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setWorkflowInstanceId("workflow-1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowPriority(1);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(queueName, taskId, taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenReturn(false);
        when(systemTask.execute(workflowModel, taskModel, workflowExecutor))
                .thenReturn(Optional.of(new ConductorEvent()));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, times(1)).execute(workflowModel, taskModel, workflowExecutor);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_taskExecutionException_shouldLogAndRemoveTaskFromQueue() {
        String taskId = "task-1";
        String queueName = "queue-1";
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setWorkflowInstanceId("workflow-1");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        taskModel.setWorkflowPriority(1);
        when(executionDAOFacade.getWorkflowModel(taskModel.getWorkflowInstanceId(), false))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(queueName, taskId, taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenReturn(false);
        when(systemTask.execute(workflowModel, taskModel, workflowExecutor)).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(asyncSystemTaskExecutor, times(1)).logError(
                anyString(), anyString(), any(Exception.class));
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }
}