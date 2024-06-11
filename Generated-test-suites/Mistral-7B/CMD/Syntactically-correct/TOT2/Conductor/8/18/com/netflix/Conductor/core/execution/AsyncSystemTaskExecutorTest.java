package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.utils.QueueUtils;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    @InjectMocks(strictness = Strictness.LENIENT)
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(mockTaskModel());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(mockWorkflowModel());
        when(metadataDAO.getTaskDef(anyString())).thenReturn(mockTaskDef());
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.get(anyString())).thenReturn(mockQueue());
        when(workflowExecutor.decide(anyString())).thenReturn(true);
        doNothing().when(asyncSystemTaskExecutor).postponeQuietly(anyString(), any(TaskModel.class));
        doNothing().when(asyncSystemTaskExecutor).loadTaskQuietly(anyString());
    }

    @Test
    public void testExecute_taskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testExecute_taskAlreadyCompleted() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), taskId);
        verifyNoInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_taskInQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(false);
        doReturn(true).when(queueDAO).postpone(queueName, eq(taskId), anyInt(), anyLong());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(queueName, eq(taskId), anyInt(), anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_concurrencyLimitExceeded() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(queueDAO.postpone(queueName, eq(taskId), anyInt(), anyLong()))
                .thenThrow(new IllegalStateException("Concurrency limit exceeded"));
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_rateLimitExceeded() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any()))
                .thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_workflowCompleted() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = mockWorkflowModel();
        workflow.setStatus(Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflow);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testExecute_workflowInProgress() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mockTaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = mockWorkflowModel();
        workflow.setStatus(Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(task.getWorkflowInstanceId(), false))
                .thenReturn(workflow);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = QueueUtils.getQueueName("workflow-1");
        TaskModel task = mock(TaskModel.class);
        when(queueDAO.postpone(queueName, eq(task.getTaskId()), anyInt(), anyLong()))
                .thenReturn(true);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, eq(task.getTaskId()), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskExist() {
        String taskId = "task-1";
        TaskModel task = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel taskReturned = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(task, taskReturned);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() {
        String taskId = "task-1";
        TaskModel task = null;
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        TaskModel taskReturned = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(taskReturned);
    }

    private TaskModel mockTaskModel() {
        TaskModel task = mock(TaskModel.class);
        task.setTaskId("task-1");
        task.setTaskType("task-type");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setStartTime(System.currentTimeMillis());
        task.setEndTime(System.currentTimeMillis());
        task.setCallbackAfterSeconds(1000);
        task.setPollCount(1);
        task.setWorkflowPriority(1);
        task.setWorkflowInstanceId("workflow-1");
        return task;
    }

    private WorkflowModel mockWorkflowModel() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        workflow.setWorkflowId("workflow-1");
        workflow.setWorkflowName("workflow-name");
        workflow.setStatus(Status.IN_PROGRESS);
        return workflow;
    }

    private WorkflowSystemTask mockWorkflowSystemTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        return systemTask;
    }

    private Queue mockQueue() {
        Queue queue = mock(Queue.class);
        return queue;
    }
}