package com.oceanbase.odc.service.flow.instance;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlowTaskInstanceTest {
  private FlowTaskInstance instance;
  // Other necessary objects required to instantiate FlowTaskInstance like RuntimeDelegateMapper, FlowableAdaptor, etc.

  @Before
  public void setup() {
    // Initialize objects required by FlowTaskInstance
    // instance = new FlowTaskInstance();
  }

  @Test 
  public void testBindServiceTask() {
    // prepare test data
    // execute method
    // instance.bindServiceTask(target);
    // verify the result
    // assertTrue(instance.isPresentOnThisMachine());
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testCancelTaskNotOnThisMachine() {
    instance.cancel(true);
  }

  // Continue to add other test methods..
}