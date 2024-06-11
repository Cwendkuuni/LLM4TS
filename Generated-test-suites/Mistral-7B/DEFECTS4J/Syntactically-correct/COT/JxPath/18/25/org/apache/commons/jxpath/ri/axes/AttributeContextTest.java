import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private NodeTest nodeTest;

 @Before
 public void setUp() {
 parentContext = new EvalContext(null, null);
 nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testConstructor() {
 assert(attributeContext instanceof AttributeContext);
 }

 @Test
 public void testGetCurrentNodePointer() {
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertNull(currentNodePointer);
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 assertFalse(attributeContext.isSetStarted());
 assertNull(attributeContext.getIterator());
 }

 @Test
 public void testSetPosition() {
 attributeContext.setPosition(0);
 assertTrue(attributeContext.isSetStarted());
 assertNotNull(attributeContext.getIterator());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionNegative() {
 attributeContext.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionBeyondCurrent() {
 attributeContext.setPosition(1);
 attributeContext.nextNode();
 attributeContext.setPosition(2);
 }

 @Test
 public void testNextNodeFalse() {
 attributeContext.nextNode();
 attributeContext.nextNode();
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeTrue() {
 NodePointer currentNodePointer = null;
 for (int i = 0; i < 10; i++) {
 currentNodePointer = attributeContext.nextNode() != null ? attributeContext.getCurrentNodePointer() : null;
 Assert.assertNotNull(currentNodePointer);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextNodeNonNodeNameTest() {
 nodeTest = new NodeTest() {};
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 }

 @Test(expected = NullPointerException.class)
 public void testAttributeIteratorNull() {
 nodeTest = new NodeNameTest(null);
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAttributeIteratorEnd() {
 NodeIterator iterator = parentContext.getCurrentNodePointer().attributeIterator(new QName("test"));
 iterator.next();
 iterator.next();
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setIterator(iterator);
 attributeContext.nextNode();
 }
}