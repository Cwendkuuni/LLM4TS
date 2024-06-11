
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @Mock
    WorkflowSystemTask systemTask;
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private final String taskId = "TaskID";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(1));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(1));
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO,
        conductorProperties, workflowExecutor);
    }
  
    @Test
    public void testExecute() {
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
 
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(0)).remove(any(), any());
    }
  
    @Test
    public void testExecute_TaskNotFoundInDB() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(systemTask.getTaskType(), taskId);
    }
  
    @Test
    public void testExecute_DeleteTaskFromDBThrowsException() {
        doThrow(new RuntimeException()).when(queueDAO).remove(systemTask.getTaskType(), taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(systemTask.getTaskType(), taskId);
    }
}

