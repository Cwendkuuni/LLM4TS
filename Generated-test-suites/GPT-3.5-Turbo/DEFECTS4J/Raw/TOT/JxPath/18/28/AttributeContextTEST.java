import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

  private AttributeContext attributeContext;
  private EvalContext parentContext;
  private NodeTest nodeTest;

  @Before
  public void setUp() {
    parentContext = new EvalContext();
    nodeTest = new NodeNameTest(new QName("attribute"));
    attributeContext = new AttributeContext(parentContext, nodeTest);
  }

  @Test
  public void testGetCurrentNodePointer() {
    assertNull(attributeContext.getCurrentNodePointer());
    NodePointer nodePointer = new NodePointer();
    attributeContext.currentNodePointer = nodePointer;
    assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
  }

  @Test
  public void testReset() {
    attributeContext.setStarted = true;
    attributeContext.iterator = new NodeIterator();
    attributeContext.reset();
    assertFalse(attributeContext.setStarted);
    assertNull(attributeContext.iterator);
  }

  @Test
  public void testSetPosition_lessThanCurrentPosition() {
    attributeContext.setPosition(3);
    attributeContext.setPosition(2);
    assertFalse(attributeContext.setStarted);
    assertNull(attributeContext.iterator);
  }

  @Test
  public void testSetPosition_greaterThanCurrentPosition() {
    attributeContext.setPosition(2);
    assertTrue(attributeContext.setStarted);
  }

  @Test
  public void testSetPosition_nextNodeFalse() {
    attributeContext.parentContext.setCurrentNodePointer(null);
    assertFalse(attributeContext.setPosition(1));
  }

  @Test
  public void testSetPosition_nextNodeTrue() {
    attributeContext.parentContext.setCurrentNodePointer(new NodePointer());
    assertTrue(attributeContext.setPosition(1));
  }

  @Test
  public void testNextNode_setStartedTrue() {
    attributeContext.setStarted = true;
    attributeContext.iterator = null;
    assertFalse(attributeContext.nextNode());
  }

  @Test
  public void testNextNode_iteratorNull() {
    attributeContext.nextNode();
    assertNull(attributeContext.currentNodePointer);
  }

  @Test
  public void testNextNode_iteratorPositionIncrementFalse() {
    attributeContext.setStarted = true;
    NodeIterator iterator = new NodeIterator();
    attributeContext.iterator = iterator;
    iterator.setPosition(1);
    assertFalse(attributeContext.nextNode());
  }

  @Test
  public void testNextNode_iteratorPositionIncrementTrue() {
    attributeContext.setStarted = true;
    NodeIterator iterator = new NodeIterator();
    attributeContext.iterator = iterator;
    assertTrue(attributeContext.nextNode());
  }
}