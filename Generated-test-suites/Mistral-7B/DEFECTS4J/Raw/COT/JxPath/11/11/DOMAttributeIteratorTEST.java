package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private Element element;
 private DOMAttributeIterator iterator;

 @Before
 public void setup() {
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 element = document.createElement("root");
 DocumentType doctype = document.createDocumentType("root", null, null);
 element.appendChild(doctype);
 element.setAttribute("attr1", "value1");
 element.setAttributeNS("xmlns:x", "prefix:local", "value2");
 element.setAttribute("attr3", "value3");
 nodePointer = new DOMPointerImpl(document, document);
 }

 @Test
 public void testConstructorNullParent() {
     assertNull(new DOMAttributeIterator(null, new QName("*")));
 }

 @Test
 public void testConstructorNullName() {
     assertNull(new DOMAttributeIterator(nodePointer, null));
 }

 @Test
 public void testConstructorEmptyIterator() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithSingleAttribute() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("attr1"));
     assertTrue(iterator.hasNext());
     NodePointer nodePointer = iterator.next();
     Assert.assertEquals("attr1", nodePointer.getName().getLocalPart());
 }

 @Test
 public void testConstructorWithMultipleAttributes() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     assertTrue(iterator.hasNext());
     NodePointer nodePointer1 = iterator.next();
     Assert.assertEquals("attr1", nodePointer1.getName().getLocalPart());
     assertTrue(iterator.hasNext());
     NodePointer nodePointer2 = iterator.next();
     Assert.assertEquals("attr3", nodePointer2.getName().getLocalPart());
     assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithNamespaceQName() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("xmlns:x", "prefix:local"));
     assertTrue(iterator.hasNext());
     NodePointer nodePointer = iterator.next();
     Assert.assertEquals("prefix:local", nodePointer.getName().getLocalPart());
 }

 @Test
 public void testConstructorWithNamespaceURI() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*", "http://www.w3.org/2000/xmlns/"));
     assertTrue(iterator.hasNext());
     NodePointer nodePointer1 = iterator.next();
     Assert.assertEquals("xmlns:x", nodePointer1.getName().getPrefix());
     assertTrue(iterator.hasNext());
     NodePointer nodePointer2 = iterator.next();
     Assert.assertEquals("attr1", nodePointer2.getName().getLocalPart());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidQName() {
     new DOMAttributeIterator(nodePointer, new QName("", ""));
 }

 @Test
 public void testHasNext() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     assertTrue(iterator.hasNext());
     assertTrue(iterator.hasNext());
     assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextNullIterator() {
     DOMAttributeIterator iterator = null;
     assertNull(iterator.next());
 }

 @Test
 public void testNextNullAttribute() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     iterator.next();
     iterator.next();
     assertNull(iterator.next());
 }

 @Test
 public void testGetPosition() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     iterator.next();
     iterator.next();
     int position = iterator.getPosition();
     assertEquals(2, position);
 }

 @Test
 public void testSetPositionLessThanOne() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     assertFalse(iterator.setPosition(-1));
 }

 @Test
 public void testSetPositionGreaterThanSize() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     iterator.next();
     iterator.next();
     assertFalse(iterator.setPosition(4));
 }

 @Test
 public void testSetPositionValidPosition() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
     iterator.next();
     iterator.next();
     assertTrue(iterator.setPosition(1));
     NodePointer nodePointer = iterator.next();
     Assert.assertEquals("attr1", nodePointer.getName().getLocalPart());
 }
}