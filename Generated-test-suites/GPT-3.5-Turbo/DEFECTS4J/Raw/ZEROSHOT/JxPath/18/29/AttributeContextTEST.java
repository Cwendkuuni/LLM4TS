import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {
  
  private AttributeContext attributeContext;
  
  @Before
  public void setUp() {
    EvalContext parentContext = new EvalContext();
    NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
    attributeContext = new AttributeContext(parentContext, nodeTest);
  }
  
  @Test
  public void testGetCurrentNodePointer() {
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testReset() {
    attributeContext.reset();
    assertFalse(attributeContext.isSetStarted());
    assertNull(attributeContext.getIterator());
    assertNull(attributeContext.getCurrentNodePointer());
  }
  
  @Test
  public void testSetPosition() {
    assertFalse(attributeContext.setPosition(1));
    assertFalse(attributeContext.setPosition(2));
  }
  
  @Test
  public void testNextNode() {
    assertFalse(attributeContext.nextNode());
  }
  
  @Test
  public void testNextNodeWithSetStartedTrue() {
    attributeContext.setSetStarted(true);
    attributeContext.setIterator(null);
    assertFalse(attributeContext.nextNode());
  }
  
  @Test
  public void testNextNodeWithIteratorNull() {
    attributeContext.setSetStarted(false);
    attributeContext.setIterator(null);
    assertFalse(attributeContext.nextNode());
  }
  
  @Test
  public void testNextNodeWithIteratorPositionSetFalse() {
    attributeContext.setSetStarted(false);
    NodeIterator iterator = new NodeIterator();
    iterator.setPosition(1);
    attributeContext.setIterator(iterator);
    assertFalse(attributeContext.nextNode());
  }
  
  @Test
  public void testNextNodeWithValidIterator() {
    attributeContext.setSetStarted(false);
    NodeIterator iterator = new NodeIterator();
    iterator.setPosition(1);
    NodePointer currentNodePointer = new NodePointer();
    iterator.setNodePointer(currentNodePointer);
    attributeContext.setIterator(iterator);
    assertTrue(attributeContext.nextNode());
    assertEquals(attributeContext.getCurrentNodePointer(), currentNodePointer);
  }
}