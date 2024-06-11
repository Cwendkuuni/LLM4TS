
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    @Mock
    private TaskModel taskModel;

    private AsyncSystemTaskExecutor executor;

    @Before
    public void setUp() {
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        executor.execute(workflowSystemTask, "1");

        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(metadataDAO, times(1)).getTaskDef(anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        executor.execute(workflowSystemTask, "1");
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        executor.execute(workflowSystemTask, "1");

        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(metadataDAO, times(0)).getTaskDef(anyString());
    }
}
