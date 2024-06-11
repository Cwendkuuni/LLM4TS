package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class OnlineSchemaChangeFlowableTaskTest {

  // Representing TaskService as a Mock Object
  private TaskService taskService = mock(TaskService.class);

  @Before
  public void setup() {
    // ...
  }

  @After
  public void tearDown() {
    // ...
  }

  @Test
  public void testStart() {
    OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
    task.start(1L, taskService, null);

    assertEquals(TaskStatus.RUNNING, task.status);
  }

  @Test
  public void testIsSuccessful() {
	  OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
	  task.status = TaskStatus.DONE;
	  
	  assertTrue(task.isSuccessful());
  }
  
  // Add additional test cases
}