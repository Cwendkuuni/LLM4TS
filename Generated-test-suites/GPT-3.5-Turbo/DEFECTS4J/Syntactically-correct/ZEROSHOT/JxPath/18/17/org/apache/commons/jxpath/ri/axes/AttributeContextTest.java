import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
  
  @Test
  public void testGetCurrentNodePointer() {
    AttributeContext context = new AttributeContext(null, null);
    assertNull(context.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    AttributeContext context = new AttributeContext(null, null);
    context.reset();
    assertFalse(context.setPosition(1));
  }
  
  @Test
  public void testSetPosition() {
    AttributeContext context = new AttributeContext(null, null);
    assertFalse(context.setPosition(1));
  }
  
  @Test
  public void testNextNode() {
    AttributeContext context = new AttributeContext(null, null);
    assertFalse(context.nextNode());
  }
}