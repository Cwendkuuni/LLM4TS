import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;

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
    private WorkflowSystemTask systemTask;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private ConductorProperties conductorProperties;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
    }

    @Test
    public void testExecute() {
        String taskId = "12345";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        when(systemTask.isTaskRetrievalRequired()).thenReturn(true);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflowModel);
        when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(taskModel.getTaskType()).thenReturn("testTaskType");
        when(taskModel.getTaskId()).thenReturn(taskId);
        
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verify(systemTask, times(1)).isTaskRetrievalRequired();
        verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
        verify(executionDAOFacade, times(1)).getWorkflowModel(anyString(), anyBoolean());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        String taskId = "12345";
        
        when(taskModel.getTaskId()).thenReturn(taskId);
        
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
        
        verify(queueDAO, times(1)).postpone(eq(queueName), eq(taskId), anyLong(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "12345";
        
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
        
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        
        assertEquals(taskModel, result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    }
}