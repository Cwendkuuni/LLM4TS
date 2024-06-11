package com.oceanbase.odc.service.flow.instance;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.core.shared.TaskType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
  
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest{

  @Test
  public void testGetCoreFlowableElementType() {
    // Given
    // Create a test FlowTaskInstance object. Here you might need to pass mock objects.
    FlowTaskInstance instanceUnderTest = new FlowTaskInstance();

    // when
    FlowableElementType result = instanceUnderTest.getCoreFlowableElementType();
  
    // then
    assertEquals(FlowableElementType.SERVICE_TASK, result);
  }

  @Test(expected = NullPointerException.class)
  public void testBindServiceTaskNull() {
    // give a null task
    BaseRuntimeFlowableDelegate<?> rs = null;
  
    // when
    FlowTaskInstance instanceUnderTest = new FlowTaskInstance();
    instanceUnderTest.bindServiceTask(rs);
  }

  @Test
  public void testBindServiceTask() {
    // Given an instance and a target task not related to this instance
    FlowTaskInstance instanceUnderTest = new FlowTaskInstance();
    BaseRuntimeFlowableDelegate<?> task = Mockito.mock(BaseRuntimeFlowableDelegate.class);
    when(task.getTargetTaskInstanceId()).thenReturn(123L);  // A random ID

    // When
    instanceUnderTest.bindServiceTask(task);

    // Then
    // We're just testing that no exception is thrown when we bind a task unrelated to this instance
  }

  @Test(expected = InterruptedException.class)
  public void testGetResultException() throws InterruptedException, ExecutionException {
    // Given an instance present on another machine
    FlowTaskInstance instanceUnderTest = new FlowTaskInstance();
  
    // When
    instanceUnderTest.getResult();

    // Then 
    // An exception should be thrown
  }
}