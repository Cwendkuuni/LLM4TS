import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;

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
    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteSuccessfully() {
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        doNothing().when(workflowExecutor).decide(anyString());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        
        verify(queueDAO, times(0)).remove(anyString(), anyString());
        verify(workflowExecutor, times(1)).decide(anyString());
    }

    @Test
    public void testExecuteFailedToFetchTask() {
        String taskId = "testTaskId";
   
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException());
        
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(0)).decide(anyString());
    }

    @Test
    public void testExecuteTaskFailed() {
        String taskId = "testTaskId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setWorkflowInstanceId("workflowInstanceId");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowSystemTask.start(any(), any(), any())).thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verify(workflowExecutor, times(0)).decide(anyString());
    }
}