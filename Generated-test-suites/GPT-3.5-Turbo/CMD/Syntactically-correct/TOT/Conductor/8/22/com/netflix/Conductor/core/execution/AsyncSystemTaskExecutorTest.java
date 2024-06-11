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
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_WhenTaskNotPresent() {
        // Given
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        // When
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "12345");

        // Then
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_WhenTaskAlreadyCompleted() {
        // Given
        TaskModel task = new TaskModel();
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(task.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        // When
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "12345");

        // Then
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_WhenTaskScheduled() {
        // Given
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        // When
        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), "12345");

        // Then
        verify(task, times(1)).incrementPollCount();
    }

    // Add more test cases for other scenarios

}