package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.TestUtils;

import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    @MockBean
    private ExecutionDAOFacade executionDAOFacade;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private MetadataDAO metadataDAO;

    @MockBean
    private QueueUtils queueUtils;

    @MockBean
    private Monitors monitors;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Autowired
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Autowired
    private ConductorProperties conductorProperties;

    private String queueName;
    private String taskId;
    private String workflowId;
    private WorkflowModel workflowModel;
    private WorkflowSystemTask systemTask;

    @Before
    public void setup() {
        queueName = "testQueue";
        taskId = "testTaskId";
        workflowId = "testWorkflowId";
        workflowModel = TestUtils.createWorkflowModel(workflowId);
        systemTask = Mockito.mock(WorkflowSystemTask.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(TestUtils.createTaskModel(taskId));
        Mockito.when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflowModel);
        Mockito.when(workflowExecutor.decide(workflowId)).thenReturn(true);
        Mockito.when(queueUtils.getQueueName(Mockito.any(TaskModel.class))).thenReturn(queueName);
        Mockito.when(monitors.recordQueueWaitTime(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(1L);
    }

    @Test
    public void testExecute_taskNotFound() {
        // Given
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(queueName, taskId);
        verify(executionDAOFacade).updateTask(null);
        verify(queueDAO).remove(queueName, taskId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskInProgress() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO, never()).postpone(queueName, taskId, Mockito.anyInt(), Mockito.anyLong());
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_taskScheduled() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).postpone(queueName, taskId, Mockito.anyInt(), Mockito.anyLong());
        verify(executionDAOFacade).updateTask(task);
    }

    @Test
    public void testExecute_taskTerminal() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(queueName, taskId);
        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_queueFull() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When - Expected exception
        try {
            asyncSystemTaskExecutor.execute(systemTask, taskId);
            fail("Expected QueueFullException");
        } catch (Exception e) {
            // Then
            verify(queueDAO, never()).postpone(queueName, taskId, Mockito.anyInt(), Mockito.anyLong());
            verify(executionDAOFacade).updateTask(task);
            Monitors.verifyNoRecordings(monitors, "queueWaitTime", queueName);
            assertEquals(QueueDAOImpl.QUEUE_FULL_EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void testExecute_rateLimitPerFrequency() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When - Expected exception
        try {
            asyncSystemTaskExecutor.execute(systemTask, taskId);
            fail("Expected RateLimitException");
        } catch (Exception e) {
            // Then
            verify(queueDAO, never()).postpone(queueName, taskId, Mockito.anyInt(), Mockito.anyLong());
            verify(executionDAOFacade).updateTask(task);
            Monitors.verifyNoRecordings(monitors, "queueWaitTime", queueName);
            assertEquals(
                    String.format("TaskRateLimitExceeded: %s:{} for limit:{}", taskId, task.getTaskDefName(), task.getRateLimitPerFrequency()), e.getMessage());
        }
    }

    @Test
    public void testExecute_workflowTerminal() {
        // Given
        TaskModel task = TestUtils.createTaskModel(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = TestUtils.createWorkflowModel(workflowId);
        workflow.setStatus(WorkflowModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, false)).thenReturn(workflow);

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(queueName, taskId);
        verify(executionDAOFacade).updateTask(task);
        verify(workflowExecutor).decide(workflowId);
    }
}