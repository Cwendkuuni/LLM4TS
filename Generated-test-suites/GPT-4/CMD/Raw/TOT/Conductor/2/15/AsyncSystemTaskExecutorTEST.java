import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

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
    private WorkflowSystemTask systemTask;

    private AsyncSystemTaskExecutor executor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "1";
        TaskModel task = new TaskModel();
        task.setTaskId(1L);
        Mockito.doReturn(task).when(executionDAOFacade).getTaskModel(taskId);

        executor.execute(systemTask, taskId);

        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);

    }
}