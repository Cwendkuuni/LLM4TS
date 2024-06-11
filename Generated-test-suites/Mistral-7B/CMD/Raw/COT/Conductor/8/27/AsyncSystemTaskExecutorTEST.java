package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.testutils.TestUtils;
import com.netflix.conductor.testutils.WorkflowModelBuilder;
import com.netflix.conductor.testutils.WorkflowSystemTaskTestBuilder;

@SuppressWarnings("unchecked")
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = new ConductorProperties();
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_nullSystemTask_doesNotExecute() {
        String taskId = UUID.randomUUID().toString();
        asyncSystemTaskExecutor.execute(null, taskId);

        TaskModel task = executionDAOFacade.getTaskModel(taskId);
        assertNull(task);
    }

    @Test
    public void testExecute_nullTaskId_doesNotExecute() {
        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, null);

        TaskModel task = executionDAOFacade.getTaskModel("someId");
        assertNull(task);
    }

    @Test
    public void testExecute_existingTask_doesNotExecuteAgain() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // The task should not be updated or removed from the queue
        TaskModel taskFromQueue = queueDAO.getTask(taskId);
        assertSame(task, taskFromQueue);
    }

    @Test
    public void testExecute_completedWorkflow_removesTask() {
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setWorkflowInstanceId(workflowId);

        WorkflowModel workflow = new WorkflowModelBuilder().build();
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // The task should be removed from the queue
        TaskModel taskFromQueue = queueDAO.getTask(taskId);
        assertNull(taskFromQueue);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullExecutionDAOFacade() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                null, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullQueueDAO() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, null, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullMetadataDAO() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, null, conductorProperties, workflowExecutor);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullConductorProperties() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, null, workflowExecutor);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_nullWorkflowExecutor() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskAlreadyInProgress() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskInErrorState() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_taskExecutingAsync() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        systemTask.setAsyncComplete(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_taskExecutionPostponed() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setCallbackAfterSeconds(100);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(queueDAO.getTask(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // The task should be postponed in the queue
        TaskModel taskFromQueue = queueDAO.getTask(taskId);
        assertSame(task, taskFromQueue);
        assertSame(QueuePriority.NORMAL, taskFromQueue.getQueuePriority());
        assertFalse(taskFromQueue.isCallbackAfterExecuted());
    }

    @Test
    public void testExecute_taskExecutionPostponedWithPriority() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setCallbackAfterSeconds(100);
        task.setPriority(QueuePriority.HIGH);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(queueDAO.getTask(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = new WorkflowSystemTaskTestBuilder().build();
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // The task should be postponed in the queue with the given priority
        TaskModel taskFromQueue = queueDAO.getTask(taskId);
        assertSame(task, taskFromQueue);
        assertSame(QueuePriority.HIGH, taskFromQueue.getQueuePriority());
        assertFalse(taskFromQueue.isCallbackAfterExecuted());
    }

    @Test
    public void testPostponeQuietly_existingTask_doesNotPostpone() {
        String queueName = "someQueue";
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(queueDAO.getTask(queueName, taskId)).thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        TaskModel taskFromQueue = queueDAO.getTask(queueName, taskId);
        assertSame(task, taskFromQueue);
    }

    @Test
    public void testPostponeQuietly_nullQueueName_doesNotPostpone() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(queueDAO.getTask(null, taskId)).thenReturn(task);

        asyncSystemTaskExecutor.postponeQuietly(null, task);

        TaskModel taskFromQueue = queueDAO.getTask(null, taskId);
        assertSame(task, taskFromQueue);
    }

    @Test(expected = NullPointerException.class)
    public void testPostponeQuietly_nullTask() {
        String queueName = "someQueue";
        asyncSystemTaskExecutor.postponeQuietly(queueName, null);
    }

    @Test
    public void testLoadTaskQuietly_existingTask_returnsTask() {
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertSame(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_nonExistingTask_returnsNull() {
        String taskId = UUID.randomUUID().toString();

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(loadedTask);
    }
}