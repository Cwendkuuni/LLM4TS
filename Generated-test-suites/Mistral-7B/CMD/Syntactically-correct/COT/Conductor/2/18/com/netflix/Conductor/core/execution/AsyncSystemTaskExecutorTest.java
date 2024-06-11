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

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueueName;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(
                org.apache.commons.lang3.time.Duration.ofSeconds(1000L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(
                org.apache.commons.lang3.time.Duration.ofSeconds(5000L));
    }

    @Test
    public void testExecute_nullTask_removesDeadTaskFromQueue() {
        String taskId = "test-task-id";
        String taskQueue = QueueUtils.getQueueName(new TaskModel());
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq(taskQueue), eq(taskId));
    }

    @Test
    public void testExecute_terminalTask_removesCompletedTaskFromQueue() {
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.COMPLETED);
        String taskQueue = QueueUtils.getQueueName(task);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(1L);
        workflow.setStatus(Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(task.getId().toString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, task.getId().toString());

        verify(queueDAO).remove(eq(taskQueue), eq(task.getId().toString()));
    }

    @Test
    public void testExecute_exceedsInProgressLimit_postponesTask() {
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getId().toString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, task.getId().toString());

        verify(queueDAO).postpone(anyString(), eq(task.getId().toString()), anyInt(), eq(5000L));
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency_postponesTask() {
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testDefName");
        task.setRateLimitPerFrequency(5);

        when(executionDAOFacade.getTaskModel(task.getId().toString())).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskDefinitionModel());

        asyncSystemTaskExecutor.execute(systemTask, task.getId().toString());

        verify(queueDAO).postpone(anyString(), eq(task.getId().toString()), anyInt(), eq(5000L));
    }

    @Test
    public void testExecute_workflowCompleted_decidesWorkflow() {
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.SCHEDULED);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId(1L);
        workflow.setStatus(Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(task.getId().toString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflow.getId(), false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, task.getId().toString());

        verify(workflowExecutor).decide(workflow.getId());
    }

    @Test(expected = Exception.class)
    public void testExecute_workflowNotFound_throwsException() {
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(task.getId().toString())).thenReturn(task);

        doThrow(new RuntimeException("Test exception")).when(executionDAOFacade).getWorkflowModel(
                anyLong(), anyBoolean());

        asyncSystemTaskExecutor.execute(systemTask, task.getId().toString());
    }

    @Test
    public void testPostponeQuietly_queueExists_postponesTask() {
        String queueName = QueueName.SYSTEM_TASK_QUEUE.name();
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(queueDAO.getQueueSize(queueName)).thenReturn(1);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO).postpone(eq(queueName), eq(task.getId().toString()), anyInt(), eq(5000L));
    }

    @Test
    public void testPostponeQuietly_queueDoesNotExist_doesNotPostponeTask() {
        String queueName = "nonExistentQueue";
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(queueDAO.getQueueSize(queueName)).thenReturn(0);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        verify(queueDAO, never()).postpone(anyString(), eq(task.getId().toString()), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_taskExists_returnsTask() {
        String taskId = "test-task-id";
        TaskModel task = new TaskModel();
        task.setId(1L);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(task, loadedTask);
    }

    @Test
    public void testLoadTaskQuietly_taskDoesNotExist_returnsNull() {
        String taskId = "test-task-id";

        TaskModel loadedTask = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(loadedTask);
    }
}