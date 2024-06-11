package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Testing for DOMAttributeIterator class
 */
public class DOMAttributeIteratorTest {

 private Document document;
 private Element root;

 @Before
 public void setUp() {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 root = document.createElement("root");
 document.appendChild(root);

 root.setAttribute("attr1", "value1");
 root.setAttribute("xmlns:x", "http://test.com");
 root.setAttributeNS("xmlns:x", "xml:id", "id1");
 root.setAttributeNS("xmlns:x", "xml:space", "preserve");
 }

 @Test
 public void testAttributeIterator() {
 DOMAttributeIterator attrIterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName(""));
 NodePointer nodePointer;
 int position = 0;

 assertNotNull(attrIterator);
 assertEquals(5, attrIterator.getSize());

 // Test getNodePointer() method
 while ((nodePointer = attrIterator.getNodePointer()) != null) {
 position++;
 Attr attr = (Attr) nodePointer.getNode();
 System.out.println("Position: " + position + "\tName: " + attr.getLocalName() + "\tValue: " + attr.getValue());
 Assert.assertNotNull(attr);
 }

 assertEquals(5, position);
 }

 @Test
 public void testAttributeIteratorWithPrefix() {
 DOMAttributeIterator attrIterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("xmlns:x"));
 NodePointer nodePointer;
 int position = 0;

 assertNotNull(attrIterator);
 assertEquals(2, attrIterator.getSize());

 // Test getNodePointer() method
 while ((nodePointer = attrIterator.getNodePointer()) != null) {
 position++;
 Attr attr = (Attr) nodePointer.getNode();
 System.out.println("Position: " + position + "\tName: " + attr.getLocalName() + "\tValue: " + attr.getValue());
 Assert.assertNotNull(attr);
 Assert.assertEquals("xml:id", attr.getLocalName());
 if (attr.getNamespaceURI() != null) {
 Assert.assertEquals("http://test.com", attr.getNamespaceURI());
 }
 }

 assertEquals(2, position);
 }

 @Test
 public void testAttributeIteratorWithName() {
 DOMAttributeIterator attrIterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("attr1"));
 NodePointer nodePointer;
 int position = 0;

 assertNotNull(attrIterator);
 assertEquals(1, attrIterator.getSize());

 // Test getNodePointer() method
 while ((nodePointer = attrIterator.getNodePointer()) != null) {
 position++;
 Attr attr = (Attr) nodePointer.getNode();
 System.out.println("Position: " + position + "\tName: " + attr.getLocalName() + "\tValue: " + attr.getValue());
 Assert.assertNotNull(attr);
 Assert.assertEquals("attr1", attr.getLocalName());
 Assert.assertEquals("value1", attr.getValue());
 }

 assertEquals(1, position);
 }

 @Test
 public void testEmptyAttributeIterator() {
 DOMAttributeIterator attrIterator = new DOMAttributeIterator(new DOMPointer(document, document), new QName(""));
 NodePointer nodePointer;
 int position = 0;

 assertNotNull(attrIterator);
 assertEquals(0, attrIterator.getSize());

 // Test getNodePointer() method
 while ((nodePointer = attrIterator.getNodePointer()) != null) {
 position++;
 Assert.fail();
 }

 assertEquals(0, position);
 }

 @Test
 public void testAttributeIteratorWithWrongName() {
 DOMAttributeIterator attrIterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("wrongName"));
 NodePointer nodePointer;
 int position = 0;

 assertNotNull(attrIterator);
 assertEquals(0, attrIterator.getSize());

 // Test getNodePointer() method
 while ((nodePointer = attrIterator.getNodePointer()) != null) {
 position++;
 Assert.fail();
 }

 assertEquals(0, position);
 }
}