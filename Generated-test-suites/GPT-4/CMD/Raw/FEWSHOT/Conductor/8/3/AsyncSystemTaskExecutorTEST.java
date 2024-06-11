package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void executeTest() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "1";
        
        TaskModel task = new TaskModel();
        WorkflowModel workflow = new WorkflowModel();
        
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(queueDAO.postpone(anyString(),anyString(),anyInt(),anyLong())).thenReturn(true);
        when(queueDAO.remove(anyString(),anyString())).thenReturn(true);
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflow.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(2)).getTaskModel(anyString());
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}