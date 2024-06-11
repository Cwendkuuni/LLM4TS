package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacadeMock;
    @Mock
    private QueueDAO queueDAOMock;
    @Mock
    private MetadataDAO metadataDAOMock;
    @Mock
    private ConductorProperties conductorPropertiesMock;
    @Mock
    private WorkflowExecutor workflowExecutorMock;
    @Mock
    private WorkflowSystemTask workflowSystemTaskMock;
    @Mock
    private TaskModel taskModelMock;
    @Mock
    private WorkflowModel workflowModelMock;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacadeMock, queueDAOMock,
            metadataDAOMock, conductorPropertiesMock, workflowExecutorMock);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModelMock);
        when(executionDAOFacadeMock.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModelMock);
        when(workflowSystemTaskMock.isTaskRetrievalRequired()).thenReturn(false);

        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "SampleTask1");
        verify(executionDAOFacadeMock, times(1)).getTaskModel(anyString());

        verify(workflowSystemTaskMock, times(1)).isAsyncComplete(taskModelMock);
        verify(queueDAOMock, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly_withoutException() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenReturn(taskModelMock);
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("SampleTask1");
        assertSame(taskModelMock, task);
    }

    @Test(expected=RuntimeException.class)
    public void testLoadTaskQuietly_withException() {
        when(executionDAOFacadeMock.getTaskModel(anyString())).thenThrow(new RuntimeException());
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly("SampleTask1");
        assertNull(task);
    }

    @Test
    public void testPostponeQuietly_withoutException() {
        asyncSystemTaskExecutor.postponeQuietly("SampleQueue1", taskModelMock);
        verify(queueDAOMock, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testPostponeQuietly_withException() {
        doThrow(RuntimeException.class).when(queueDAOMock).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.postponeQuietly("SampleQueue1", taskModelMock);
    }
}
