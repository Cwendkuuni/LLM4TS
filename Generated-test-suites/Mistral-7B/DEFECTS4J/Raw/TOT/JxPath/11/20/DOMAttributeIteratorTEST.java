package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private Node pointerNode;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() throws Exception {
 Document doc = DocumentBuilderFactory.newDocumentBuilder().newDocument();
 Element root = doc.createElement("root");
 root.setAttribute("attr1", "value1");
 root.setAttribute("xmlns:x1", "http://test.com/x1");
 root.setAttribute("xmlns:x2", "http://test.com/x2");
 root.setAttribute("attr2", "value2");
 root.setAttribute("attr3", "value3");
 doc.appendChild(root);
 pointerNode = new DOMAttrributePointer(new DOMParentPointer(doc, doc), root);
 }

 @Test
 public void testEmptyIterator() {
 DOMAttributeIterator emptyIterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "*"));
 assertEquals(0, emptyIterator.getPosition());
 assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testIteratorWithOneMatchingAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr1"));
 assertEquals(pointerNode, iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneMatchingAttributeInNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("x1", "attr1"));
 assertEquals(pointerNode, iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleMatchingAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr2"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleMatchingAttributesInNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("x2", "attr2"));
 assertNotNull(iterator.getNodePointer());
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoMatchingAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr4"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoMatchingAttributesInNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("x3", "attr4"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorHasNext() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr1"));
 iterator.next();
 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr1"));
 NodePointer nextNode = iterator.next();
 assertEquals(pointerNode.getParent(), nextNode.getParent());
 assertEquals("root", nextNode.getNodeName());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorGetPositionNegative() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr1"));
 iterator.next();
 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorGetPositionGreaterThanSize() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(pointerNode.getParent(), pointerNode.getParent()), new QName("", "attr1"));
 iterator.next();
 iterator.setPosition(5);
 }
}