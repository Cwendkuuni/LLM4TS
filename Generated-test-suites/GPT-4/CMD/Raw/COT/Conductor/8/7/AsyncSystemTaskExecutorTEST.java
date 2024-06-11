package com.netflix.conductor.core.execution;

import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void AsyncSystemTaskExecutorTest_Constructor() {
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        assertNotNull(asyncSystemTaskExecutor);
    }
    
    @Test
    public void testExecuteWithException() {

        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        String testCaseTaskId = "testTaskId";
        when(systemTaskMock.isTaskRetrievalRequired()).thenReturn(false);

        willThrow(RuntimeException.class).given(executionDAOFacade).updateTask(any());

        asyncSystemTaskExecutor.execute(systemTaskMock, testCaseTaskId);

        verify(executionDAOFacade, times(1)).getTaskModel(testCaseTaskId);
        verify(executionDAOFacade, times(1)).updateTask(any());
    }
}