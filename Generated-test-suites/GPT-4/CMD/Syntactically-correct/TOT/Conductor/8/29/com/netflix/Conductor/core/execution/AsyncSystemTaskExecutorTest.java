package com.netflix.conductor.core.execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade mockExecutionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Test
    public void execute_returnsImmediatelyWhenTaskNotFound() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "notFoundTaskId";
        
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verifyZeroInteractions(systemTask);
        verifyZeroInteractions(taskId);
    }

    // Other methods go here

}