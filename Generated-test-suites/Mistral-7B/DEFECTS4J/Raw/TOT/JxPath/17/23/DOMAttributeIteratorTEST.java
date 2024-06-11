package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com/x' xmlns:y='http://test.com/y'>" +
 "<child attr1='value1' attr2='value2' attr3='value3'/>" +
 "</root>";
 try {
 Document doc = JaxpFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
 element = (Element) doc.getDocumentElement();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAttributeIteratorForEmptyNode() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testAttributeIteratorForElementWithOneAttribute() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), new QName("attr1", "http://test.com/x"));
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertEquals("value1", nodePointer.getNodeValue());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorForElementWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertEquals("value1", nodePointer.getNodeValue().getNodeValue());
 assertTrue(iterator.hasNext());
 nodePointer = iterator.next();
 assertEquals("value2", nodePointer.getNodeValue().getNodeValue());
 assertTrue(iterator.hasNext());
 nodePointer = iterator.next();
 assertEquals("value3", nodePointer.getNodeValue().getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorForElementWithNamespace() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), new QName("x:attr1", "http://test.com/x"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorForElementWithPrefix() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), new QName("attr1", "x"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorForElementWithWrongLocalName() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), new QName("attr3", "http://test.com/x"));
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorForNullElement() {
 iterator = new DOMAttributeIterator(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorForNullQName() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 }

 @Test
 public void testAttributeIteratorForEmtyList() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 iterator.setPosition(attributes.size() + 1);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorForPositionAfterLastElement() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 iterator.setPosition(attributes.size() + 1);
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributeIteratorForPositionBeforeFirstElement() {
 iterator = new DOMAttributeIterator(new DOMPointNodePointer(element), null);
 iterator.setPosition(-1);
 }
}