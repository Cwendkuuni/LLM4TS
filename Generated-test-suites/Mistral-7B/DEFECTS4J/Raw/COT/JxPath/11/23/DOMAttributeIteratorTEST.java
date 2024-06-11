package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator iterator;

 @Before
 public void setup() {
 Node node = new MockNode("root");
 Element element = new MockElement("element");
 element.setAttribute("test", "testValue");
 node.appendChild(element);
 parentNode = new NodePointer(node);
 }

 @Test
 public void testConstructorWithSpecificName() {
 QName name = new QName("test");
 iterator = new DOMAttributeIterator(parentNode, name);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWithWildcardName() {
 iterator = new DOMAttributeIterator(parentNode, null);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testGetNodePointer() {
 QName name = new QName("test");
 iterator = new DOMAttributeIterator(parentNode, name);
 NodePointer nodePointer = iterator.getNodePointer();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointerWithWrongPosition() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(2);
 iterator.getNodePointer();
 }

 @Test
 public void testSetPositionWithCorrectPosition() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(1);
 assertTrue(iterator.getNodePointer().getNode() instanceof Attr);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithWrongPosition() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(3);
 }

 @Test
 public void testSizeWithOneAttribute() {
 iterator = new DOMAttributeIterator(parentNode, null);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testSizeWithMultipleAttributes() {
 Element newElement = new MockElement("newElement");
 newElement.setAttribute("attr1", "value1");
 newElement.setAttribute("attr2", "value2");
 parentNode.setNode(new Node[]{node, element});
 iterator = new DOMAttributeIterator(parentNode, null);
 assertEquals(2, iterator.size());
 }

 @Test
 public void testHasNextWithOneAttribute() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNextWithOneAttribute() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.next();
 NodePointer nextNode = iterator.next();
 assertNull(nextNode);
 }

 @Test(expected = NoSuchElementException.class)
 public void testNextWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.next();
 iterator.next();
 iterator.next();
 assertNull(iterator.next());
 }

 @Test
 public void testNextWithSpecificName() {
 QName name = new QName("test");
 iterator = new DOMAttributeIterator(parentNode, name);
 NodePointer nextNode = iterator.next();
 assertEquals("test", nextNode.getNode().getNodeName());
 }

 @Test
 public void testNextWithSpecificNameAndMultipleAttributes() {
 QName name1 = new QName("test1");
 QName name2 = new QName("test2");
 iterator = new DOMAttributeIterator(parentNode, name1);
 NodePointer nextNode1 = iterator.next();
 iterator.next();
 nextNode1 = iterator.next();
 assertEquals("test1", nextNode1.getNode().getNodeName());
 iterator.setPosition(0);
 iterator = new DOMAttributeIterator(parentNode, name2);
 nextNode1 = iterator.next();
 assertEquals("test2", nextNode1.getNode().getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullParentNode() {
 iterator = new DOMAttributeIterator(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullQName() {
 iterator = new DOMAttributeIterator(parentNode, null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttributeWithNullElement() {
 iterator = new DOMAttributeIterator(new NodePointer(new MockNode("root")), null);
 Element element = null;
 iterator.getAttribute(element, new QName("test"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAttributeWithNullName() {
 iterator = new DOMAttributeIterator(new NodePointer(new MockNode("root")), new QName("test"));
 Element element = new MockElement("element");
 iterator.getAttribute(null, new QName("test"));
 }

 @Test
 public void testGetAttribute() {
 QName name = new QName("test");
 iterator = new DOMAttributeIterator(parentNode, name);
 Attr attribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals("testValue", attribute.getValue());
 }

 @Test
 public void testHasNextWithEmptyNode() {
 Node emptyNode = new MockNode("emptyNode");
 parentNode.setNode(emptyNode);
 iterator = new DOMAttributeIterator(parentNode, null);
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionWithWrongParentNode() {
 NodePointer wrongParentNode = new NodePointer(new MockNode("wrongParentNode"));
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(0, wrongParentNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionWithNullParentNode() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(0, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithWrongPositionAndDifferentParentNode() {
 NodePointer wrongParentNode = new NodePointer(new MockNode("wrongParentNode"));
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(2, wrongParentNode);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithWrongPositionAndSameParentNode() {
 iterator = new DOMAttributeIterator(parentNode, null);
 iterator.setPosition(3);
 }
}