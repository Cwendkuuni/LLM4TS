import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Test
 public void testAttributeContextConstructor() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testGetCurrentNodePointer() {
 // Basic functionality test
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0);
 attributeContext.setCurrentNodePointer(expectedNodePointer);
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertEquals(expectedNodePointer, actualNodePointer);

 // Edge case: null node pointer
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertNull(actualNodePointer);
 }

 @Test
 public void testReset() {
 // Basic functionality test
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 Assert.assertFalse(attributeContext.isSetStarted());
 Assert.assertNull(attributeContext.getIterator());
 Assert.superCallOrder(new AssertionError(), parentContext, "reset");

 // Edge case: setStarted already false
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.reset();
 Assert.assertFalse(attributeContext.isSetStarted());
 Assert.assertNull(attributeContext.getIterator());
 Assert.assertFalse(parentContext.isSetStarted());
 Assert.assertNull(parentContext.getIterator());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionNegative() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setPosition(-1);
 }

 @Test
 public void testSetPositionEqual() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setPosition(0);
 }

 @Test
 public void testSetPositionGreater() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setPosition(1);
 }

 @Test
 public void testNextNodeFalse() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 Assert.assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeTrue() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 NodePointer expectedNodePointer = new NodePointer(new Object(), 0);
 attributeContext.setCurrentNodePointer(expectedNodePointer);
 Assert.assertTrue(attributeContext.nextNode());
 NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
 Assert.assertNotSame(expectedNodePointer, actualNodePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextNodeAfterEnd() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.nextNode();
 }

 @Test
 public void testIteratorNull() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest("test");
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setIterator(null);
 Assert.assertFalse(attributeContext.hasNextNode());
 Assert.assertNull(attributeContext.getCurrentNodePointer());
 }
}