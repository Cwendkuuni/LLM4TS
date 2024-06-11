

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;


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

    private String mockTaskId = "mockTaskId";

    @Before
    public void setUp() {
        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds()).thenReturn(30L);
        when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds()).thenReturn(60L);
    }

    @Test
    public void execute_whenTaskNotFound_thenDoNotProceed() {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, mockTaskId);
        verify(systemTask, times(0)).isTaskRetrievalRequired();
    }

    @Test
    public void execute_ifTaskIsTerminal_thenRemoveFromQueue() {
        TaskModel task = new TaskModel();
        task.setTaskId(mockTaskId);
        task.setStatus(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, mockTaskId);
        verify(queueDAO).remove(any(), eq(mockTaskId));
    }

    // Add more tests here..

}

