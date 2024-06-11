package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private QueueDAOImpl queueDAOImpl;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAOImpl = new QueueDAOImpl();
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_NullTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertNull(systemTask.getTask());
    }

    @Test
    public void testExecute_NonTerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(queueDAO.getQueueName(task)).thenReturn("queueName");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyExecuteWithNonTerminalTask(task);
    }

    @Test
    public void testExecute_TerminalTask() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(queueDAO.getQueueName(task)).thenReturn("queueName");

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyExecuteWithTerminalTask(task);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ConcurrentLimitExceeded() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(queueDAO.getQueueName(task)).thenReturn("queueName");

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_RateLimitExceeded() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = mock(TaskModel.class);
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(queueDAO.getQueueName(task)).thenReturn("queueName");

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_NullTask() {
        String taskId = "taskId";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_NotNullTask() {
        String taskId = "taskId";
        TaskModel task = mock(TaskModel.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertSame(task, loadedTask);
    }

    private void verifyExecuteWithNonTerminalTask(TaskModel task) {
        ArgumentCaptor<WorkflowModel> workflowModelArgumentCaptor = ArgumentCaptor.forClass(WorkflowModel.class);
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);
        ArgumentCaptor<WorkflowExecutor> workflowExecutorArgumentCaptor = ArgumentCaptor.forClass(WorkflowExecutor.class);

        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
        verify(systemTask).start(workflowModelArgumentCaptor.capture(), taskArgumentCaptor.capture(), workflowExecutorArgumentCaptor.capture());
        verify(executionDAOFacade).updateTask(taskArgumentCaptor.capture());
        verify(queueDAO).remove(queueDAOImpl.getQueueName(task), task.getTaskId());
    }

    private void verifyExecuteWithTerminalTask(TaskModel task) {
        ArgumentCaptor<TaskModel> taskArgumentCaptor = ArgumentCaptor.forClass(TaskModel.class);

        verify(queueDAO).remove(queueDAOImpl.getQueueName(task), task.getTaskId());
    }
}