
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.netflix.conductor.core.execution.*;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class AsyncSystemTaskExecutorTest {

    ExecutionDAOFacade executionDAOFacade = mock(ExecutionDAOFacade.class);
    QueueDAO queueDAO = mock(QueueDAO.class);
    MetadataDAO metadataDAO = mock(MetadataDAO.class);
    ConductorProperties conductorProperties = mock(ConductorProperties.class);
    WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    
    @Test
    public void testExecute() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(systemTask.getTaskType()).thenReturn("taskType");
        
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,
                                                           queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        verify(queueDAO, times(1)).remove(systemTask.getTaskType(), taskId);

        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).updateTask(task);
    }

    @Test(expected = Exception.class)
    public void testExecuteError() {
        String taskId = "taskId";
        TaskModel task = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());
        
        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,
                                                           queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verifyNoInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietlyError() {
        String taskId = "taskId";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception());

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,
                                                           queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

    @Test
    public void testPostponeQuietlyError() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");
        
        doThrow(new Exception()).when(queueDAO).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(),
                                                         1L);

        AsyncSystemTaskExecutor asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade,
                                                           queueDAO, metadataDAO, conductorProperties, workflowExecutor);
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        
        verify(queueDAO, times(1)).postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 1L);
    }
}
