import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacadeMock;
    private QueueDAO queueDAOMock;
    private MetadataDAO metadataDAOMock;
    private ConductorProperties conductorPropertiesMock;
    private WorkflowExecutor workflowExecutorMock;

    @Before
    public void setup() {
        executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        queueDAOMock = mock(QueueDAO.class);
        metadataDAOMock = mock(MetadataDAO.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        workflowExecutorMock = mock(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacadeMock,
            queueDAOMock,
            metadataDAOMock,
            conductorPropertiesMock,
            workflowExecutorMock
        );
    }

    @Test
    public void testExecute_TaskDoesNotExist() {
        // Setup
        String taskId = "12345";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

        // Execute
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        // Verify
        verify(queueDAOMock, times(1)).remove(anyString(), eq(taskId));
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        // Setup
        String taskId = "12345";
        WorkflowSystemTask systemTaskMock = mock(WorkflowSystemTask.class);
        TaskModel taskModelMock = mock(TaskModel.class);
        when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);

        // Execute
        asyncSystemTaskExecutor.execute(systemTaskMock, taskId);

        // Verify
        verify(queueDAOMock, times(1)).remove(anyString(), eq(taskId));
    }

    // Add more test cases for other methods

}