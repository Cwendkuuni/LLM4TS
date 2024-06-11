import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AsyncSystemTaskExecutorTest {

    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private ConductorProperties conductorProperties;
    private WorkflowExecutor workflowExecutor;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                workflowExecutor);
    }

    @Test
    public void testExecute_whenTaskIsNull() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(LOGGER, times(1)).error(anyString(), eq(taskId), eq(systemTask));
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_whenTaskStatusIsTerminal() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";

        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(LOGGER, times(1)).info(anyString(), eq(task.getTaskType()), eq(task.getTaskId()));
        verify(queueDAO, times(1)).remove(anyString(), eq(task.getTaskId()));
    }

    // Add more test cases for other methods in the class

}