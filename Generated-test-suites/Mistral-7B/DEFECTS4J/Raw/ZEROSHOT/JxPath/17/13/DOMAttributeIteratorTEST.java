package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator attributeIterator;
 private Element testElement;

 @Before
 public void setUp() {
 testElement = new Element.Builder("root").build();
 testElement.setAttribute("testAttribute1", "testValue1");
 testElement.setAttribute("xmlns:test", "http://test.com");
 testElement.setAttribute("testAttribute2", "testValue2");
 testElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
 testElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
 parentNode = new NodePointer(new NodeBuilder(new DocumentBuilder().parse("")).getDocumentElement());
 parentNode.setNode(testElement);
 }

 @Test
 public void testDOMAttributeIteratorEmpty() {
 DOMAttributeIterator emptyIterator = new DOMAttributeIterator(parentNode, null);
 assertEquals(0, emptyIterator.getPosition());
 assertNull(emptyIterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIteratorSingleAttribute() {
 DOMAttributeIterator singleAttributeIterator = new DOMAttributeIterator(parentNode, new QName("test:testAttribute1"));
 assertEquals(1, singleAttributeIterator.getPosition());
 NodePointer attributePointer = singleAttributeIterator.getNodePointer();
 assertInstanceOf(DOMAttributePointer.class, attributePointer);
 assertEquals("testValue1", attributePointer.getNodeValue());
 }

 @Test
 public void testDOMAttributeIteratorMultipleAttributes() {
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(parentNode, new QName("*"));
 assertEquals(5, multipleAttributesIterator.getPosition());
 for (int i = 0; i < 5; i++) {
 NodePointer attributePointer = multipleAttributesIterator.getNodePointer();
 assertInstanceOf(DOMAttributePointer.class, attributePointer);
 if (i == 0) {
 assertEquals("testAttribute1", attributePointer.getNodeName().getLocalPart());
 assertEquals("testValue1", attributePointer.getNodeValue());
 } else if (i == 1) {
 assertEquals("testAttribute2", attributePointer.getNodeName().getLocalPart());
 assertEquals("testValue2", attributePointer.getNodeValue());
 } else {
 assertEquals("xmlns:test", attributePointer.getNodeName().getLocalPart());
 assertNotNull(attributePointer.getNodeName().getPrefix());
 assertEquals("http://test.com", attributePointer.getNodeValue());
 }
 multipleAttributesIterator.next();
 }
 assertEquals(0, multipleAttributesIterator.getPosition());
 }

 @Test
 public void testDOMAttributeIteratorEmptyNamespace() {
 DOMAttributeIterator emptyNamespaceIterator = new DOMAttributeIterator(parentNode, new QName("xmlns:empty"));
 assertEquals(0, emptyNamespaceIterator.getPosition());
 assertNull(emptyNamespaceIterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIteratorSameNamespace() {
 DOMAttributeIterator sameNamespaceIterator = new DOMAttributeIterator(parentNode, new QName("xmlns:test"));
 assertEquals(5, sameNamespaceIterator.getPosition());
 for (int i = 0; i < 5; i++) {
 NodePointer attributePointer = sameNamespaceIterator.getNodePointer();
 assertInstanceOf(DOMAttributePointer.class, attributePointer);
 if (i == 0) {
 assertEquals("testAttribute1", attributePointer.getNodeName().getLocalPart());
 assertEquals("testValue1", attributePointer.getNodeValue());
 } else {
 assertEquals("xmlns:test", attributePointer.getNodeName().getLocalPart());
 assertNotNull(attributePointer.getNodeName().getPrefix());
 assertEquals("http://test.com", attributePointer.getNodeValue());
 }
 sameNamespaceIterator.next();
 }
 assertEquals(0, sameNamespaceIterator.getPosition());
 }

 @Test
 public void testDOMAttributeIteratorDifferentNamespace() {
 DOMAttributeIterator differentNamespaceIterator = new DOMAttributeIterator(parentNode, new QName("xmlns:xsi"));
 assertEquals(1, differentNamespaceIterator.getPosition());
 NodePointer attributePointer = differentNamespaceIterator.getNodePointer();
 assertInstanceOf(DOMAttributePointer.class, attributePointer);
 assertEquals("xmlns:xsi", attributePointer.getNodeName().getLocalPart());
 assertNotNull(attributePointer.getNodeName().getPrefix());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", attributePointer.getNodeValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIteratorNullParent() {
 new DOMAttributeIterator(null, new QName("testAttribute1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIteratorNullName() {
 new DOMAttributeIterator(parentNode, null);
 }
}