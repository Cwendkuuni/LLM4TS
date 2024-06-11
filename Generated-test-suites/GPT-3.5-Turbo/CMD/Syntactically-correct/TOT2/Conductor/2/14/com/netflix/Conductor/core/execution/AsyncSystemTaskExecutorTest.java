import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private TaskModel task;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private WorkflowSystemTask systemTask;

    @Test
    public void testExecute_foundTask() {
        String taskId = "1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(executionDAOFacade).getWorkflowModel(anyString(), anyBoolean);
        verify(executionDAOFacade).updateTask(task);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);

        assertFalse(task.getStatus().equals(TaskModel.Status.SCHEDULED));
    }

    @Test
    public void testExecute_notFoundTask() {
        String taskId = "2";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(anyString(), anyString());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

}