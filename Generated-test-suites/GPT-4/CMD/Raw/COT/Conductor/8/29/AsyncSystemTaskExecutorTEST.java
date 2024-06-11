
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {
    private ExecutionDAOFacade executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
    private QueueDAO queueDAO = Mockito.mock(QueueDAO.class);
    private MetadataDAO metadataDAO = Mockito.mock(MetadataDAO.class);
    private ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
    private WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        TaskModel taskModel = Mockito.mock(TaskModel.class);
        String taskId = "task1";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        doNothing().when(queueDAO).remove(anyString(), anyString());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }

    @Test(expected = RuntimeException.class)
    public void testLoadTaskQuietlyWhenExceptionOccur() {
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException("Task not found"));
        
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly("task1"));
    }

    @Test
    public void testPostponeQuietlyWhenExceptionOccur() {
        doThrow(new RuntimeException("Exception while postponing")).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        
        asyncSystemTaskExecutor.postponeQuietly("queue1", Mockito.mock(TaskModel.class));
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}
