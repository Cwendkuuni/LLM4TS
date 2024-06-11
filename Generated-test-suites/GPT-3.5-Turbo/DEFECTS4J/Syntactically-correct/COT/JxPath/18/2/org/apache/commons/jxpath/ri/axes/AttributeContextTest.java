import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

  // Method: getCurrentNodePointer()
  @Test
  public void testGetCurrentNodePointer() {
    EvalContext parentContext = new EvalContext();
    NodeTest nodeTest = new NodeTest();
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    NodePointer result = attributeContext.getCurrentNodePointer();
    assertNull(result);
  }

  // Method: reset()
  @Test
  public void testReset() {
    EvalContext parentContext = new EvalContext();
    NodeTest nodeTest = new NodeTest();
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    attributeContext.reset();
    assertFalse(attributeContext.isSetStarted());
    assertNull(attributeContext.getIterator());
    assertEquals(0, attributeContext.getCurrentPosition());
  }

  // Method: setPosition(int)
  @Test
  public void testSetPosition() {
    EvalContext parentContext = new EvalContext();
    NodeTest nodeTest = new NodeTest();
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    attributeContext.setPosition(0);
    assertEquals(0, attributeContext.getCurrentPosition());
    attributeContext.setPosition(1);
    assertEquals(1, attributeContext.getCurrentPosition());
  }

  // Method: nextNode()
  @Test
  public void testNextNode() {
    EvalContext parentContext = new EvalContext();
    NodeTest nodeTest = new NodeTest();
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    attributeContext.setStarted(false);
    attributeContext.setIterator(null);
    attributeContext.setCurrentNodePointer(null);
    assertFalse(attributeContext.nextNode());
  }
}