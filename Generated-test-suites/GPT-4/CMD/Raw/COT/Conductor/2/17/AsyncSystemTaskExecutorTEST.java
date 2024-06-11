import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class AsyncSystemTaskExecutorTest {
    @Mock ExecutionDAOFacade executionDAOFacade;
    @Mock QueueDAO queueDAO;
    @Mock MetadataDAO metadataDAO;
    @Mock ConductorProperties conductorProperties;
    @Mock WorkflowExecutor workflowExecutor;
    @InjectMocks AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    @Mock WorkflowSystemTask systemTask;
    @Mock TaskModel task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstruct() {
        new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties,workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "UniqueId";
        when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(systemTask, times(1)).start(any(), any(), any());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecuteWhenTaskNotFound() {
        String taskId = "UniqueId";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecuteWhenTaskIsTerminal() {
        String taskId = "UniqueId";
        when(task.getStatus()).thenReturn(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
}