
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacadeMock;
    
    @Mock
    private QueueDAO queueDAOMock;

    @Mock
    private MetadataDAO metadataDAOMock;

    @Mock
    private ConductorProperties conductorPropertiesMock;

    @Mock
    private WorkflowExecutor workflowExecutorMock;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutorUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // Arrange
        WorkflowSystemTask systemTask = new StubWorkflowSystemTask();
        String taskId = "sampleTaskId";
        
        // Mocking behaviours
        when(executionDAOFacadeMock.getTaskModel(anyString()))
                .thenReturn(new TaskModel());
        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration())
                .thenReturn(Duration.ofSeconds(1));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration())
                .thenReturn(Duration.ofSeconds(1));
        
        // Act
        asyncSystemTaskExecutorUnderTest.execute(systemTask, taskId);
        
        // Assert
        verify(executionDAOFacadeMock).getTaskModel(anyString());
        verify(conductorPropertiesMock).getSystemTaskWorkerCallbackDuration();
        verify(conductorPropertiesMock).getTaskExecutionPostponeDuration();
    }
}

// For testing purpose, overriding the abstract class functionalities
class StubWorkflowSystemTask extends WorkflowSystemTask {

    public StubWorkflowSystemTask() {
        super("StubWorkflow");
    }

    @Override
    public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor executor) {
    }
    
    @Override
    public void execute(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor executor) {
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
