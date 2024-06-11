package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private WorkflowSystemTask systemTaskMock;
    private TaskModel taskMock;
    private WorkflowModel workflowMock;

    @Before
    public void setUp() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);
        taskMock = mock(TaskModel.class);
        workflowMock = mock(WorkflowModel.class);
        systemTaskMock = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacadeMock, queueDAOMock,
                metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);
    }

    @Test
    public void when_task_not_found_execute_should_remove_task_from_queue_and_return() {
        String taskId = "fake-task-id";
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        verify(queueDAOMock, times(1)).remove(any(), any());
    }

    @Test
    public void when_task_status_is_terminal_execute_should_remove_task_from_queue_and_return() {
        String taskId = "fake-task-id";
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskMock);
        when(taskMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        verify(queueDAOMock, times(1)).remove(any(), any());
    }

    @Test
    public void when_task_status_is_scheduled_and_im_x_in_progress_limit_reached_execute_should_postpone_task_and_return() {
        String taskId = "fake-task-id";
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskMock);
        when(taskMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskMock)).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        verify(queueDAOMock, times(1)).postpone(any(), any(), anyInt(), anyLong());
    }

    // Similar test methods continue for each logic branch in the execute() method.
}
