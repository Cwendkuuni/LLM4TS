package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.utils.test.TestExecutionDAOFacade;
import com.netflix.conductor.utils.test.TestMetadataDAO;
import com.netflix.conductor.utils.test.TestQueueDAO;

@SuppressWarnings("deprecation")
public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = new TestExecutionDAOFacade();
        queueDAO = new TestQueueDAO();
        metadataDAO = new TestMetadataDAO();
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(executionDAOFacade);
        Mockito.verifyNoMoreInteractions(queueDAO);
        Mockito.verifyNoMoreInteractions(metadataDAO);
        Mockito.verifyNoMoreInteractions(conductorProperties);
        Mockito.verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_removeTaskFromQueue() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskIsTerminalState_removeTaskFromQueue() {
        String taskId = "task-123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_taskIsScheduledState_postponeTask() {
        String queueName = QueueUtils.getQueueName("workflow-123");
        String taskId = "task-123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setWorkflowPriority(1);
        task.setCallbackAfterSeconds(5000);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(
                Mockito.mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(eq(queueName), eq(taskId), eq(1), eq(5000));
    }

    @Test
    public void testExecute_taskIsInProgressState_executeSystemTask() {
        String queueName = QueueUtils.getQueueName("workflow-123");
        String taskId = "task-123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setId("workflow-123");
        workflow.setName("workflow-name");
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        WorkflowSystemTask workflowSystemTask = new WorkflowSystemTaskImpl();
        workflowSystemTask.setTask(task);
        workflowSystemTask.setWorkflow(workflow);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(eq("workflow-123"), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask).start(eq(workflow), eq(task), eq(workflowExecutor));
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsInProgressLimit_postponeTask() {
        String queueName = QueueUtils.getQueueName("workflow-123");
        String taskId = "task-123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(
                Mockito.mock(WorkflowSystemTask.class), taskId);

        fail("Expected IllegalStateException to be thrown");
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_exceedsRateLimitPerFrequency_postponeTask() {
        String queueName = QueueUtils.getQueueName("workflow-123");
        String taskId = "task-123";

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(Mockito.mock(TaskDefinition.class));
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, Mockito.any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(
                Mockito.mock(WorkflowSystemTask.class), taskId);

        fail("Expected IllegalStateException to be thrown");
    }
}