
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    AsyncSystemTaskExecutor asyncSystemTaskExecutorTest;

    @Mock
    ExecutionDAOFacade executionDAOFacade;

    @Mock
    QueueDAO queueDAO;

    @Mock
    MetadataDAO metadataDAO;

    @Mock
    WorkflowExecutor workflowExecutor;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    WorkflowSystemTask workflowSystemTask;

    @Mock
    TaskModel taskModel;

    @Mock
    WorkflowModel workflowModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutorTest.execute(workflowSystemTask, "taskId");

        verify(executionDAOFacade, times(1)).getTaskModel("taskId");
        verify(queueDAO, times(1)).remove(anyString(), eq("taskId"));
    }

    @Test
    public void testExecuteWithException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenAnswer((Answer<TaskModel>) invocation -> {
            throw new RuntimeException("Failed in getTask method");
        });

        asyncSystemTaskExecutorTest.execute(workflowSystemTask, "errorTaskId");

        verify(executionDAOFacade, times(1)).getTaskModel("errorTaskId");
        verify(queueDAO, times(1)).remove(anyString(), eq("errorTaskId"));
    }
  
    @Test
    public void testPostponeQuietly() {
        doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());

        asyncSystemTaskExecutorTest.postponeQuietly(anyString(), taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testLoadTaskQuietly() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        assertEquals(taskModel, asyncSystemTaskExecutorTest.loadTaskQuietly(anyString()));
        
        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }
  
    @Test
    public void testLoadTaskQuietlyWithException() {
        when(executionDAOFacade.getTaskModel(anyString())).thenAnswer((Answer<TaskModel>) invocation -> {
            throw new RuntimeException("Failed in getTask method");
        });

        assertNull(asyncSystemTaskExecutorTest.loadTaskQuietly(anyString()));

        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
    }
}
