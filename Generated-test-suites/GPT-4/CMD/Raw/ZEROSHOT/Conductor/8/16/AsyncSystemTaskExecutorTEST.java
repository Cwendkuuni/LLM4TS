
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import org.mockito.InjectMocks;

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
    WorkflowSystemTask systemTask;
    
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);     
    }
    
    @Test
    public void testExecute() {
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO, times(1)).remove(anyString(),eq(taskId));
        verify(queueDAO, times(1)).postpone(anyString(),eq(taskId),anyInt(),anyLong());
        verify(metadataDAO, atMost(1)).getTaskDef(anyString());
        verify(executionDAOFacade, atMost(1)).getWorkflowModel(anyString(),anyBoolean());
    }
    
    @Test
    public void testLoadTaskQuietly() {
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        TaskModel fetchedTaskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        verify(executionDAOFacade).getTaskModel(taskId);
        Assert.assertEquals(taskModel, fetchedTaskModel);
    }
}
