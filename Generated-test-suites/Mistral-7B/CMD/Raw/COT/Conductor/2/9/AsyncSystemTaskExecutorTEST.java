package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskSpy;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.QueueName;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOStub;
import com.netflix.conductor.dao.metadata.TaskDef;
import com.netflix.conductor.execution.workflow.WorkflowExecutor;
import com.netflix.conductor.metric.Monitors;

@SuppressWarnings("unchecked")
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

    @Autowired
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    private WorkflowSystemTaskSpy systemTaskSpy;

    @Before
    public void setup() {
        systemTaskSpy = new WorkflowSystemTaskSpy();
    }

    @Test
    public void testExecute_validTask_success() {
        // Given
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflowId");
        workflowModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new TaskDef("taskDefName", "taskType", "queueName"));
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        // When
        asyncSystemTaskExecutor.execute(systemTaskSpy, taskId);

        // Then
        verify(systemTaskSpy, times(1)).start(workflowModel, task, workflowExecutor);
    }

    @Test
    public void testExecute_terminalWorkflow_success() {
        // Given
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflowId");
        workflowModel.setStatus(ExecutionStatus.COMPLETED);

        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new TaskDef("taskDefName", "taskType", "queueName"));
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        // When
        asyncSystemTaskExecutor.execute(systemTaskSpy, taskId);

        // Then
        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test(expected = Exception.class)
    public void testExecute_taskNotFound_exception() {
        // Given
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(systemTaskSpy, taskId);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testExecute_exceedsInProgressLimit_postpone() {
        // Given
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflowId");
        workflowModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new TaskDef("taskDefName", "taskType", "queueName"));
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Optional.ofNullable(new Long(1000)));

        // When
        asyncSystemTaskExecutor.execute(systemTaskSpy, taskId);

        // Then
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testExecute_exceedsRateLimitPerFrequency_postpone() {
        // Given
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setWorkflowInstanceId("workflowId");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setRateLimitPerFrequency(1);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowInstanceId("workflowId");
        workflowModel.setStatus(ExecutionStatus.IN_PROGRESS);

        when(metadataDAO.getTaskDef(anyString()))
                .thenReturn(new TaskDef("taskDefName", "taskType", "queueName"));
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
                .thenReturn(workflowModel);
        when(metadataDAO.getTaskDef(anyString())).thenReturn(null);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Optional.ofNullable(new Long(1000)));

        // When
        asyncSystemTaskExecutor.execute(systemTaskSpy, taskId);

        // Then
        verify(queueDAO, times(1)).postpone(anyString(), taskId, anyInt(), anyInt());
    }

    @Test
    public void testPostponeQuietly_postponeTask() {
        // Given
        String queueName = QueueName.TASK_EXECUTION.name();
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");

        // When
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        // Then
        verify(queueDAO, times(1)).postpone(eq(queueName), eq(task.getTaskId()), anyInt(), anyInt());
    }

    @Test
    public void testLoadTaskQuietly_returnNull() {
        // Given
        String taskId = "taskId";

        // When
        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Then
        assertNotNull(taskModel);
        assertEquals(null, taskModel);
    }
}