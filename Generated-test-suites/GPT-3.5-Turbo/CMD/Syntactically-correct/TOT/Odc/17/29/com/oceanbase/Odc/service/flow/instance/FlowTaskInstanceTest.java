import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

  private FlowTaskInstance flowTaskInstance;

  @Before
  public void setUp() {
    // Initialize FlowTaskInstance for testing
    flowTaskInstance = new FlowTaskInstance();
  }

  @Test
  public void testCreate() {
    // Typical use case: Create a new FlowTaskInstance
    flowTaskInstance.create();
    assertNotNull(flowTaskInstance.getId());
  }

  @Test
  public void testUpdate() {
    // Typical use case: Update an existing FlowTaskInstance
    flowTaskInstance.update();
    assertNotNull(flowTaskInstance.getUpdateTime());
  }

  @Test
  public void testComplete() {
    // Edge case: Complete a FlowTaskInstance with aborted flag
    flowTaskInstance.abort();
    assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
  }

  @Test
  public void testCancel() {
    // Edge case: Cancel a running FlowTaskInstance
    try {
      flowTaskInstance.cancel(true);
      assertFalse(flowTaskInstance.isDone());
    } catch (Exception e) {
      fail("Exception thrown while canceling task");
    }
  }

  @Test
  public void testGetResult() {
    // Error scenario: Get result for a TaskInstance that is not present on this machine
    try {
      flowTaskInstance.getResult();
      fail("UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      // Exception is expected
    } catch (Exception e) {
      fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testResourceType() {
    // Typical use case: Get the resource type of a FlowTaskInstance
    assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
  }

}