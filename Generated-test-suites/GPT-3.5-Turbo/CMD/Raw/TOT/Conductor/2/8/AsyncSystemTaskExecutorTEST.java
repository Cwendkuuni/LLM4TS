import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;

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
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }
    
    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "123";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);
        
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }
    
    @Test
    public void testExecute_TaskCompleted() {
        String taskId = "123";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);
        
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
    }
    
    @Test
    public void testExecute_TaskScheduled() {
        String taskId = "123";
        WorkflowSystemTask mockSystemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        
        asyncSystemTaskExecutor.execute(mockSystemTask, taskId);
        
        verify(queueDAO, times(1)).postpone(anyString(), eq(taskId), anyInt(), anyLong());
    }
}