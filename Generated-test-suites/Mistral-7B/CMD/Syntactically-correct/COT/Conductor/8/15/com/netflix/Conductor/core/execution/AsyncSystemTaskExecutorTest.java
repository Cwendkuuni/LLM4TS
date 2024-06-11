package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.netflix.conductor.dao.QueueDAO.Message;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtils;
import com.netflix.conductor.utils.TestExecutor;

@SuppressWarnings("deprecation")
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
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        TestExecutor testExecutor = new TestExecutor();
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(testExecutor.getDuration(1L, TimeUnit.SECONDS));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(testExecutor.getDuration(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(new WorkflowModel());
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskIsAlreadyScheduled_queueIsNotRemoved() {
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(new WorkflowModel());
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        verify(queueDAO, never()).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test
    public void testExecute_taskIsTerminal_queueIsRemoved() {
        TaskModel task = new TaskModel();
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getWorkflowModel("workflowId", false)).thenReturn(new WorkflowModel());
        when(executionDAOFacade.getTaskModel("taskId")).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, "taskId");

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(queueDAO).remove(QueueUtils.getQueueName(task), task.getTaskId());
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_taskIsNull() {
        asyncSystemTaskExecutor.execute(systemTask, null);
    }

    // Add more test cases for other methods and edge cases here

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotFound() {
        String taskId = "taskId";

        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(task);
    }

    @Test
    public void testLoadTaskQuietly_taskIsFound() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertSame(task, loadedTask);
    }

    // Add more test cases for other methods and edge cases here

    @Test(expected = NullPointerException.class)
    public void testExecute_executionDAOFacadeIsNull() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(null, null, null, null, null);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
    }

    // Add more test cases for other dependencies and edge cases here
}