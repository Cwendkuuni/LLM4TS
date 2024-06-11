package com.netflix.conductor.core.execution;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {
  @InjectMocks
  private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  @Mock
  private ExecutionDAOFacade executionDAOFacade;

  @Mock
  private WorkflowExecutor workflowExecutor;

  @Mock
  private QueueDAO queueDAO;

  @Before
  public void setup(){
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testExecute(){
    // write your test here by making use of the mocks
  }
  
  @Test
  public void testPostponeQuietly(){
    // write your test here by making use of the mocks
  }

  @Test
  public void testLoadTaskQuietly(){
    // write your test here by making use of the mocks
  }

  // Create more test methods as need

}