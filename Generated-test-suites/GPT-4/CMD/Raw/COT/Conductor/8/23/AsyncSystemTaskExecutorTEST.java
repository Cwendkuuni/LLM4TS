
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    @Mock
    ExecutionDAOFacade executionDAOFacade;
    
    @Mock
    WorkflowSystemTask task;

    @Mock
    QueueDAO queueDAO;

    @Mock
    MetadataDAO metadataDAO;
    
    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    TaskModel taskModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // Mock a `WorkflowSystemTask` instance and its related methods
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // Mock a `ConductorProperties` instance and its related methods
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10L));

        // Initialize the AsyncSystemTaskExecutor instance
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        // Execute the `execute` method
        executor.execute(systemTask, "testTaskId");

        // Verify interactions
        verify(executionDAOFacade, times(1)).getTaskModel("testTaskId");
    }

    @Test(expected = NotFoundException.class)
    public void testExecuteWithTaskNotFound() {
        // Mock a `WorkflowSystemTask` instance and its related methods
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        // Mock a `ConductorProperties` instance and its related methods
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10L));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10L));

        // Define a custom exception to be thrown
        NotFoundException exception = new NotFoundException("Task not found");

        // When `executionDAOFacade.getTaskModel(anyString())` is called, throw the exception
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(exception);

        // Initialize the AsyncSystemTaskExecutor instance
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        // Execute the `execute` method
        executor.execute(systemTask, "testTaskId");
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        executor.execute(task, taskId);
        Mockito.verify(executionDAOFacade).getTaskModel(taskId);
    }

    @Test(expected = Exception.class)
    public void testLoadTaskQuietlyException() {
        String taskId = "testTaskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception("Database Error"));

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.execute(task, taskId);
    }

    @Test
    public void testPostponeQuietly(){
        String queueName = "TestQueue";
        TaskModel taskModel = mock(TaskModel.class);

        when(taskModel.getTaskId()).thenReturn("testTaskId");
        when(taskModel.getWorkflowPriority()).thenReturn(1);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.postponeQuietly(queueName, taskModel);
        verify(queueDAO, times(1)).postpone(queueName, "testTaskId", 1, systemTaskCallbackTime);
    }

    @Test(expected = Exception.class)
    public void testPostponeQuietlyException() {
        String queueName = "TestQueue";
        TaskModel taskModel = mock(TaskModel.class);

        when(taskModel.getTaskId()).thenReturn("testTaskId");
        when(taskModel.getWorkflowPriority()).thenReturn(1);
        doThrow(new RuntimeException()).when(queueDAO).postpone(queueName, "testTaskId", 1, systemTaskCallbackTime);

        AsyncSystemTaskExecutor executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        executor.postponeQuietly(queueName, taskModel);
    }
}

