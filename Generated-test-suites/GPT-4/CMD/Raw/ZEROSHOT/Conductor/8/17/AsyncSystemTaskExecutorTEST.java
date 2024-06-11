
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Duration;

import com.netflix.conductor.core.execution.*;
import com.netflix.conductor.core.config.ConductorProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void executeTaskTest() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(5));
        when(conductorProperties.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(5));
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId1");
    }

    @Test
    public void testPostponeQuietly() {
        try {
            asyncSystemTaskExecutor.getClass().getDeclaredMethod("postponeQuietly", String.class, TaskModel.class).invoke(asyncSystemTaskExecutor, "queueName", new TaskModel());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLoadTaskQuietly() {
        try {
            TaskModel taskModel = (TaskModel) asyncSystemTaskExecutor.getClass().getDeclaredMethod("loadTaskQuietly", String.class).invoke(asyncSystemTaskExecutor, "taskId");
            assertNull(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
