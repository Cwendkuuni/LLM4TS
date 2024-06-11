package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.netflix.conductor.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private String queueName;
    private String taskId;
    private TaskModel task;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade,
                        queueDAO,
                        metadataDAO,
                        conductorProperties,
                        workflowExecutor);

        task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskDefName("taskDefName");
        task.setStatus(TaskModel.Status.SCHEDULED);
        workflow = new WorkflowModel();
        workflow.setWorkflowInstanceId(task.getWorkflowInstanceId());
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        systemTask = mock(WorkflowSystemTask.class);

        queueName = QueueUtils.getQueueName(task);
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        assertNotNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testLoadTaskQuietlyNull() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoMoreInteractions(executionDAOFacade);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        when(executionDAOFacade.getWorkflowModel(
                workflow.getWorkflowInstanceId(), ArgumentMatchers.booleanValue(false)))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor).decide(workflow.getWorkflowInstanceId());
        verifyNoMoreInteractions(executionDAOFacade);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_InProgressLimitExceeded() {
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_RateLimitPerFrequencyExceeded() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                task, metadataDAO.getTaskDef(task.getTaskDefName())))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        workflow.setStatus(WorkflowModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verifyNoMoreInteractions(executionDAOFacade);
        verify(queueDAO).remove(queueName, taskId);
    }

    @Test
    public void testExecute_SystemTaskCallbackDuration() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(workflowExecutor.decide(workflow.getWorkflowInstanceId()))
                .thenReturn(WorkflowExecutionResult.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        Thread.sleep(systemTaskCallbackTime + queueTaskMessagePostponeSecs);
        verify(queueDAO).remove(queueName, taskId);
        long endTime = System.currentTimeMillis();
        assertTrue(endTime >= startTime + systemTaskCallbackTime);
    }
}