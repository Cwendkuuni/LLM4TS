import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_SuccessfulExecution() {
        //Setup
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        when(systemTask.isAsyncComplete(any())).thenReturn(true);

        String taskId = "12345";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        //Execute
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        //Verify
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskNotFound() {
        //Setup
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        //Execute
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        //Verify
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_ExceptionThrown() {
        //Setup
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "12345";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        doThrow(new RuntimeException("Test Exception")).when(systemTask).start(any(), any(), any());

        //Execute
        asyncSystemTaskExecutor.execute(systemTask, taskId);

        //Verify
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
    }

    //Add more test cases for edge cases and error scenarios

}