import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void testAsyncSystemTaskExecutorWithNullArguments() {
        new AsyncSystemTaskExecutor(null, null, null, null, null);
    }

    @Test
    public void testExecuteMethodWithValidArguments() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade).getTaskModel("taskId");
    }

    @Test
    public void testExecuteMethodWithTaskInTerminalState() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskInTerminalStateId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new IllegalStateException("Task is in terminal state."));
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecuteMethodExceptionHandling() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskThatThrowsExceptionId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        asyncSystemTaskExecutor.execute(systemTask, taskId);
    }
}