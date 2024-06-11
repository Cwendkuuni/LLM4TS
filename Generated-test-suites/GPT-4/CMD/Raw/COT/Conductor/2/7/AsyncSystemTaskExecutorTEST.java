
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutor;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteWhenSystemTaskAndTaskIdAreNull() {
        asyncSystemTaskExecutor.execute(null, null);
        verify(queueDAO, times(0)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWhenSystemTaskAndTaskIdAreInvalid() {
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("INVALID"), "INVALID");
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void testExecuteWhenSystemTaskAndTaskIdAreValid() {
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("VALID"), "VALID");
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test(expected = Exception.class)
    public void testExecuteWhenThereIsAnException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("VALID"), "VALID");
    }
}
