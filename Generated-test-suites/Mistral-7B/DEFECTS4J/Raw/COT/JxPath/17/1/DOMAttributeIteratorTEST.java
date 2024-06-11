import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @Test
 public void testConstructor() {
 Node node = MockNode.createMockElement();
 QName name = new QName("test", "testNamespace", "testPrefix");
 iterator = new DOMAttributeIterator(new NodePointer(node), name);
 Assert.assertNotNull(iterator);
 }

 @Test
 public void testConstructorWithWildcardName() {
 Node node = MockNode.createMockElement();
 iterator = new DOMAttributeIterator(new NodePointer(node), new QName("*"));
 Assert.assertFalse("Wildcard constructor should not be empty", iterator.hasNext());
 }

 @Test
 public void testNextAttribute() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 node.setAttributeNode(new Attr("testAttr2", "testValue2"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 iterator.next();
 Assert.assertEquals(new DOMAttributePointer(nodePointer, iterator.current()), new DOMAttributePointer(nodePointer, node.getAttributeNode("testAttr1")));
 iterator.next();
 Assert.assertEquals(new DOMAttributePointer(nodePointer, iterator.current()), new DOMAttributePointer(nodePointer, node.getAttributeNode("testAttr2")));
 }

 @Test
 public void testNextAttributeWithNamespace() {
 Node node = MockNode.createMockElement();
 node.setAttributeNodeNS(new Attr("test:testAttr1", "testValue1"), "test:testNamespace");
 node.setAttributeNodeNS(new Attr("test:testAttr2", "testValue2"), "test:testNamespace");
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test:test"));
 iterator.next();
 Assert.assertEquals(new DOMAttributePointer(nodePointer, iterator.current()), new DOMAttributePointer(nodePointer, node.getAttributeNodeNS(new QName("test:testNamespace", "test:testAttr1"), null)));
 iterator.next();
 Assert.assertEquals(new DOMAttributePointer(nodePointer, iterator.current()), new DOMAttributePointer(nodePointer, node.getAttributeNodeNS(new QName("test:testNamespace", "test:testAttr2"), null)));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextAttributeOutOfBounds() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test
 public void testHasNext() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 node.setAttributeNode(new Attr("testAttr2", "testValue2"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 Assert.assertTrue(iterator.hasNext());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testPosition() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 node.setAttributeNode(new Attr("testAttr2", "testValue2"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 iterator.next();
 iterator.setPosition(1);
 Assert.assertEquals(iterator.getPosition(), 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionNegative() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 node.setAttributeNode(new Attr("testAttr2", "testValue2"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 iterator.next();
 iterator.setPosition(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionOutOfBounds() {
 Node node = MockNode.createMockElement();
 node.setAttributeNode(new Attr("testAttr1", "testValue1"));
 node.setAttributeNode(new Attr("testAttr2", "testValue2"));
 nodePointer = new NodePointer(node);
 iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
 iterator.next();
 iterator.next();
 iterator.setPosition(3);
 }
}