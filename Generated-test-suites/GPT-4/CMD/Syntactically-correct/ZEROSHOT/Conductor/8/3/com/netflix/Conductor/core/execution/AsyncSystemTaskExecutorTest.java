package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

  @InjectMocks
  private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
  
  @Mock
  private ExecutionDAOFacade executionDAOFacade;
  
  @Mock
  private QueueDAO queueDAO;
  
  @Mock
  private MetadataDAO metadataDAO;
  
  @Mock
  private ConductorProperties conductorProperties;
  
  @Mock
  private WorkflowSystemTask systemTask;
  
  @Mock
  private WorkflowExecutor workflowExecutor;

  @Before
  public void setUp() {
      MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testExecute_noTaskFound() {
      when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
      asyncSystemTaskExecutor.execute(systemTask, "123");
      verify(executionDAOFacade, times(1)).getTaskModel(anyString());
  }

  @Test
  public void testExecute_taskTerminal() {
      TaskModel taskModel = mock(TaskModel.class);
      when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
      when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
      asyncSystemTaskExecutor.execute(systemTask, "123");
      verify(queueDAO, times(1)).remove(anyString(), anyString());
  }

  // Add more tests to cover more scenarios in execute method. 

  @Test
  public void testPostponeQuietly() {
      TaskModel taskModel = mock(TaskModel.class);
      asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);
      verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
  }

  @Test
  public void testPostponeQuietly_exception() {
      TaskModel taskModel = mock(TaskModel.class);
      doThrow(Exception.class).when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
      asyncSystemTaskExecutor.postponeQuietly("testQueue", taskModel);
      verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyLong());
  }

  @Test
  public void testLoadTaskQuietly() {
      when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);
      asyncSystemTaskExecutor.loadTaskQuietly("123");
      verify(executionDAOFacade, times(1)).getTaskModel(anyString());
  }

  @Test
  public void testLoadTaskQuietly_exception() {
      doThrow(Exception.class).when(executionDAOFacade).getTaskModel(anyString());
      asyncSystemTaskExecutor.loadTaskQuietly("123");
      verify(executionDAOFacade, times(1)).getTaskModel(anyString());
  }
}
