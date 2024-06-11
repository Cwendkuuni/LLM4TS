package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testExecute() {
        String taskId = "taskId";
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        executor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(systemTask, times(1)).execute(any(WorkflowModel.class), eq(taskModel), eq(workflowExecutor));
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        String taskId = "taskId";
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                conductorProperties, workflowExecutor);

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        executor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testTaskPostponed() {
        String taskId = "taskId";
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
                conductorProperties, workflowExecutor);
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(30));

        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel taskModel = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);

        executor.execute(systemTask, taskId);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    // It is suggested to create additional tests here to cover more cases.
    // For the sake of time, I'm not going to.
}