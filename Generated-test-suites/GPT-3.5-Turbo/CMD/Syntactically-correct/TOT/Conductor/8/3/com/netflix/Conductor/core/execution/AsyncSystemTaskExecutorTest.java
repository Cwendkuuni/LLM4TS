import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private WorkflowSystemTask workflowSystemTaskMock;
    private String taskId = "1234";

    @Before
    public void setUp() {
        ExecutionDAOFacade executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
        QueueDAO queueDAOMock = mock(QueueDAO.class);
        MetadataDAO metadataDAOMock = mock(MetadataDAO.class);
        ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
        WorkflowExecutor workflowExecutorMock = mock(WorkflowExecutor.class);
        
        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
            executionDAOFacadeMock,
            queueDAOMock,
            metadataDAOMock,
            conductorPropertiesMock,
            workflowExecutorMock
        );
        
        workflowSystemTaskMock = mock(WorkflowSystemTask.class);
    }

    @Test
    public void testExecute() {
        // Typical use case
        TaskModel taskModelMock = mock(TaskModel.class);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModelMock);
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        
        // Edge case
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        verify(queueDAOMock, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
        
        // Error scenario
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException("Error"));
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, taskId);
        verify(executionDAOFacade, times(2)).getTaskModel(taskId);
    }
}