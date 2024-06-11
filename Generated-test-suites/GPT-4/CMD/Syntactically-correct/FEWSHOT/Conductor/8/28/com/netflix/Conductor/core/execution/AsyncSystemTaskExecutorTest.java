package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Duration;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @Mock
    ExecutionDAOFacade executionDAOFacade;
    @Mock
    QueueDAO queueDAO;
    @Mock
    MetadataDAO metadataDAO;
    @Mock
    ConductorProperties conductorProperties;
    @Mock
    WorkflowExecutor workflowExecutor;

    private final String TEST_ID = "test_id";

    @Test
    public void testExecuteNotFoundTask() {
        when(executionDAOFacade.getTaskModel(TEST_ID)).thenReturn(null);
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.execute(Mockito.mock(WorkflowSystemTask.class), TEST_ID);
        verify(executionDAOFacade).getTaskModel(TEST_ID);
    }

    @Test
    public void testExecuteTerminalTask() {
        WorkflowSystemTask task = Mockito.mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = Mockito.mock(TaskModel.class);

        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(TEST_ID)).thenReturn(taskModelMock);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.execute(task, TEST_ID);

        verify(executionDAOFacade).getTaskModel(TEST_ID);
        verify(taskModelMock).getStatus();
        verify(queueDAO).remove(anyString(), eq(TEST_ID));
    }

    @Test
    public void testExecuteInProgressLimitExceeded() {
        WorkflowSystemTask task = Mockito.mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = Mockito.mock(TaskModel.class);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(TEST_ID)).thenReturn(taskModelMock);
        when(executionDAOFacade.exceedsInProgressLimit(taskModelMock)).thenReturn(true);
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.execute(task, TEST_ID);
        verify(executionDAOFacade).getTaskModel(TEST_ID);
        verify(taskModelMock).getStatus();
        verify(executionDAOFacade).exceedsInProgressLimit(taskModelMock);
        verify(queueDAO).postpone(anyString(), eq(TEST_ID), anyInt(), anyLong());
    }

    // Similar methods to test different task states can be added

    @Test
    public void testLoadTaskQuietlyException() {
        when(executionDAOFacade.getTaskModel(TEST_ID)).thenThrow(RuntimeException.class);
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        TaskModel result = executor.loadTaskQuietly(TEST_ID);
        verify(executionDAOFacade).getTaskModel(TEST_ID);
        org.junit.Assert.assertNull(result);
    }
}