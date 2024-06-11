package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowInstance;
import com.netflix.conductor.model.WorkflowModel.Status;

@SuppressWarnings("deprecation")
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

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowSystemTask systemTask;

    @Autowired
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(10));
        when(metadataDAO.getTaskDef(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testExecute_taskIsTerminal_queueIsRemoved() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTask");
        task.setWorkflowInstanceId(UUID.randomUUID().toString());
        task.setWorkflowPriority(1);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueDAO.get(anyString(), eq(taskId)))
                .thenReturn(new QueueModel("testQueue", taskId, 0, 0L));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(queueDAO).remove(eq("testQueue"), eq(taskId));
    }

    @Test
    public void testExecute_taskIsTerminal_workflowIsCompleted() {
        // Given
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTask");
        task.setWorkflowInstanceId(workflowId);
        task.setWorkflowPriority(1);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true))
                .thenReturn(new WorkflowModel().withStatus(Status.COMPLETED));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testExecute_taskIsNotTerminal_workflowIsNotCompleted() {
        // Given
        String taskId = UUID.randomUUID().toString();
        String workflowId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTask");
        task.setWorkflowInstanceId(workflowId);
        task.setWorkflowPriority(1);
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(workflowId, true))
                .thenReturn(new WorkflowModel().withStatus(Status.RUNNING));

        // When
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // Then
        verify(workflowExecutor, never()).decide(workflowId);
    }

    @Test(expected = Exception.class)
    public void testExecute_exceptionIsThrown() {
        // Given
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setTaskDefName("testTask");
        task.setStatus(ExecutionStatus.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        // When & Then
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(monitors).error(AsyncSystemTaskExecutor.class.getSimpleName(), "executeSystemTask");
    }

    @Test
    public void testPostponeQuietly_queueIsPostponed() {
        // Given
        String queueName = "testQueue";
        String taskId = UUID.randomUUID().toString();
        TaskModel task = new TaskModel();
        task.setId(taskId);
        task.setWorkflowPriority(1);

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO).postpone(eq(queueName), eq(taskId), eq(1), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_taskIsLoaded() {
        // Given
        String taskId = UUID.randomUUID().toString();

        // When
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNotNull(task);
    }
}