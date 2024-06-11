
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private WorkflowSystemTask workflowSystemTask;
    @Mock
    private WorkflowExecutor workflowExecutor;
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds()).thenReturn(0L);        

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, conductorProperties, workflowExecutor);
    }
    
    @Test
    public void shouldExecuteTask() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(workflowSystemTask.isTaskRetrievalRequired()).thenReturn(false);
        asyncSystemTaskExecutor.execute(workflowSystemTask, "1");
        verify(queueDAO, times(1)).remove(Matchers.anyString(), anyString());
    }

    //Continue with other test methods...

}
