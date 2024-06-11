package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO.QueuePriority;
import com.netflix.conductor.metrics.Monitors;

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private TaskModel task;
    private String taskId = "taskId";
    private String queueName;
    private WorkflowModel workflow;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        task = mock(TaskModel.class);
        queueName = QueueUtils.getQueueName(task);
        systemTask = mock(WorkflowSystemTask.class);
        when(task.getTaskDefName()).thenReturn("testTaskDefName");
        when(task.getWorkflowInstanceId()).thenReturn("testWorkflowId");
        when(task.getQueueWaitTime()).thenReturn(1L);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, systemTask.isTaskRetrievalRequired()))
                .thenReturn(workflow);
        when(queueDAO.remove(queueName, taskId)).thenReturn(1);

        Monitors mockMonitors = mock(Monitors.class);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(mockMonitors);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertNull(task);
    }

    @Test
    public void testExecute_terminalTask() {
        task.setStatus(ExecutionStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId).equals(1));
    }

    @Test(expected = Exception.class)
    public void testExecute_taskExecutionLimitExceeded() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = Exception.class)
    public void testExecute_rateLimitExceeded() {
        when(task.getRateLimitPerFrequency()).thenReturn(1);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, mockMonitors)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test
    public void testExecute_taskNotInScheduleState() {
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId).equals(1));
    }

    @Test
    public void testExecute_taskExecutedSuccessfully() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(true);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(false);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(queueDAO.remove(queueName, taskId).equals(1));
    }

    @Test
    public void testExecute_taskExecutedAsync() {
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(systemTask.isAsyncComplete(task)).thenReturn(false);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertSame(queuePriority(QueuePriority.HIGHEST), task.getWorkflowPriority());
        assertFalse(queueDAO.remove(queueName, taskId).equals(1));
    }

    @Test
    public void testExecute_workflowTerminal() {
        task.setStatus(ExecutionStatus.SCHEDULED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        assertFalse(workflowExecutor.decide(workflowId).equals(1));
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        assertFalse(queueDAO.remove(queueName, task.getTaskId()).equals(1));
    }

    @Test
    public void testLoadTaskQuietly_nullTask() {
        asyncSystemTaskExecutor.loadTaskQuietly(null);

        assertNull(task);
    }

    private QueuePriority queuePriority(QueuePriority queuePriority) {
        return queuePriority;
    }
}