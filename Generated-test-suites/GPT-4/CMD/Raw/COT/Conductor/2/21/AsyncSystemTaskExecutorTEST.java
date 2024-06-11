import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private WorkflowSystemTask wstMock;
    @Mock
    private ExecutionDAOFacade execFacadeMock;
    @Mock
    private QueueDAO queueDAOMock;
    @Mock
    private MetadataDAO metadataDAOMock;
    @Mock
    private ConductorProperties conductorPropsMock;
    @Mock
    private WorkflowExecutor workflowExecutorMock;
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(execFacadeMock, queueDAOMock, 
metadataDAOMock, conductorPropsMock, workflowExecutorMock);
    }

    @Test
    public void executeWithInvalidTaskId() {
        when(execFacadeMock.getTaskModel(anyString())).thenThrow(new RuntimeException("invalid task"));
        asyncSystemTaskExecutor.execute(wstMock, "invalidTaskId");
        verify(queueDAOMock, times(1)).remove(anyString(), anyString());
    }

    @Test
    public void executeWithValidTaskIdAndTerminalTaskStatus() {
        TaskModel task = new TaskModel();
        task.setStatus(TaskModel.Status.COMPLETED);

        when(execFacadeMock.getTaskModel(anyString())).thenReturn(task);
        asyncSystemTaskExecutor.execute(wstMock, "validTaskId");

        verify(queueDAOMock, times(1)).remove(anyString(), anyString());
    }
}

// There are more test case scenarios to be handled in similar manner. The complete listing is very exhaustive.