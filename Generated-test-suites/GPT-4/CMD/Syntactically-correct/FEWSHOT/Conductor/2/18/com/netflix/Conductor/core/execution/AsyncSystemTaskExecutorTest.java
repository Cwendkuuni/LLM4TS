
package com.netflix.conductor.core.execution;

import org.junit.Test;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute_WithNonExistentTask() {
        final String taskId = "non_existent_task";
        WorkflowSystemTask systemTaskMock = new WorkflowSystemTask();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);
    }

    @Test
    public void testExecute_WithExistentTask() {
        final String taskId = "existent_task";
        WorkflowSystemTask systemTaskMock = new WorkflowSystemTask();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(new TaskModel());
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);
    }
}
