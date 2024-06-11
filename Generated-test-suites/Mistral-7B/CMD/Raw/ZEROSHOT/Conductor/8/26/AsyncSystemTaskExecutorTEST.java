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
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO.QueueAction;
import com.netflix.conductor.utils.QueueUtils;

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

    @Mock
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId";
        String workflowInstanceId = "workflowInstanceId";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setWorkflowInstanceId(workflowInstanceId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel(workflowInstanceId, false);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testExecute_TaskInQueue() {
        String taskId = "taskId";
        String workflowInstanceId = "workflowInstanceId";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId(workflowInstanceId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getWorkflowModel(workflowInstanceId, true);
        verify(queueDAO).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                asyncSystemTaskExecutor.getSystemTaskCallbackTime());
    }

    @Test
    public void testExecute_TaskException() {
        String taskId = "taskId";
        String workflowInstanceId = "workflowInstanceId";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId(workflowInstanceId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        Exception exception = new RuntimeException("Test Exception");
        when(systemTask.isAsyncComplete(task)).thenThrow(exception);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(workflowInstanceId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        String taskId = "taskId";

        asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

        verify(queueDAO).postpone(queueName, taskId, 0, queueSystemTaskExecutor.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.empty());

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade, never()).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
    }
}