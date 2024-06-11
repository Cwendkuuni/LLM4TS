import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for AttributeContext class.
 */
public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;

 @Before
 public void setup() {
 // Initialize parentContext for AttributeContext
 QName name = new QName("test", "testAttribute");
 NodeTest nodeTest = new NodeNameTest(name);
 parentContext = new EvalContext(null, nodeTest);
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 // Test getCurrentNodePointer() method
 @Test
 public void testGetCurrentNodePointer() {
 NodePointer expectedNodePointer = new NodePointer(new Object());
 attributeContext.currentNodePointer = expectedNodePointer;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertEquals("Expected and actual NodePointer should be equal",
 expectedNodePointer, actualNodePointer);
 }

 // Test reset() method
 @Test
 public void testReset() {
 attributeContext.reset();
 Assert.assertFalse("setStarted should be false after reset",
 attributeContext.isSetStarted());
 Assert.assertNull("iterator should be null after reset",
 attributeContext.iterator);
 }

 // Test setPosition(int position) method
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionNegativeValue() {
 attributeContext.setPosition(-1);
 }

 @Test
 public void testSetPositionValidValue() {
 attributeContext.setPosition(1);
 NodePointer expectedNodePointer = new NodePointer(new Object());
 attributeContext.currentNodePointer = expectedNodePointer;
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertEquals("Expected and actual NodePointer should be equal",
 expectedNodePointer, actualNodePointer);
 }

 // Test nextNode() method
 @Test(expected = IllegalStateException.class)
 public void testNextNodeBeforeStart() {
 attributeContext.nextNode();
 }

 @Test
 public void testNextNodeWithStarted() {
 AttributeContext attributeContextCopy = new AttributeContext(parentContext, nodeTest);
 attributeContextCopy.nextNode(); // Start the iterator
 NodePointer expectedNodePointer = new NodePointer(new Object());
 attributeContextCopy.currentNodePointer = expectedNodePointer;
 NodePointer actualNodePointer = attributeContextCopy.getCurrentNodePointer();
 Assert.assertEquals("Expected and actual NodePointer should be equal",
 expectedNodePointer, actualNodePointer);

 // Next Node
 attributeContextCopy.nextNode();
 NodePointer nextExpectedNodePointer = new NodePointer(new Object());
 NodePointer nextActualNodePointer = attributeContextCopy.getCurrentNodePointer();
 Assert.assertEquals("Expected and actual NodePointer should be equal",
 nextExpectedNodePointer, nextActualNodePointer);
 }

 // Test nextNode() method with no more nodes
 @Test
 public void testNextNodeNoMoreNodes() {
 AttributeContext attributeContextNoMoreNodes = new AttributeContext(parentContext, nodeTest);
 attributeContextNoMoreNodes.nextNode(); // Start the iterator
 attributeContextNoMoreNodes.nextNode(); // Move to the next node
 attributeContextNoMoreNodes.nextNode(); // Move to the next node
 Assert.assertFalse("CurrentNodePointer should be null after no more nodes",
 attributeContextNoMoreNodes.getCurrentNodePointer() != null);
 }
}