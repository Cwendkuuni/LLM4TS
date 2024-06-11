
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
    
    @Mock
    private WorkflowSystemTask workflowSystemTaskMock;
    
    @Mock
    private TaskModel taskModelMock;
    
    @Mock
    private WorkflowModel workflowModelMock;
    
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        when(conductorPropertiesMock.getSystemTaskWorkerCallbackDuration()).thenReturn(Duration.ofSeconds(10));
        when(conductorPropertiesMock.getTaskExecutionPostponeDuration()).thenReturn(Duration.ofSeconds(10));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacadeMock, 
                queueDAOMock, 
                metadataDAOMock, 
                conductorPropertiesMock, 
                workflowExecutorMock);
    }
    
    // In this test case, we pretend that the method loadTaskQuietly returns null, so the test verifies that the task is not executed.
    @Test 
    public void testExecute_taskModelNull() {
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(null);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "testTaskId");
        
        verify(executionDAOFacadeMock, never()).updateTask(any());
    }

    // In this test case, we pretend that the method loadTaskQuietly returns a task with terminal status. It tests the early return behavior.
    @Test 
    public void testExecute_taskIsTerminal() {
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "testTaskId");
        
        verify(queueDAOMock).remove(any(), any());
        verify(executionDAOFacadeMock, never()).updateTask(any());
    }

    // This test case tests if the executionDAOFacadeMock.exceedsInProgressLimit returns true (`TaskModel.Status.SCHEDULED && ExecutionLimit` condition checked).
    @Test 
    public void testIfExceedsInProgressLimit_thenExecutionWillBePostponed() {
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
        when(executionDAOFacadeMock.exceedsInProgressLimit(taskModelMock)).thenReturn(true);
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "testTaskId");
        
        verify(queueDAOMock).postpone(any(), any(), any(), any());
    }

    //This test case checks exception handling when an unrecoverable exception is thrown from within the main `try` block in execute method.
    @Test  
    public void testExceptionHandlingExecuteTask() {
        when(executionDAOFacadeMock.getTaskModel(any())).thenReturn(taskModelMock);
        when(taskModelMock.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
        doThrow(new RuntimeException()).when(workflowSystemTaskMock).execute(any(), any(), any());
        
        asyncSystemTaskExecutor.execute(workflowSystemTaskMock, "testTaskId");
        
        verify(executionDAOFacadeMock).updateTask(taskModelMock);
        verify(workflowExecutorMock, never()).decide(any());
    }
}
