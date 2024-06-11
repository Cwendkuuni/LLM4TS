package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.workflow.WorkflowExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Optional;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.executionDAOFacade = mock(ExecutionDAOFacade.class);
        this.queueDAO = new QueueDAOImpl();
        this.metadataDAO = mock(MetadataDAO.class);
        this.workflowExecutor = mock(WorkflowExecutor.class);
        this.conductorProperties = new ConductorProperties();
        this.conductorProperties.setSystemTaskWorkerCallbackDuration(Duration.ofSeconds(10));
        this.conductorProperties.setTaskExecutionPostponeDuration(Duration.ofSeconds(5));

        this.asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecuteNullTaskId() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(workflowSystemTask, null);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteNullSystemTask() {
        String taskId = "taskId";
        asyncSystemTaskExecutor.execute(null, taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecuteTaskAlreadyCompleted() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.COMPLETED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecuteTaskInProgress() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, never()).remove(anyString(), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecuteTaskScheduled() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);
        doNothing().when(queueDAO).postpone(anyString(), eq(taskId), anyInt(), anyInt());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), anyInt());
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecuteTaskConcurrentExecutionLimit() {
        String taskDefName = "taskDefName";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("queueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskDefName(taskDefName);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");

        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(new TaskDefinitionModel());

        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);

        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), anyInt());
        verify(workflowExecutor, times(1)).decide("workflowId");
    }

    @Test
    public void testExecuteTaskRateLimitPerFrequency() {
        String taskDefName = "taskDefName";
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName("queueName");
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskDefName(taskDefName);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflowId");
        task.setRateLimitPerFrequency(1);

        MetadataDAO metadataDAO = mock(MetadataDAO.class);
        TaskDefinitionModel taskDefinitionModel = new TaskDefinitionModel();
        taskDefinitionModel.setRateLimitPerFrequency(1);
        when(metadataDAO.getTaskDef(taskDefName)).thenReturn(taskDefinitionModel);

        ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, taskDefinitionModel)).thenReturn(true);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyInt(), anyInt());
        verify(workflowExecutor, times(1)).decide("workflowId");
    }

    @Test
    public void testExecuteTaskNoWorkflowFound() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.DELETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testExecuteTaskWorkflowNotFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("taskDefName");
        task.setWorkflowInstanceId("workflowId");

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Workflow not found")).when(executionDAOFacade).getWorkflowModel(task.getWorkflowInstanceId(), true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verify(workflowExecutor, never()).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void testExecuteTaskSystemTaskCallback() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(false);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).postpone(eq(QueueUtils.getQueueName(task)), eq(taskId), anyInt(), anyInt());
    }

    @Test
    public void testExecuteTaskSystemTaskComplete() {
        String workflowId = "workflowId";
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        when(workflowSystemTask.isAsyncComplete(task)).thenReturn(true);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(queueDAO, times(1)).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
        verify(workflowExecutor, times(1)).decide(workflowId);
    }
}