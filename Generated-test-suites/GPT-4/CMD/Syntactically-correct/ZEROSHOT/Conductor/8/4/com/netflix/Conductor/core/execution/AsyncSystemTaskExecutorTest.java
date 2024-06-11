package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConstructorProperties constructorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowSystemTask systemTask;


    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void loadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException("Test exception"));
        asyncSystemTaskExecutor.loadTaskQuietly("task_id");
    }

    @Test
    public void postponeQuietly() {
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyLong())).thenThrow(new RuntimeException("Test exception"));
        asyncSystemTaskExecutor.postponeQuietly("queue_name", new TaskModel());
    }

    @Test
    public void execute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(new TaskModel());
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(new WorkflowModel());
        when(systemTask.isAsyncComplete(any())).thenReturn(false);
        asyncSystemTaskExecutor.execute(systemTask, "task_id");
        verify(executionDAOFacade).updateTask(any());
    }
}
