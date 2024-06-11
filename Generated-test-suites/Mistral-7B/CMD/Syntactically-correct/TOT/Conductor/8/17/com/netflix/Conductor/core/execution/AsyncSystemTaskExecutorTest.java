package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.testutils.SpringTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SpringTest
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
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_taskNotFound() {
        String taskId = "task-1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_taskInProgress() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(task);
        TaskModel savedTask = new TaskModel();
        savedTask.setId(taskId);
        savedTask.setStatus(TaskModel.Status.IN_PROGRESS);
        savedTask.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        savedTask.setWorkflowPriority(WORKFLOW_PRIORITY);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(savedTask);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_INSTANCE_ID);
        workflowModel.setStatus(WorkflowInstance.Status.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(workflowExecutor).decide(workflowInstanceId);
    }

    @Test
    public void testExecute_taskCompleted() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(task);
        TaskModel savedTask = new TaskModel();
        savedTask.setId(taskId);
        savedTask.setStatus(TaskModel.Status.COMPLETED);
        savedTask.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        savedTask.setWorkflowPriority(WORKFLOW_PRIORITY);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(savedTask);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_INSTANCE_ID);
        workflowModel.setStatus(WorkflowInstance.Status.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO).remove(queueName, taskId);
        verify(workflowExecutor).decide(workflowInstanceId);
    }

    @Test
    public void testExecute_taskFailed() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(task);
        TaskModel savedTask = new TaskModel();
        savedTask.setId(taskId);
        savedTask.setStatus(TaskModel.Status.FAILED);
        savedTask.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        savedTask.setWorkflowPriority(WORKFLOW_PRIORITY);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(savedTask);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_INSTANCE_ID);
        workflowModel.setStatus(WorkflowInstance.Status.RUNNING);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).remove(queueName, taskId);
        verify(workflowExecutor, never()).decide(workflowInstanceId);
    }

    @Test
    public void testExecute_queueFull() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(task);
        TaskModel savedTask = new TaskModel();
        savedTask.setId(taskId);
        savedTask.setStatus(TaskModel.Status.SCHEDULED);
        savedTask.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        savedTask.setWorkflowPriority(WORKFLOW_PRIORITY);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(savedTask);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_INSTANCE_ID);
        workflowModel.setStatus(WorkflowInstance.Status.RUNNING);

        when(executionDAOFacade.exceedsInProgressLimit(savedTask)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(queueName, taskId, savedTask.getWorkflowPriority(), anyLong());
    }

    @Test
    public void testExecute_rateLimitExceeded() {
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName(task);
        TaskModel savedTask = new TaskModel();
        savedTask.setId(taskId);
        savedTask.setStatus(TaskModel.Status.SCHEDULED);
        savedTask.setWorkflowInstanceId(WORKFLOW_INSTANCE_ID);
        savedTask.setWorkflowPriority(WORKFLOW_PRIORITY);
        TaskModel taskDef = new TaskModel();
        taskDef.setTaskDefName("taskDefName");
        taskDef.setRateLimitPerFrequency(5);
        when(metadataDAO.getTaskDef(taskDef.getTaskDefName())).thenReturn(taskDef);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(savedTask);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_INSTANCE_ID);
        workflowModel.setStatus(WorkflowInstance.Status.RUNNING);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(savedTask, taskDef)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).postpone(queueName, taskId, savedTask.getWorkflowPriority(), anyLong());
    }

    private static final String WORKFLOW_INSTANCE_ID = "workflowInstanceId";
    private static final int WORKFLOW_PRIORITY = 1;
}