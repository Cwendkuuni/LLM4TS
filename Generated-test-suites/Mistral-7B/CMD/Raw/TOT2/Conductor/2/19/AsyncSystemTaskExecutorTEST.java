package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadTaskQuietly() {
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        TaskModel taskModelResult = asyncSystemTaskExecutor.loadTaskQuietly("taskId");

        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        assertEquals(taskModel, taskModelResult);
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).remove(anyString(), taskId);
    }

    @Test
    public void testExecute_TaskNotInProgressOrAsyncComplete() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(
                anyString(),
                taskId,
                anyInt(),
                anyInt());
    }

    @Test
    public void testExecute_TaskInProgress() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);

        doAnswer(invocation -> {
            TaskModel task = invocation.getArgument(0);
            task.setStatus(TaskModel.Status.IN_PROGRESS);
            return null;
        }).when(executionDAOFacade).updateTask(taskModel);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test(expected = Exception.class)
    public void testExecute_ExceptionThrown() {
        String taskId = "taskId";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        doThrow(new Exception("Test Exception")).when(systemTask).execute(any(), any(), any());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        // This test should fail as the exception is not caught and logged properly
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel taskModel = mock(TaskModel.class);

        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);

        verify(queueDAO, times(1)).postpone(
                queueName,
                taskModel.getTaskId(),
                taskModel.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
    }
}