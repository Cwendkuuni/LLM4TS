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
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskImpl;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.workflow.WorkflowModelImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TestUtils;

@SuppressWarnings({"unchecked"})
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
    private Monitors monitors;

    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowModel = new WorkflowModelImpl();
        task = new TaskModel();
        when(executionDAOFacade.getExecutionDAO())
                .thenReturn(new ExecutionDAOFacadeImpl());
        when(queueDAO).thenReturn(new QueueDAOImpl());
        when(metadataDAO).thenReturn(new MetadataDAOImpl());
        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(TestUtils.getDuration(1000L));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(TestUtils.getDuration(5000L));
        when(workflowExecutor.decide(anyString()))
                .thenReturn(Optional.empty());
        when(monitors.recordQueueWaitTime(anyString(), anyLong()))
                .thenReturn(null);
    }

    @Test
    public void execute_taskIsNotFound_shouldNotExecuteTaskAndRemoveItFromQueue() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq("queueName"), eq(taskId));
        verifyNoInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
    }

    @Test
    public void execute_taskIsTerminal_shouldRemoveItFromQueue() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        task.setStatus(TaskModel.Status.TERMINAL);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(eq("queueName"), eq(taskId));
        verifyNoInteractions(executionDAOFacade, metadataDAO, workflowExecutor);
    }

    @Test
    public void execute_taskIsScheduled_shouldCheckIfItCanBeExecuted() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).exceedsInProgressLimit(task);
        verifyZeroInteractions(queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void execute_taskIsScheduledAndCanBeExecuted_shouldExecuteTaskAndUpdateItsStatus() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).start(eq(workflowModel), eq(task), eq(workflowExecutor));
        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void execute_taskIsScheduledButCannotBeExecuted_shouldPostponeIt() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                eq("queueName"),
                eq(taskId),
                eq(task.getWorkflowPriority()),
                eq(queueTaskMessagePostponeSecs));
    }

    @Test
    public void execute_taskIsInProgress_shouldUpdateItsStatusAndInvokeDecideOnWorkflowExecutor() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false))
                .thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).execute(eq(workflowModel), eq(task), eq(workflowExecutor));
        verify(workflowExecutor).decide(eq(workflowModel.getId()));
        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void execute_taskIsTerminalButWorkflowIsNot_shouldKeepItInQueue() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowModel.getId(), false))
                .thenReturn(new WorkflowModelImpl());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyZeroInteractions(queueDAO);
        verify(workflowExecutor).decide(eq(workflowModel.getId()));
    }

    @Test
    public void execute_taskAsyncCompleteButNotScheduled_shouldRemoveItFromQueue() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        boolean isTaskAsyncComplete = true;

        asyncSystemTaskExecutor.execute(new WorkflowSystemTaskImpl(), taskId);

        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void execute_taskAsyncCompleteAndScheduled_shouldRemoveItFromQueue() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        boolean isTaskAsyncComplete = true;

        asyncSystemTaskExecutor.execute(new WorkflowSystemTaskImpl(), taskId);

        verify(queueDAO).remove(eq("queueName"), eq(taskId));
    }

    @Test
    public void execute_taskExecutionCompleted_shouldDecideOnWorkflow() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);
        boolean hasTaskExecutionCompleted = true;

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflowModel.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void execute_taskExecutionThrowsException_shouldLogErrorAndKeepTaskInQueue() {
        String taskId = "taskId";
        String queueName = QueueUtils.getQueueName(task);

        doThrow(new RuntimeException("Test Exception"))
                .when(systemTask)
                .start(eq(workflowModel), eq(task), eq(workflowExecutor));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(monitors).error(
                eq(AsyncSystemTaskExecutor.class.getSimpleName()),
                eq("executeSystemTask"));
        verifyZeroInteractions(queueDAO);
    }
}