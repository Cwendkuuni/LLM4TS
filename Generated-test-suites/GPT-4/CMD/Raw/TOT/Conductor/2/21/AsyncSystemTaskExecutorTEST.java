import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;

    @Test
    public void testExecuteTaskNotFound() {
        // Prepare data
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        // Execute method
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        // Verify behavior
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteTaskFound() {
        // Prepare data
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(WorkflowModel.Status.RUNNING);

        // Execute method
        asyncSystemTaskExecutor.execute(workflowSystemTask, "testTaskId");

        // Verify behavior
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    // You should create more test functions to cover other part of the class as appropriate
}