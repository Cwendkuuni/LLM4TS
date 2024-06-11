package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskDAOImpl;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.TestConductorProperties;
import com.netflix.conductor.utils.TestExecutionDAOFacade;
import com.netflix.conductor.utils.TestMetadataDAO;
import com.netflix.conductor.utils.TestWorkflowExecutor;
import com.netflix.conductor.utils.TestWorkflowModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                TestConductorProperties.getTestSystemTaskWorkerCallbackDuration());
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                TestConductorProperties.getTestTaskExecutionPostponeDuration());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(TestWorkflowModel.getTestWorkflowModel());
        when(metadataDAO.getTaskDef(anyString())).thenReturn(TestWorkflowModel.getTestTaskDef());
        when(queueDAO).thenReturn(new QueueDAOImpl());
        when(workflowExecutor).thenReturn(new TestWorkflowExecutor());
    }

    /** Test for execute method with task that is not found */
    @Test
    public void testExecuteTaskNotFound() {
        String taskId = "task-123";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName("taskType"), taskId);
    }

    /** Test for execute method with task that is already completed */
    @Test
    public void testExecuteTaskAlreadyCompleted() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName("taskType"), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    /** Test for execute method with task that exceeds inProgress limit */
    @Test
    public void testExecuteTaskExceedsInProgressLimit() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName("taskType"),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs());
    }

    /** Test for execute method with task that exceeds rate limit per frequency */
    @Test
    public void testExecuteTaskExceedsRateLimitPerFrequency() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setRateLimitPerFrequency(1);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(task.getTaskDef());

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(
                QueueUtils.getQueueName("taskType"),
                taskId,
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs());
    }

    /** Test for execute method with task that is already in progress */
    @Test
    public void testExecuteTaskAlreadyInProgress() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskCaptor.capture());
        TaskModel updatedTask = taskCaptor.getValue();
        assertEquals(TaskModel.Status.IN_PROGRESS, updatedTask.getStatus());
    }

    /** Test for execute method with task that is scheduled */
    @Test
    public void testExecuteTaskScheduled() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskCaptor.capture());
        TaskModel updatedTask = taskCaptor.getValue();
        assertEquals(TaskModel.Status.IN_PROGRESS, updatedTask.getStatus());
    }

    /** Test for execute method with task that completes asynchronously */
    @Test
    public void testExecuteTaskAsyncComplete() throws InterruptedException {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Thread.sleep(systemTaskCallbackTime() + 100);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskCaptor.capture());
        TaskModel updatedTask = taskCaptor.getValue();
        assertEquals(TaskModel.Status.COMPLETED, updatedTask.getStatus());
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName("taskType"), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    /** Test for execute method with task that completes synchronously */
    @Test
    public void testExecuteTaskSyncComplete() {
        String workflowId = "workflow-123";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskImpl("taskType", null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        ArgumentCaptor<TaskModel> taskCaptor = ArgumentCaptor.forClass(TaskModel.class);
        verify(executionDAOFacade, times(1)).updateTask(taskCaptor.capture());
        TaskModel updatedTask = taskCaptor.getValue();
        assertEquals(TaskModel.Status.COMPLETED, updatedTask.getStatus());
        verify(queueDAO, times(1)).remove(QueueUtils.getQueueName("taskType"), taskId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    /** Test for postponeQuietly method with task that is not found */
    @Test
    public void testPostponeQuietlyTaskNotFound() {
        String queueName = "queueName";
        String taskId = "task-123";
        doReturn(null).when(executionDAOFacade).getTaskModel(taskId);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId);

        verify(queueDAO, times(0)).postpone(queueName, taskId, anyInt(), anyInt());
    }

    /** Test for postponeQuietly method with task that is already postponed */
    @Test
    public void testPostponeQuietlyTaskAlreadyPostponed() {
        String queueName = "queueName";
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setCallbackAfterSeconds(queueTaskMessagePostponeSecs());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskId);

        verify(queueDAO, times(1)).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs());
    }

    private long queueTaskMessagePostponeSecs() {
        return TestConductorProperties.getTestTaskExecutionPostponeDuration().getSeconds();
    }

    private long systemTaskCallbackTime() {
        return TestConductorProperties.getTestSystemTaskWorkerCallbackDuration().getSeconds();
    }
}