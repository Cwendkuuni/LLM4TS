import org.junit.*;
import static org.junit.Assert.*;

public class AttributeContextTest {

  @Test
  public void testGetCurrentNodePointer() {
    AttributeContext context = new AttributeContext(parentContext, nodeTest);
    assertNotNull(context.getCurrentNodePointer());
  }

  @Test
  public void testReset() {
    AttributeContext context = new AttributeContext(parentContext, nodeTest);
    context.reset();
    assertFalse(context.setStarted);
    assertNull(context.iterator);
    super.reset();
  }

  @Test
  public void testSetPosition() {
    AttributeContext context = new AttributeContext(parentContext, nodeTest);
    int position = 1;
    boolean result = context.setPosition(position);
    assertTrue(result);
  }

  @Test
  public void testNextNode() {
    AttributeContext context = new AttributeContext(parentContext, nodeTest);
    boolean result = context.nextNode();
    assertTrue(result);
  }
  
}