package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskStub;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueuePriority;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.WorkflowModelStub;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.QueueUtilsStub;

public class AsyncSystemTaskExecutorTest {

    private static final Logger log = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private QueueDAOImpl queueDAOImpl;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAOImpl = new QueueDAOImpl();

        when(executionDAOFacade.getTaskModel(Mockito.anyString())).thenCallRealMethod();
        when(executionDAOFacade.getWorkflowModel(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(new WorkflowModelStub());
        when(metadataDAO.getTaskDef(Mockito.anyString())).thenReturn(new TaskModel());

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @After
    public void tearDown() {
        queueDAOImpl.deleteAll();
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is not updated since it is already completed
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
        assertFalse(taskModelCaptor.getValue().isModified());
    }

    @Test
    public void testExecute_taskIsNotAvailable() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is not updated since it is not available
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertNull(taskModelCaptor.getValue());
    }

    @Test
    public void testExecute_taskIsScheduled() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is updated and removed from the queue
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
        assertTrue(taskModelCaptor.getValue().isModified());
        assertEquals(QueuePriority.HIGH, taskModelCaptor.getValue().getQueuePriority());
        queueDAOImpl.assertQueueSize(1);
        queueDAOImpl.getQueue(QueueUtils.getQueueName(task)).stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .ifPresent(queueTask -> queueDAOImpl.delete(queueTask));
    }

    @Test
    public void testExecute_taskIsInProgress() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModelStub();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowInstanceId(), false))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is updated and not removed from the queue
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
        assertTrue(taskModelCaptor.getValue().isModified());
        assertEquals(QueuePriority.HIGH, taskModelCaptor.getValue().getQueuePriority());
        queueDAOImpl.assertQueueSize(1);
    }

    @Test
    public void testExecute_taskIsTerminal() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        task.setEndTime(System.currentTimeMillis() - Duration.ofSeconds(10).toMillis());

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is not updated since it is already completed
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertFalse(taskModelCaptor.getValue().isModified());
    }

    @Test
    public void testExecute_queueIsFull() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(executionDAOFacade.exceedsInProgressLimit(task))
                .thenReturn(Optional.of(new RuntimeException("Exceeded in progress limit")));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is postponed in the queue
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
        assertTrue(taskModelCaptor.getValue().isModified());
        assertEquals(QueuePriority.HIGH, taskModelCaptor.getValue().getQueuePriority());
        queueDAOImpl.assertQueueSize(1);
        queueDAOImpl.getQueue(QueueUtils.getQueueName(task)).stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .ifPresent(queueTask -> {
                    assertEquals(Long.parseLong(taskId), queueTask.getTaskId());
                    assertEquals(QueuePriority.HIGH, queueTask.getQueuePriority());
                    assertEquals(System.currentTimeMillis(), queueTask.getCallbackAfterMillis());
                });
    }

    @Test
    public void testExecute_taskIsRateLimited() {
        String taskId = "test-task-id";
        WorkflowSystemTask systemTask = new WorkflowSystemTaskStub();

        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null))
                .thenReturn(new RuntimeException("Exceeded rate limit"));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Verify that the task is postponed in the queue
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
        assertTrue(taskModelCaptor.getValue().isModified());
        assertEquals(QueuePriority.HIGH, taskModelCaptor.getValue().getQueuePriority());
        queueDAOImpl.assertQueueSize(1);
        queueDAOImpl.getQueue(QueueUtils.getQueueName(task)).stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .ifPresent(queueTask -> {
                    assertEquals(Long.parseLong(taskId), queueTask.getTaskId());
                    assertEquals(QueuePriority.HIGH, queueTask.getQueuePriority());
                    assertEquals(System.currentTimeMillis(), queueTask.getCallbackAfterMillis());
                });
    }

    @Test
    public void testPostponeQuietly_taskIsNotAvailable() {
        String queueName = "test-queue-name";
        TaskModel task = new TaskModel();

        when(queueDAO.getQueue(queueName)).thenReturn(null);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that the task is not added to the queue since it is not available
        queueDAOImpl.assertQueueSize(0);
    }

    @Test
    public void testPostponeQuietly_taskIsAlreadyPostponed() {
        String queueName = "test-queue-name";
        String taskId = "test-task-id";
        TaskModel task = new TaskModel();

        when(queueDAO.getQueue(queueName)).thenReturn(QueueUtilsStub.getQueueWithSingleTask(taskId));

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that the task is not added to the queue since it is already postponed
        queueDAOImpl.assertQueueSize(1);
    }

    @Test
    public void testPostponeQuietly_taskIsPostponed() {
        String queueName = "test-queue-name";
        String taskId = "test-task-id";
        TaskModel task = new TaskModel();

        when(queueDAO.getQueue(queueName)).thenReturn(QueueUtilsStub.getEmptyQueue());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Verify that the task is added to the queue
        queueDAOImpl.assertQueueSize(1);
        queueDAOImpl.getQueue(queueName).stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .ifPresent(queueTask -> {
                    assertEquals(Long.parseLong(taskId), queueTask.getTaskId());
                    assertEquals(QueuePriority.HIGH, queueTask.getQueuePriority());
                    assertEquals(System.currentTimeMillis(), queueTask.getCallbackAfterMillis());
                });
    }

    @Test
    public void testLoadTaskQuietly_taskIsNotAvailable() {
        String taskId = "test-task-id";

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Verify that the task is null since it is not available
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertNull(taskModelCaptor.capture());
    }

    @Test
    public void testLoadTaskQuietly_taskIsAvailable() {
        String taskId = "test-task-id";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Verify that the task is returned
        ArgumentCaptor<TaskModel> taskModelCaptor = ArgumentCaptor.forClass(TaskModel.class);
        assertEquals(executionDAOFacade.getClass(), asyncSystemTaskExecutor.getExecutionDAOFacade());
        assertEquals(taskModelCaptor.capture(), task);
    }
}