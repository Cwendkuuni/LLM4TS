import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttributeContextTest {

 private AttributeContext attributeContext;
 private NodeIterator iterator;
 private Node node;

 @BeforeAll
 public void setUp() {
 Node testNode = createTestNode();
 JXPathContext context = new JXPathContext(testNode);
 attributeContext = new AttributeContext(context.getContextNode(), new NodeNameTest("testAttr"));
 }

 @Test
 public void testNextNode_ReturnsTrueWhenNextAttributeIsPresent() {
 iterator = attributeContext.getCurrentNodePointer().attributeIterator(new QName("testAttr"));
 assertTrue(attributeContext.nextNode());
 assertEquals(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testNextNode_ReturnsFalseWhenNoMoreAttributesToIterate() {
 iterator = attributeContext.getCurrentNodePointer().attributeIterator(new QName("nonExistentAttr"));
 assertFalse(attributeContext.nextNode());
 assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testSetPosition_SetsCorrectPosition() {
 attributeContext.setPosition(1);
 Node secondAttribute = attributeContext.getCurrentNodePointer().getNode();
 assertEquals("second_testAttr", secondAttribute.getNodeName().getLocalPart());
 }

 @Test
 public void testSetPosition_ThrowsIllegalArgumentExceptionWhenPositionIsNegative() {
 assertThrows(IllegalArgumentException.class, () -> attributeContext.setPosition(-1));
 }

 @Test
 public void testSetPosition_ThrowsIllegalArgumentExceptionWhenPositionIsGreaterThanNumberOfAttributes() {
 attributeContext.reset();
 iterator = attributeContext.getCurrentNodePointer().attributeIterator(new QName("testAttr"));
 iterator.next();
 assertThrows(IllegalArgumentException.class, () -> attributeContext.setPosition(2));
 }

 @Test
 public void testReset_ResetsToInitialState() {
 attributeContext.reset();
 assertFalse(attributeContext.nextNode());
 assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testNextNode_ThrowsIllegalStateExceptionWhenIteratorIsClosed() {
 attributeContext.reset();
 iterator = attributeContext.getCurrentNodePointer().attributeIterator(new QName("testAttr"));
 iterator.close();
 assertThrows(IllegalStateException.class, () -> attributeContext.nextNode());
 }

 private Node createTestNode() {
 Node testNode = new Node();
 testNode.addAttribute("testAttr", "testValue1");
 testNode.addAttribute("testAttr", "testValue2");
 testNode.addAttribute("testAttr", "testValue3");
 testNode.addAttribute("nonExistentAttr", "nonExistentValue");
 return testNode;
 }

}