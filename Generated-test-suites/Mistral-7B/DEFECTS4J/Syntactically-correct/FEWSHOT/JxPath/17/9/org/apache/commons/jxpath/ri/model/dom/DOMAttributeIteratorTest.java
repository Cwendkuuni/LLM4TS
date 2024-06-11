package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private Node createTestElement(String namespaceURI, String prefix, String localName) {
 String xml = "<root xmlns:" + prefix + "=\"" + namespaceURI + "\">"
             + "<elem " + prefix + ":myattr=\"foo\"/>"
             + "</root>";

 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 Document doc = builder.parse(new InputSource(new StringReader(xml)));
 Node root = doc.getDocumentElement();
 return root.getElementsByTagNameNS(namespaceURI, localName).item(0);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testEmptyIterator() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(null), null);
 assertNull(iterator.nextNode());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(createTestElement("http://test.com", "test", "myattr")), null);
 Attr attribute = (Attr) iterator.nextNode();
 assertEquals("foo", attribute.getValue());
 assertNull(iterator.nextNode());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(createTestElement("http://test.com", "test", "*")), null);
 assertNotNull(iterator.nextNode());
 assertNotNull(iterator.nextNode());
 }

 @Test
 public void testIteratorWithNamespaceAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(createTestElement("http://test.com", "test", null, "http://test.com")), new QName("http://test.com", "test", "myattr"));
 Attr attribute = (Attr) iterator.nextNode();
 assertEquals("foo", attribute.getValue());
 }

 @Test
 public void testIteratorWithMixedAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(createTestElement("", "test", "*")), new QName("", "test", "myattr"));
 Attr attribute1 = (Attr) iterator.nextNode();
 assertEquals("foo1", attribute1.getValue());
 Attr attribute2 = (Attr) iterator.nextNode();
 assertEquals("foo2", attribute2.getValue());
 }

 @Test
 public void testIteratorWithNonMatchingAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(createTestElement("http://test.com", "test", null, "http://test.com")), new QName("http://another", "test", "myattr"));
 assertNull(iterator.nextNode());
 }
}