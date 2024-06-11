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
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.queue.QueueDAOImpl;
import com.netflix.conductor.model.execution.WorkflowExecutor;
import com.netflix.conductor.model.execution.WorkflowInstance;
import com.netflix.conductor.model.execution.WorkflowInstanceStatus;
import com.netflix.conductor.model.workflow.WorkflowDefinition;
import com.netflix.conductor.model.workflow.WorkflowTaskDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

public class AsyncSystemTaskExecutorTest {
    private AsyncSystemTaskExecutor executor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private String queueName;
    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executionDAOFacade = mock(ExecutionDAOFacadeImpl.class);
        queueDAO = mock(QueueDAOImpl.class);
        metadataDAO = mock(MetadataDAOImpl.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Arrays.of(1L, "s"));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Arrays.of(1L, "s"));

        executor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
        queueName = QueueUtils.getQueueName("testQueue");
        task = new TaskModel();
        workflow = new WorkflowInstance();
        systemTask = mock(WorkflowSystemTask.class);
    }

    @Test
    public void testExecute_NullTaskId() {
        executor.execute(systemTask, null);
        // Verify no task was loaded
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 0);
        // Verify no task was removed from queue
        assertTrue(queueDAO.removeCallCount(), 0);
    }

    @Test
    public void testExecute_NullSystemTask() {
        executor.execute(null, "taskId");
        // Verify no task was loaded
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 0);
        // Verify no task was removed from queue
        assertTrue(queueDAO.removeCallCount(), 0);
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(null);

        executor.execute(systemTask, "taskId");
        // Verify task was not found and removed from queue
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertTrue(queueDAO.removeCallCount(), 1);
    }

    @Test
    public void testExecute_TaskExecutionCompleted() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        WorkflowTaskDefinition taskDefinition = new WorkflowTaskDefinition();

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.STARTED;
        workflow.setStatus(workflowStatus);
        workflow.setDefinition(workflowDefinition);
        workflow.setTasks(Arrays.asList(taskDefinition));

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(workflowDefinition);

        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(true);
        when(systemTask.getTaskType()).thenReturn("testTaskType");

        executor.execute(systemTask, "taskId");

        // Verify task was found and removed from queue
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertTrue(queueDAO.removeCallCount(), 1);

        // Verify workflow was evaluated
        workflowExecutor.decideCallCount = 1;
    }

    @Test
    public void testExecute_TaskNotTerminal() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.STARTED;
        workflow.setStatus(workflowStatus);
        workflow.setDefinition(new WorkflowDefinition());

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(new WorkflowDefinition());
        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);

        executor.execute(systemTask, "taskId");

        // Verify task was found and postponed in the queue
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertFalse(queueDAO.removeCallCount(), 1);
        assertTrue(queueDAO.postponeCallCount(), 1);
    }

    @Test
    public void testExecute_TaskRateLimitExceeded() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.STARTED;
        workflow.setStatus(workflowStatus);
        workflow.setDefinition(new WorkflowDefinition());

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(new WorkflowDefinition());
        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);
        when(task.getRateLimitPerFrequency()).thenReturn(1L);

        executor.execute(systemTask, "taskId");

        // Verify task was found, postponed in the queue, and removed from execution
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertFalse(queueDAO.removeCallCount(), 1);
        assertTrue(queueDAO.postponeCallCount(), 1);
        assertTrue(executionDAOFacade.updateTaskCallCount(), 1);
    }

    @Test
    public void testExecute_TaskConcurrentLimitExceeded() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.STARTED;
        workflow.setStatus(workflowStatus);
        workflow.setDefinition(new WorkflowDefinition());

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(new WorkflowDefinition());
        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);
        when(task.getMaxConcurrentInstances()).thenReturn(1);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        executor.execute(systemTask, "taskId");

        // Verify task was found, postponed in the queue, and removed from execution
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertFalse(queueDAO.removeCallCount(), 1);
        assertTrue(queueDAO.postponeCallCount(), 1);
        assertTrue(executionDAOFacade.updateTaskCallCount(), 1);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.COMPLETED;
        workflow.setStatus(workflowStatus);

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(new WorkflowDefinition());
        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(true);

        executor.execute(systemTask, "taskId");

        // Verify task was found, removed from queue, and workflow was evaluated
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertTrue(queueDAO.removeCallCount(), 1);
        workflowExecutor.decideCallCount = 1;
    }

    @Test
    public void testExecute_TaskInProgress() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(workflow);

        WorkflowInstanceStatus workflowStatus = WorkflowInstanceStatus.STARTED;
        workflow.setStatus(workflowStatus);

        when(metadataDAO.getTaskDef(ArgumentMatchers.anyString())).thenReturn(new WorkflowDefinition());
        when(systemTask.isAsyncComplete(ArgumentMatchers.any(TaskModel.class)))
                .thenReturn(false);

        executor.execute(systemTask, "taskId");

        // Verify task was found, postponed in the queue, and callback time was set
        assertTrue(executionDAOFacade.getTaskModelCallCount(), 1);
        assertFalse(queueDAO.removeCallCount(), 1);
        assertTrue(queueDAO.postponeCallCount(), 1);

        Optional<Long> callbackAfterSecondsOptional = Optional.ofNullable(task.getCallbackAfterSeconds());
        assertTrue(callbackAfterSecondsOptional.isPresent());
        assertEquals(1L, callbackAfterSecondsOptional.get().longValue());
    }

    @Test
    public void testPostponeQuietly_NullTaskId() {
        executor.postponeQuietly(queueName, null);
        // Verify no task was postponed in the queue
        assertTrue(queueDAO.postponeCallCount(), 0);
    }

    @Test
    public void testPostponeQuietly_NullQueueName() {
        executor.postponeQuietly(null, task);
        // Verify no task was postponed in the queue
        assertTrue(queueDAO.postponeCallCount(), 0);
    }

    @Test
    public void testPostponeQuietly_TaskNotFound() {
        executor.postponeQuietly(queueName, task);
        // Verify no task was postponed in the queue
        assertTrue(queueDAO.postponeCallCount(), 0);
    }

    @Test
    public void testPostponeQuietly() {
        when(executionDAOFacade.getTaskModel(ArgumentMatchers.anyString())).thenReturn(task);

        executor.postponeQuietly(queueName, task);
        // Verify task was postponed in the queue
        assertTrue(queueDAO.postponeCallCount(), 1);
    }
}