import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.Axis;
import org.apache.commons.jxpath.ri.model.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private Node iteratorNode;
 private Node testNode;

 @Before
 public void setUp() {
 parentContext = new EvalContext(null, Axis.SELF);
 testNode = new Node(1, "testNode", null, null);
 iteratorNode = new Node(2, "iteratorNode", null, null);
 parentContext.setCurrentNodePointer(new NodePointer(testNode));
 }

 @Test
 public void testAttributeContextWithValidNodeTest() {
 NodeNameTest nodeTest = new NodeNameTest(new QName("test"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertTrue(attributeContext.nextNode());
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(iteratorNode, currentNodePointer.getNode());
 }

 @Test
 public void testAttributeContextWithInvalidNodeTest() {
 NodeTest nodeTest = new NodeTest() {
 @Override
 public boolean matches(Node node) {
 return false;
 }
 };
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testAttributeContextWithEmptyIterateMethod() {
 parentContext.getCurrentNodePointer().setCurrentIterator(null);
 attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("test")));
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testAttributeContextWithNullIterator() {
 attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("test")));
 parentContext.getCurrentNodePointer().setAttributeIterator(null);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testAttributeContextWithInvalidQName() {
 NodeNameTest nodeTest = new NodeNameTest(new QName("test:invalid"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testAttributeContextWithNoSuchAttribute() {
 NodeNameTest nodeTest = new NodeNameTest(new QName("nonexistent"));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertFalse(attributeContext.nextNode());
 }
}