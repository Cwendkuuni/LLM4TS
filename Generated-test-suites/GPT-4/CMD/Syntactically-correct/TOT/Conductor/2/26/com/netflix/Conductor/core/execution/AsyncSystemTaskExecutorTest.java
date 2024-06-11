
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade mockExecutionDAOFacade;
    @Mock
    private QueueDAO mockQueueDAO;
    @Mock
    private MetadataDAO mockMetadataDAO;
    @Mock
    private ConductorProperties mockConductorProperties;
    @Mock
    private WorkflowExecutor mockWorkflowExecutor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(mockExecutionDAOFacade, mockQueueDAO, 
            mockMetadataDAO, mockConductorProperties, mockWorkflowExecutor);
    }


    @Test
    public void execute_whenTaskIsNotFound_shouldRemoveTaskFromQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "sometaskId";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(mockQueueDAO).remove(systemTask.getTaskType(), taskId);
    }
    
    // Other tests for execute method ...
    

    // Tests for other private methods with reflection if necessary ...
}


