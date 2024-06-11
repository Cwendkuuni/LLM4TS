
import com.netflix.conductor.core.execution.*;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    ExecutionDAOFacade mockExecutionDAOFacade;

    @Mock
    QueueDAO mockQueueDAO;

    @Mock
    WorkflowExecutor mockWorkflowExecutor;

    @Mock
    WorkflowSystemTask mockWorkflowSystemTask;

    @Mock
    TaskModel mockTaskModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(mockExecutionDAOFacade, mockQueueDAO, null, null, mockWorkflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "1";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTaskModel);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);
        verify(mockExecutionDAOFacade, times(1)).getTaskModel(taskId);
        verify(mockWorkflowSystemTask, times(1)).isAsyncComplete(mockTaskModel);
    }

    @Test
    public void testExecute_NullTask() {
        String taskId = "1";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(mockWorkflowSystemTask, taskId);
        verify(mockExecutionDAOFacade, times(1)).getTaskModel(taskId);
        verifyZeroInteractions(mockWorkflowSystemTask);
    }

    @Test
    public void testLoadTaskQuietly_WithException() {
        String taskId = "1";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNull(result);
        verify(mockExecutionDAOFacade, times(1)).getTaskModel(taskId);
    }

    @Test
    public void testLoadTaskQuietly_WithoutException() {
        String taskId = "2";
        when(mockExecutionDAOFacade.getTaskModel(taskId)).thenReturn(mockTaskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertNotNull(result);
        verify(mockExecutionDAOFacade, times(1)).getTaskModel(taskId);
    }
}
