package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOException;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("resultoftmethodcallignored")
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

    @Autowired
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanup() {
        reset(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.COMPLETED);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.TERMINAL);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test(expected = Exception.class)
    public void testExecute_ExecutionLimitExceeded() {
        String taskId = "taskId";
        String taskType = "taskType";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, QueuePriority.DEFAULT, queueTaskMessagePostponeSecs);
    }

    @Test(expected = Exception.class)
    public void testExecute_RateLimitExceeded() {
        String taskId = "taskId";
        String taskType = "taskType";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setStatus(TaskModel.Status.SCHEDULED)
                .setRateLimitPerFrequency(1);
        WorkflowModel workflow = new WorkflowModel().setId("workflowId").setStatus(Status.STARTED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(taskType)).thenReturn(Optional.of(workflow));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, workflow)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(queueDAO, times(1)).postpone(queueName, taskId, QueuePriority.DEFAULT, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.STARTED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.STARTED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, QueuePriority.DEFAULT, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskCancelled() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.CANCELED);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.STARTED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test(expected = Exception.class)
    public void testExecute_ExceptionThrown() {
        String taskId = "taskId";
        String taskType = "taskType";
        String workflowInstanceId = "workflowInstanceId";
        String queueName = QueueUtils.getQueueName(new TaskModel().setId(taskId).setTaskDefName(taskType));
        TaskModel task = new TaskModel().setId(taskId).setTaskType(taskType).setWorkflowInstanceId(workflowInstanceId)
                .setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel().setId(workflowInstanceId).setStatus(Status.STARTED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowInstanceId, false)).thenReturn(workflow);
        doThrow(new Exception("ExceptionMessage")).when(workflowExecutor).decide(workflowInstanceId);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
    }

    @Test(expected = QueueDAOException.class)
    public void testPostponeQuietly_QueueFailure() {
        String queueName = "queueName";
        TaskModel task = new TaskModel().setStatus(TaskModel.Status.SCHEDULED);

        when(queueDAO.postpone(queueName, task.getId(), QueuePriority.DEFAULT, queueTaskMessagePostponeSecs))
                .thenThrow(new QueueDAOException("QueueFailureMessage"));

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel().setId(taskId).setStatus(TaskModel.Status.SCHEDULED);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "taskId";

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }
}