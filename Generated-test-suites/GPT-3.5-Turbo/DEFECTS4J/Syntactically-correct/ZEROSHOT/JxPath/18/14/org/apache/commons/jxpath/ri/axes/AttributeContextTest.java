import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {
  
  private AttributeContext attributeContext;
  private EvalContext parentContext;
  private NodeTest nodeTest;
  
  @Before
  public void setup() {
    parentContext = new EvalContext();
    nodeTest = new NodeTest();
    attributeContext = new AttributeContext(parentContext, nodeTest);
  }
  
  @Test
  public void testGetCurrentNodePointer() {
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    attributeContext.setPosition(1);
    attributeContext.reset();
    assertEquals(0, attributeContext.getCurrentPosition());
  }
  
  @Test
  public void testSetPosition() {
    boolean result = attributeContext.setPosition(3);
    assertTrue(result);
    assertEquals(3, attributeContext.getCurrentPosition());
  }
  
  @Test
  public void testSetPositionInvalid() {
    boolean result = attributeContext.setPosition(-1);
    assertFalse(result);
    assertEquals(0, attributeContext.getCurrentPosition());
  }
  
  @Test
  public void testNextNode() {
    boolean result = attributeContext.nextNode();
    assertFalse(result);
  }
}