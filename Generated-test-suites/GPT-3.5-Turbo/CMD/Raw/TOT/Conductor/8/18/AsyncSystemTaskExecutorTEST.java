import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
  
  private ExecutionDAOFacade executionDAOFacade;
  private QueueDAO queueDAO;
  private MetadataDAO metadataDAO;
  private WorkflowExecutor workflowExecutor;
  
  private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
  
  @Before
  public void setUp() {
    executionDAOFacade = mock(ExecutionDAOFacade.class);
    queueDAO = mock(QueueDAO.class);
    metadataDAO = mock(MetadataDAO.class);
    workflowExecutor = mock(WorkflowExecutor.class);
  
    asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
  }
  
  @Test
  public void testExecute_ValidTask() {
    // Mock TaskModel
    TaskModel task = new TaskModel();
    task.setTaskId("123");
    task.setStatus(TaskModel.Status.IN_PROGRESS);
    
    // Mock WorkflowSystemTask
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    
    // Mock retrieveWorkflowModel
    WorkflowModel workflow = new WorkflowModel();
    workflow.setStatus(WorkflowModel.Status.RUNNING);
    when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
    
    // Mock Task execution
    doNothing().when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    
    asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    
    // Verify task execution
    verify(systemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
  }
  
  @Test
  public void testExecute_TerminalTask() {
    // Mock TaskModel
    TaskModel task = new TaskModel();
    task.setTaskId("123");
    task.setStatus(TaskModel.Status.COMPLETED);
    
    // Mock WorkflowSystemTask
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    
    // Mock retrieveWorkflowModel
    WorkflowModel workflow = new WorkflowModel();
    workflow.setStatus(WorkflowModel.Status.RUNNING);
    when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
    
    asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    
    // Verify task removal from queue
    verify(queueDAO, times(1)).remove(anyString(), anyString());
  }
  
  @Test
  public void testExecute_ExceptionHandling() {
    // Mock TaskModel
    TaskModel task = new TaskModel();
    task.setTaskId("123");
    task.setStatus(TaskModel.Status.IN_PROGRESS);
    
    // Mock WorkflowSystemTask
    WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
    
    // Mock retrieveWorkflowModel
    WorkflowModel workflow = new WorkflowModel();
    workflow.setStatus(WorkflowModel.Status.RUNNING);
    when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
    
    // Mock Task execution with exception
    doThrow(new RuntimeException()).when(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    
    asyncSystemTaskExecutor.execute(systemTask, task.getTaskId());
    
    // Verify error logging
    verify(AsyncSystemTaskExecutor.LOGGER, times(1)).error(anyString(), anyString(), any(Throwable.class));
  }

}