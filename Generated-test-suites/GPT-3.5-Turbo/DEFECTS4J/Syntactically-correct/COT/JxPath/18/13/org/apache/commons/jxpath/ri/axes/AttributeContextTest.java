
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

  @Test
  public void testGetCurrentNodePointer() {
    NodeTest nodeTest = new NodeNameTest("testAttribute");
    EvalContext parentContext = new EvalContext();
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    
    assertNull(attributeContext.getCurrentNodePointer());
    
    NodeIterator nodeIterator = new NodeIterator();
    NodePointer nodePointer = new NodePointer(nodeIterator);
    attributeContext.currentNodePointer = nodePointer;
    
    assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    EvalContext parentContext = new EvalContext();
    AttributeContext attributeContext = new AttributeContext(parentContext, null);
    
    attributeContext.setStarted = true;
    attributeContext.iterator = new NodeIterator();
    attributeContext.reset();
    
    assertFalse(attributeContext.setStarted);
    assertNull(attributeContext.iterator);
  }
  
  @Test
  public void testSetPosition() {
    EvalContext parentContext = new EvalContext();
    AttributeContext attributeContext = new AttributeContext(parentContext, null);
    
    attributeContext.setCurrentPosition(0);
    assertFalse(attributeContext.setPosition(-1));
    
    attributeContext.setCurrentPosition(0);
    assertTrue(attributeContext.setPosition(1));
  }
  
  @Test
  public void testNextNode() {
    EvalContext parentContext = new EvalContext();
    AttributeContext attributeContext = new AttributeContext(parentContext, null);
    
    attributeContext.setStarted = false;
    attributeContext.iterator = null;
    parentContext.setCurrentNodePointer(new NodePointer(null));
    assertFalse(attributeContext.nextNode());
    
    attributeContext.setStarted = true;
    attributeContext.iterator = null;
    assertFalse(attributeContext.nextNode());
    
    attributeContext.setStarted = true;
    attributeContext.iterator = new NodeIterator();
    attributeContext.currentNodePointer = new NodePointer(new NodeIterator());
    assertTrue(attributeContext.nextNode());
  }
}
