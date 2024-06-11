import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

  @Test
  public void testConstructor() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    assertSame(parentContext, attributeContext.getParentContext());
    assertSame(nodeTest, attributeContext.getNodeTest());
    assertNull(attributeContext.getCurrentNodePointer());
    assertFalse(attributeContext.isSetStarted());
    assertNull(attributeContext.getIterator());
  }

  @Test
  public void testReset() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    attributeContext.setSetStarted(true);
    attributeContext.setIterator(new NodeIterator());
    attributeContext.setCurrentNodePointer(new NodePointer());

    attributeContext.reset();

    assertFalse(attributeContext.isSetStarted());
    assertNull(attributeContext.getIterator());
    assertNull(attributeContext.getCurrentNodePointer());
  }

  @Test
  public void testSetPosition_resetOnPositionLessThanCurrentPosition() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    attributeContext.setCurrentPosition(2);
    attributeContext.setSetStarted(true);
    attributeContext.setIterator(new NodeIterator());
    attributeContext.setPosition(1);

    assertFalse(attributeContext.isSetStarted());
    assertNull(attributeContext.getIterator());
    assertNull(attributeContext.getCurrentNodePointer());
    assertEquals(1, attributeContext.getCurrentPosition());
  }

  @Test
  public void testSetPosition() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    attributeContext.setSetStarted(true);
    attributeContext.setIterator(new NodeIterator());
    attributeContext.setPosition(3);

    assertNull(attributeContext.getCurrentNodePointer());
    assertEquals(3, attributeContext.getCurrentPosition());
  }

  @Test
  public void testNextNode_firstTimeSetStartedFalse_ReturnsFalse() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    attributeContext.setIterator(null);
    attributeContext.setSetStarted(false);

    assertFalse(attributeContext.nextNode());
    assertNull(attributeContext.getCurrentNodePointer());
    assertEquals(0, attributeContext.getCurrentPosition());
  }

  @Test
  public void testNextNode_iteratorNull_ReturnsFalse() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

    attributeContext.setIterator(null);
    attributeContext.setSetStarted(true);

    assertFalse(attributeContext.nextNode());
    assertNull(attributeContext.getCurrentNodePointer());
    assertEquals(0, attributeContext.getCurrentPosition());
  }

  @Test
  public void testNextNode_iteratorDoesNotMove_ReturnsTrue() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    NodeIterator iterator = new NodeIterator();
    iterator.setPosition(1);
    attributeContext.setIterator(iterator);
    attributeContext.setSetStarted(true);

    assertTrue(attributeContext.nextNode());
    assertNotNull(attributeContext.getCurrentNodePointer());
    assertEquals(1, attributeContext.getCurrentPosition());
  }

  @Test
  public void testNextNode_iteratorMoves_ReturnsTrue() {
    NodeTest nodeTest = new NodeNameTest(new QName("test"));
    EvalContext parentContext = new EvalContext(null);
    AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    NodeIterator iterator = new NodeIterator();
    iterator.setPosition(1);
    attributeContext.setIterator(iterator);
    attributeContext.setSetStarted(true);

    iterator.setPosition(2);

    assertTrue(attributeContext.nextNode());
    assertNotNull(attributeContext.getCurrentNodePointer());
    assertEquals(2, attributeContext.getCurrentPosition());
  }
}