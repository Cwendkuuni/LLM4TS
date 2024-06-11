package com.netflix.conductor.core.execution;

import static com.netflix.conductor.common.run.JobRunnerMocks.aTaskModel;
import static com.netflix.conductor.common.run.JobRunnerMocks.aWorkflowModel;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.model.TaskStatus;
import com.netflix.conductor.model.WorkflowStatus;

@Component
public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private String queueName;
    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueName = QueueUtils.getQueueName(task);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        task = aTaskModel().build();
        workflow = aWorkflowModel().build();
        systemTask = mock(WorkflowSystemTask.class);
    }

    @After
    public void tearDown() {
        Mockito.reset(executionDAOFacade, queueDAO, metadataDAO, workflowExecutor);
    }

    @Test
    public void testExecute_TaskNotFound_RemovesDeadTaskFromQueue() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);
        when(queueDAO.remove(queueName, task.getTaskId())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecute_TaskAlreadyCompleted_RemovesTaskFromQueue() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.COMPLETED);
        when(queueDAO.remove(queueName, task.getTaskId())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueName, task.getTaskId());
    }

    @Test
    public void testExecute_ExceedsInProgressLimit_PostponesTask() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        // Given the task is in the queue
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_ExceedsRateLimitPerFrequency_PostponesTask() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, null)).thenReturn(true);

        // Given the task is in the queue
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testExecute_TaskInProgress_UpdatesTask() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);

        // Given the task is in the queue
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).updateTask(task);
        Mockito.verify(task).setStatus(TaskStatus.IN_PROGRESS);
    }

    @Test
    public void testExecute_TaskAsyncComplete_UpdatesTaskAndRemovesFromQueue() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueName, task.getTaskId());
        Mockito.verify(queueDAO).updateTask(task);
        Mockito.verify(task).setStatus(TaskStatus.CANCELED);
    }

    @Test
    public void testExecute_TaskTerminal_UpdatesTaskAndRemovesFromQueue() {
        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), false)).thenReturn(workflow);
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskStatus.SCHEDULED);
        when(task.getStatus()).thenReturn(TaskStatus.TERMINAL);
        when(queueDAO.get(queueName, task.getTaskId())).thenReturn(Optional.of(task));

        asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());

        Mockito.verify(queueDAO).remove(queueName, task.getTaskId());
        Mockito.verify(queueDAO).updateTask(task);
        Mockito.verify(task).setStatus(TaskStatus.TERMINAL);
    }

    @Test
    public void testPostponeQuietly_ErrorPostponingTask_LogsError() {
        when(queueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        Mockito.verify(LOGGER).error("Error postponing task: {} in queue: {}", task.getTaskId(), queueName);
    }

    @Test
    public void testLoadTaskQuietly_NullTaskModel_ReturnsNull() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(null);

        TaskModel actualTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        assertNull(actualTaskModel);
    }

    @Test
    public void testLoadTaskQuietly_NotNullTaskModel_ReturnsTaskModel() {
        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        TaskModel actualTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        assertEquals(task, actualTaskModel);
    }
}