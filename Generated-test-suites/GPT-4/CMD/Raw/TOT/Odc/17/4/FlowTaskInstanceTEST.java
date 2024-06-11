import org.junit.Test;
import static org.junit.Assert.*;

public class FlowTaskInstanceTest {
  
  @Test
  public void testIsPresentOnThisMachine() {
      FlowTaskInstance instance = new FlowTaskInstance();
      // initiate class with certain parameters in order to test system behavior
      assertFalse(instance.isPresentOnThisMachine());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCancelWithNonexistingMachine() {
      FlowTaskInstance instance = new FlowTaskInstance();
      instance.cancel(true);
  }

  @Test
  public void testResourceType() {
      FlowTaskInstance instance = new FlowTaskInstance();
      assertEquals(instance.resourceType(), "ODC_FLOW_TASK_INSTANCE");
  }
  
  // Other test methods...
}