package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;

public class JDOMAttributeIteratorTest {

 @org.junit.Before
 public void setUp() {
 String xml = "<root xmlns:x=\"http://test.com/namespace\"><element x:myAttribute=\"value\"><subElement x:anotherAttribute=\"otherValue\"/></element></root>";
 try {
 Document doc = Document.parse(new org.jdom2.input.StringBuilderParser().parse(xml));
 root = doc.getRootElement();
 } catch (JDOMException e) {
 e.printStackTrace();
 fail("Failed to parse XML string");
 }
 }

 private Element root;

 @org.junit.Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), null);
 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 }

 @org.junit.Test
 public void testSingleAttributeIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root.getChild(0)), new QName("myAttribute"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertSame(root.getChild(0).getAttribute("myAttribute"), iterator.next());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testMultipleAttributesIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root.getChild(0)), null);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 Attribute attribute1 = (Attribute) iterator.next();
 assertEquals("value", attribute1.getValue());
 assertEquals("x", attribute1.getNamespace().getQualifiedName().getLocalPart());
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 Attribute attribute2 = (Attribute) iterator.next();
 assertEquals("otherValue", attribute2.getValue());
 assertEquals("x", attribute2.getNamespace().getQualifiedName().getLocalPart());
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorWithPrefix() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root.getChild(0)), new QName("x:myAttribute"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("value", attribute.getValue());
 assertEquals("http://test.com/namespace", attribute.getNamespace().getURI());
 assertEquals("myAttribute", attribute.getName().getLocalPart());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testAttributeIteratorWithWildcard() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root.getChild(0)), null);
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 Attribute attribute1 = (Attribute) iterator.next();
 assertEquals("myAttribute", attribute1.getName().getLocalPart());
 assertEquals("value", attribute1.getValue());
 assertEquals("x", attribute1.getNamespace().getQualifiedName().getLocalPart());
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 Attribute attribute2 = (Attribute) iterator.next();
 assertEquals("anotherAttribute", attribute2.getName().getLocalPart());
 assertEquals("otherValue", attribute2.getValue());
 assertEquals("x", attribute2.getNamespace().getQualifiedName().getLocalPart());
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 private NodeIterator createIterator(NodePointer nodePointer, QName qname) {
 return new JDOMAttributeIterator(nodePointer, qname);
 }

 @org.junit.Test
 public void testNextNode() {
 NodeIterator iterator = createIterator(new NodePointer(root), null);
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
 Attribute attribute = (Attribute) nodePointer.getNode();
 assertEquals("value", attribute.getValue());
 assertEquals("x", attribute.getNamespace().getQualifiedName().getLocalPart());
 }

 @org.junit.Test
 public void testPreviousNode() {
 NodeIterator iterator = createIterator(new NodePointer(root.getChild(0).getChild(0)), null);
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.previous();
 assertNotNull(nodePointer);
 assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
 Attribute attribute = (Attribute) nodePointer.getNode();
 assertEquals("otherValue", attribute.getValue());
 assertEquals("x", attribute.getNamespace().getQualifiedName().getLocalPart());
 }

 private NodePointer nodePointer;

 @org.junit.Test
 public void testHasNext() {
 NodeIterator iterator = createIterator(new NodePointer(root), null);
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testHasPrevious() {
 NodeIterator iterator = createIterator(new NodePointer(root.getChild(0).getChild(0)), null);
 iterator.next();
 iterator.next();
 assertTrue(iterator.hasPrevious());
 }

}