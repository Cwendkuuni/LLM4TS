import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute_whenTaskIsNull_thenLogError() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(systemTask.getTaskType(), taskId);
    }

    @Test
    public void testExecute_whenTaskIsCompleted_thenRemoveFromQueue() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "123";
        // Implement the necessary setup to mock a completed task

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(queueDAO).remove(QueueUtils.getQueueName(task), taskId);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}