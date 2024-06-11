package com.netflix.conductor.core.execution;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
  @Mock
  private WorkflowSystemTask systemTask;
  @InjectMocks
  private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testTaskNotFound() {
    //Arrange
    String taskId = "id";
    when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

    //Act
    asyncSystemTaskExecutor.execute(systemTask, taskId);

    //Asset
    verify(executionDAOFacade, times(1)).getTaskModel(taskId);
    verify(queueDAO, never()).remove(anyString(), anyString());
  }

  // Other test cases omitted for brevity...

  @Test
  public void testExecutionException() {
    //Arrange
    TaskModel task = mock(TaskModel.class);
    when(task.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
    when(systemTask.isAsyncComplete(task)).thenReturn(false);
    when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
    doThrow(new RuntimeException()).when(systemTask).execute(any(), any(), any());

    //Act
    asyncSystemTaskExecutor.execute(systemTask, "id");

    //Assert
    verify(executionDAOFacade, times(1)).updateTask(task);
    verify(workflowExecutor, never()).decide(anyString());
  }
}