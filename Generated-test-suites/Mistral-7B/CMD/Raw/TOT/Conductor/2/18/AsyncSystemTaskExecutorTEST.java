package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;

@SuppressWarnings("unchecked")
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(1L));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(1L));
    }

    @Test
    public void testExecute_taskIsAlreadyCompleted_itIsRemovedFromQueue() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.COMPLETED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskIsTerminal_itIsRemovedFromQueue() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskIsScheduled_itIsAddedToQueue() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO, times(1)).postpone(
                eq(QueueUtils.getQueueName(task)),
                eq(taskId),
                anyInt(),
                eq(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_taskIsNull_exceptionIsThrown() {
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), null);
    }

    @Test
    public void testExecute_taskIsInProgress_itIsUpdatedInDatabase() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId(workflowId);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(ExecutionStatus.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_taskIsInProgress_itCallsStartMethodOfSystemTask() {
        String taskId = "task-1";
        String workflowId = "workflow-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId(workflowId);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(ExecutionStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);
        doNothing().when(systemTask).start(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(any(), any(), any());
    }

    @Test
    public void testExecute_taskIsAsyncComplete_itIsRemovedFromQueue() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doReturn(true).when(systemTask).isAsyncComplete(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }

    @Test
    public void testExecute_taskIsAsyncComplete_itDoesNotCallCallbackMethodOfSystemTask() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.SCHEDULED);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        doReturn(true).when(systemTask).isAsyncComplete(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask, never()).execute(any(), any(), any());
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsDecided() {
        String workflowId = "workflow-1";
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId(workflowId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskIsTerminal_itIsRemovedFromQueue() {
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setStatus(ExecutionStatus.TERMINAL);
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflow-1");
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(eq(QueueUtils.getQueueName(task)), eq(taskId));
    }
}