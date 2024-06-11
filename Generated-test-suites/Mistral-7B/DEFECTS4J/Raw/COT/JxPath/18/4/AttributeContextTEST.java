import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.Axis;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private NodeTest nodeTest;

 @Before
 public void setup() {
 parentContext = new EvalContext(null, Axis.SELF);
 nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testConstructor() {
 // Basic constructor test
 assertNotNull(attributeContext);
 assertEquals(parentContext, attributeContext.getParentContext());
 assertNotNull(attributeContext.nodeTest);
 }

 @Test
 public void testGetCurrentNodePointer() {
 // Basic getCurrentNodePointer test
 NodePointer expectedNodePointer = new NodePointer(-1);
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testReset() {
 // Reset test
 attributeContext.reset();
 assertFalse(attributeContext.isStarted());
 assertNull(attributeContext.iterator);
 }

 @Test
 public void testSetPosition() {
 // Set position test
 attributeContext.setPosition(0);
 NodePointer expectedNodePointer = new NodePointer(-1);
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);

 attributeContext.setPosition(1);
 NodePointer nextNodePointer = new NodePointer(1);
 expectedNodePointer = nextNodePointer;
 actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionNegativeValue() {
 attributeContext.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionValueBiggerThanTotalNodes() {
 int totalNodes = 10;
 NodePointer[] nodes = new NodePointer[totalNodes];
 for (int i = 0; i < totalNodes; i++) {
 nodes[i] = new NodePointer(i);
 }
 parentContext.setCurrentNodePointer(nodes[totalNodes - 1]);
 attributeContext.setPosition(totalNodes);
 }

 @Test
 public void testNextNode() {
 // Basic nextNode test
 assertFalse(attributeContext.nextNode());

 NodePointer expectedNodePointer1 = new NodePointer(0);
 NodePointer actualNodePointer1 = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer1, actualNodePointer1);

 NodePointer expectedNodePointer2 = new NodePointer(1);
 NodePointer actualNodePointer2 = attributeContext.getCurrentNodePointer();
 assertTrue(attributeContext.nextNode());
 assertEquals(expectedNodePointer2, actualNodePointer2);
 }

 @Test
 public void testNextNodeNoMoreElements() {
 // Next node test with no more elements
 NodePointer expectedNodePointer = null;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);

 assertFalse(attributeContext.nextNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextNodeBeforeStarted() {
 // Next node test before started
 attributeContext.reset();
 assertTrue(attributeContext.nextNode()); // this call starts the iteration
 assertFalse(attributeContext.nextNode()); // next call without setting position should fail
 }

 @Test
 public void testNextNodeWithNullIterator() {
 // Next node test with null iterator
 attributeContext.iterator = null;
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithEmptyIterator() {
 // Next node test with empty iterator
 NodeIterator emptyIterator = new NodeIterator() {
 @Override public boolean setPosition(int position) {
 return false;
 }

 @Override public NodePointer getNodePointer() {
 return null;
 }

 @Override public boolean hasNext() {
 return false;
 }

 @Override public boolean hasPrevious() {
 return false;
 }

 @Override public int getPosition() {
 return 0;
 }
 };
 attributeContext.iterator = emptyIterator;
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithNullCurrentNodePointer() {
 // Next node test with null current node pointer
 NodePointer expectedNodePointer = null;
 attributeContext.currentNodePointer = null;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);

 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithInvalidCurrentNodePointer() {
 // Next node test with invalid current node pointer
 NodePointer expectedNodePointer = new NodePointer(-1);
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);

 // Set the current node pointer to an invalid node
 attributeContext.currentNodePointer = new NodePointer(Integer.MAX_VALUE);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithNullNodeTest() {
 // Next node test with null node test
 AttributeContext attributeContextWithNullNodeTest = new AttributeContext(parentContext, null);
 assertFalse(attributeContextWithNullNodeTest.nextNode());
 }

}