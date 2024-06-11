package com.netflix.conductor.core.execution;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {
        executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(any())).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(systemTask, "test task id");
        
        verify(executionDAOFacade, times(1)).getTaskModel(any());
        verify(executionDAOFacade, times(1)).updateTask(any());
    }

    @Test
    public void testPostponeQuietly() {
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        
        asyncSystemTaskExecutor.postponeQuietly("test queue name", taskModel);
        
        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        asyncSystemTaskExecutor.loadTaskQuietly("test task id");
        
        verify(executionDAOFacade, times(1)).getTaskModel(any());
    }
}