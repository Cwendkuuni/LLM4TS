package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private Node createTestElement(String tagName, String attrName, String attrValue, String... namespaceURIs) {
  Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newElement(tagName);
  for (String namespaceURI : namespaceURIs) {
   element.setNamespaceURI(namespaceURI);
  }
  element.setAttribute(attrName, attrValue);
  return element;
 }

 @Test
 public void testConstructorWithElementAndExactName() {
  Node parent = new DOMAttributeIterator(new DOMPointer(null), new QName("test", "test")).getNodePointer();
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(null, new Element("test")), new QName("test", "test"));
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected = parent;
  expected.setNode(actual.getNode());
  Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullParent() {
  new DOMAttributeIterator(null, new QName("test", "test"));
 }

 @Test
 public void testConstructorWithNullName() {
  Node parent = new DOMPointer(null);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, null);
  Assert.assertNull(attributeIterator.getAttributeName());
  NodePointer actual = attributeIterator.getNodePointer();
  Assert.assertNull(actual.getNodeName());
 }

 @Test
 public void testConstructorWithEmptyName() {
  Node parent = new DOMPointer(null);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName(""));
  Assert.assertNull(attributeIterator.getAttributeName());
  NodePointer actual = attributeIterator.getNodePointer();
  Assert.assertNull(actual.getNodeName());
 }

 @Test
 public void testConstructorWithElementAndWildcardName() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test", "testValue");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("*"));
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected = new DOMAttributePointer(parent, testElement.getAttributeNode("test"));
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConstructorWithElementAndNamespaceName() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test:test", "test:test", "testValue", "http://test.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test"));
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected = new DOMAttributePointer(parent, testElement.getAttributeNodeNS("http://test.com", "test:test"));
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConstructorWithElementAndNoMatchingName() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test2", "testValue2");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test"));
  Assert.assertNull(attributeIterator.getNodePointer());
 }

 @Test
 public void testConstructorWithElementAndMultipleMatchingNames() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  Element testElement2 = createTestElement("test", "test2", "testValue2", "http://test2.com");
  parent.setNode(testElement);
  List<Node> children = new ArrayList<>();
  children.add(testElement2);
  ((Element) parent.getNode()).appendChild(children.toArray(new Node[0]));
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "*"));
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected1 = new DOMAttributePointer(parent, testElement.getAttributeNodeNS("http://test1.com", "test:test1"));
  NodePointer expected2 = new DOMAttributePointer(parent, testElement2.getAttributeNodeNS("http://test2.com", "test:test2"));
  Assert.assertEquals(expected1, actual);
  Assert.assertEquals(expected2, attributeIterator.getNodePointer());
 }

 @Test
 public void testNext() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected = new DOMAttributePointer(parent, testElement.getAttributeNodeNS("http://test1.com", "test:test1"));
  Assert.assertEquals(expected, actual);
  attributeIterator.next();
  actual = attributeIterator.getNodePointer();
  Node testElement2 = createTestElement("test", "test2", "testValue2", "http://test2.com");
  NodePointer expected2 = new DOMAttributePointer(parent, testElement2.getAttributeNodeNS("http://test2.com", "test:test2"));
  Assert.assertEquals(expected2, actual);
 }

 @Test
 public void testHasNext() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  Assert.assertTrue(attributeIterator.hasNext());
  Node testElement2 = createTestElement("test", "test2", "testValue2", "http://test2.com");
  parent.setNode(testElement2);
  attributeIterator.next();
  Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNoMoreElements() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  attributeIterator.next();
  attributeIterator.next();
 }

 @Test
 public void testNullParentAndName() {
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(null, null);
  Assert.assertNull(attributeIterator.getNodePointer());
  Assert.assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testEmptyIterator() {
  Node parent = new DOMPointer(null);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("*"));
  Assert.assertFalse(attributeIterator.hasNext());
  Assert.assertNull(attributeIterator.getNodePointer());
 }

 @Test
 public void testGetPosition() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  Assert.assertEquals(0, attributeIterator.getPosition());
  attributeIterator.next();
  Assert.assertEquals(1, attributeIterator.getPosition());
 }

 @Test
 public void testSetPosition() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  attributeIterator.next();
  attributeIterator.setPosition(0);
  Assert.assertEquals(0, attributeIterator.getPosition());
  NodePointer actual = attributeIterator.getNodePointer();
  NodePointer expected = new DOMAttributePointer(parent, testElement.getAttributeNodeNS("http://test1.com", "test:test1"));
  Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  attributeIterator.next();
  attributeIterator.next();
  attributeIterator.setPosition(1);
 }

 @Test
 public void testGetNodeIterator() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  DOMAttributeIterator nodeIterator = attributeIterator.getNextSibling();
  Node testElement2 = createTestElement("test", "test2", "testValue2", "http://test2.com");
  parent.setNode(testElement2);
  attributeIterator.next();
  Assert.assertNull(nodeIterator.getNodePointer());
  nodeIterator = attributeIterator.getNextSibling();
  Node testElement3 = createTestElement("test", "test3", "testValue3", "http://test3.com");
  parent.setNode(testElement3);
  attributeIterator.next();
  attributeIterator.next();
  NodePointer actual = nodeIterator.getNodePointer();
  NodePointer expected = new DOMAttributePointer(parent, testElement3.getAttributeNodeNS("http://test3.com", "test:test3"));
  Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetNextSiblingWithEmptyList() {
  Node parent = new DOMPointer(null);
  Element testElement = createTestElement("test", "test1", "testValue1", "http://test1.com");
  parent.setNode(testElement);
  DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, new QName("test", "test1"));
  Assert.assertNull(attributeIterator.getNextSibling().getNodePointer());
 }
}