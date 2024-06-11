package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import static org.junit.jupiter.api.Assertions.*;

class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private DOMAttributeIterator attributeIterator;

 @BeforeEach
 void setup() {
 Document doc = createTestDocument();
 parentNode = new NodePointer(doc.getDocumentElement());
 QName name = new QName("test", "namespace", "http://test.com");
 attributeIterator = new DOMAttributeIterator(parentNode, name);
 }

 @Test
 void testEmptyIterator() {
 assertTrue(attributeIterator.setPosition(0));
 assertNull(attributeIterator.getNodePointer());
 }

 @Test
 void testIteratorWithOneAttribute() {
 assertFalse(attributeIterator.setPosition(1));
 assertNull(attributeIterator.getNodePointer());

 Element element = new Element("testElement", "http://test.com");
 Attr attr = element.setAttributeNode(createTestAttr("testAttribute", "testValue"));
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, name);
 assertTrue(attributeIterator.setPosition(1));
 NodePointer attrPointer = attributeIterator.getNodePointer();
 assertEquals(attr, attrPointer.getNode());
 }

 @Test
 void testIteratorWithMultipleAttributes() {
 element.setAttributeNode(createTestAttr("testAttribute1", "testValue1"));
 element.setAttributeNode(createTestAttr("testAttribute2", "testValue2"));
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, name);

 int position = 1;
 for (int i = 1; i <= 3; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertNotNull(attributePointer);
 assertInstanceOf(Attr.class, attributePointer.getNode());
 }
 }

 @Test
 void testIteratorWithNoMatchingAttribute() {
 Element element = new Element("testElement", "http://test.com");
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, new QName("*", "*", null));

 int position = 1;
 for (int i = 1; i <= 2; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 assertNull(attributeIterator.getNodePointer());
 }
 }

 @Test
 void testIteratorWithMismatchedNamespace() {
 Element element = new Element("testElementNS", "http://test.com");
 element.setAttributeNodeNS(createTestAttrNS("testAttribute", "testValue", "testNS", "http://testNS.com"));
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, new QName("test", "namespace2", null));

 int position = 1;
 for (int i = 1; i <= 2; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 assertNull(attributeIterator.getNodePointer());
 }
 }

 @Test
 void testIteratorWithMismatchedPrefix() {
 Element element = new Element("testElementNS", "http://test.com");
 element.setAttributeNodeNS(createTestAttrNS("testAttribute", "testValue", "testPrefix", "http://testNS.com"));
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, new QName("test", "testPrefix2", null));

 int position = 1;
 for (int i = 1; i <= 2; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 assertNull(attributeIterator.getNodePointer());
 }
 }

 @Test
 void testIteratorWithMismatchedLocalName() {
 Element element = new Element("testElementNS", "http://test.com");
 element.setAttributeNodeNS(createTestAttrNS("testAttribute", "testValue", null, "http://testNS.com"));
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, new QName("testAttribute2", "namespace", null));

 int position = 1;
 for (int i = 1; i <= 2; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 assertNull(attributeIterator.getNodePointer());
 }
 }

 @Test
 void testIteratorWithNullAttributeName() {
 Element element = new Element("testElement", "http://test.com");
 parentNode.setNode(element);
 attributeIterator = new DOMAttributeIterator(parentNode, null);

 int position = 1;
 for (int i = 1; i <= 2; i++) {
 assertTrue(attributeIterator.setPosition(position++));
 assertNull(attributeIterator.getNodePointer());
 }
 }

 private Attr createTestAttr(String localName, String value) {
 Attr attr = doc.createAttribute(localName);
 attr.setValue(value);
 return attr;
 }

 private Attr createTestAttrNS(String localName, String value, String prefix, String namespaceURI) {
 Attr attr = doc.createAttributeNS(namespaceURI, localName);
 attr.setValue(value);
 if (prefix != null) {
 attr.setPrefix(prefix);
 }
 return attr;
 }

 private Document createTestDocument() {
 Document doc = doc.createDocument();
 Element root = doc.createElement("root");
 doc.appendChild(root);
 DocumentType docType = doc.createDocumentType("test", null, null);
 doc.insertBefore(docType, root);
 return doc;
 }
}