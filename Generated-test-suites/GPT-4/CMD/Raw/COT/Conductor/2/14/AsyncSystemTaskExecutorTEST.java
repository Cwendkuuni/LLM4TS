import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.Execution.ConductorProperties;
import com.netflix.conductor.core.Execution.WorkflowExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;

@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private WorkflowSystemTask systemTask;
    
    @Mock
    private TaskModel task;
    
    @Mock
    private WorkflowModel workflow;
    
    private final String taskId = "taskIdExample";
    
    @Before
    public void setUp() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
    }
    
    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(task, times(1)).incrementPollCount();
    }
    
    @Test
    public void testExecuteTaskNotFound() {
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(anyString(), anyString());
    }
    
    @Test
    public void testExecutePostponeQuietly() {
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
    
    @Test
    public void testExecutePostponeQuietlyException() {
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        doThrow(new RuntimeException()).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }
}