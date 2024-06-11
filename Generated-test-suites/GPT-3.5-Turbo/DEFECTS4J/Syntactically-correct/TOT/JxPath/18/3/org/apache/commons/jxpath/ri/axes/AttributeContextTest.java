import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeContextTest {

  @Test
  public void testGetCurrentNodePointer() {
    // Test empty attribute context
    EvalContext evalContext = new AttributeContext(null, null);
    assertNull(evalContext.getCurrentNodePointer());
    
    // Test non-empty attribute context
    NodePointer nodePointer = new NodePointer();
    evalContext = new AttributeContext(new EvalContext(null), new NodeTest());
    evalContext.setCurrentNodePointer(nodePointer);
    assertEquals(nodePointer, evalContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    // Test resetting attribute context
    EvalContext parentContext = new EvalContext(null);
    NodePointer nodePointer = new NodePointer();
    AttributeContext evalContext = new AttributeContext(parentContext, null);
    evalContext.setCurrentNodePointer(nodePointer);
    evalContext.setPosition(5);
    evalContext.reset();
    assertEquals(0, evalContext.getCurrentPosition());
    assertNull(evalContext.getCurrentNodePointer());
    assertNull(evalContext.iterator);
    assertFalse(evalContext.setStarted);
  }
  
  @Test
  public void testSetPosition() {
    // Test setting position in attribute context
    EvalContext parentContext = new EvalContext(null);
    NodePointer nodePointer = new NodePointer();
    AttributeContext evalContext = new AttributeContext(parentContext, null);
    evalContext.setCurrentNodePointer(nodePointer);
    
    // Test setting position less than current position
    evalContext.setPosition(5);
    assertTrue(evalContext.setPosition(2));
    assertEquals(2, evalContext.getCurrentPosition());
    
    // Test setting position greater than current position
    evalContext.setPosition(5);
    assertTrue(evalContext.setPosition(7));
    assertEquals(7, evalContext.getCurrentPosition());
    
    // Test setting position equal to current position
    evalContext.setPosition(5);
    assertTrue(evalContext.setPosition(5));
    assertEquals(5, evalContext.getCurrentPosition());
  }
  
  @Test
  public void testNextNode() {
    // Test nextNode in attribute context
    EvalContext parentContext = new EvalContext(null);
    NodePointer nodePointer = new NodePointer();
    parentContext.setCurrentNodePointer(nodePointer);
    AttributeContext evalContext = new AttributeContext(parentContext, null);
    
    // Test non-started attribute set
    evalContext.nextNode();
    assertFalse(evalContext.setStarted);
    
    // Test attribute set not instanceof NodeNameTest
    NodeTest nodeTest = new NodeTest();
    evalContext = new AttributeContext(parentContext, nodeTest);
    evalContext.nextNode();
    assertFalse(evalContext.setStarted);
    
    // Test attribute set is instanceof NodeNameTest
    QName name = new QName();
    NodeNameTest nodeNameTest = new NodeNameTest(name);
    evalContext = new AttributeContext(parentContext, nodeNameTest);
    evalContext.nextNode();
    assertTrue(evalContext.setStarted);
    assertEquals(parentContext.getCurrentNodePointer().attributeIterator(name), evalContext.iterator);
    
    // Test iterator is null
    evalContext.iterator = null;
    assertFalse(evalContext.nextNode());
    
    // Test iterator position + 1 is out of range
    evalContext.iterator = new NodeIterator();
    evalContext.iterator.setPosition(1);
    assertFalse(evalContext.nextNode());
    
    // Test nextNode
    evalContext.iterator = new NodeIterator();
    evalContext.iterator.setPosition(1);
    assertTrue(evalContext.nextNode());
    assertNotNull(evalContext.currentNodePointer);
  }
}