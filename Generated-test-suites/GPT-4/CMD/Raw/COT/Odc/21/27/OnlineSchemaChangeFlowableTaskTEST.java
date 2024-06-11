
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;

public class OnlineSchemaChangeFlowableTaskTest {
  
  private OnlineSchemaChangeFlowableTask task;
  private Long taskId;
  private TaskService taskService;
  private DelegateExecution execution;
 
  @Before
  public void setUp(){
    task = new OnlineSchemaChangeFlowableTask();
    taskId = 1L;
    taskService = Mockito.mock(TaskService.class);
    execution = Mockito.mock(DelegateExecution.class);
  }

  @Test
  public void testStart() {
    assertNull(task.start(taskId, taskService, execution));
  }

  @Test
  public void isSuccessFul() {
    assertFalse(task.isSuccessful());
  }
 
  @Test
  public void isFailure() {
    assertFalse(task.isFailure());
  }
    
  @Test
  public void onFailure() {
    task.onFailure(taskId, taskService);
  }

  @Test
  public void onSuccessful() {
    task.onSuccessful(taskId, taskService);
  }  
  
  @Test
  public void onTimeout() {
    task.onTimeout(taskId, taskService);
  }
  
  @Test
  public void onProgressUpdate() {
    task.onProgressUpdate(taskId, taskService);
  }  

  @Test
  public void cancel() {
    assertTrue(task.cancel(false, taskId, taskService));
    assertFalse(task.cancel(true, taskId, taskService));
  }

  @Test
  public void isCancelled() {
    assertFalse(task.isCancelled());
  }
}

