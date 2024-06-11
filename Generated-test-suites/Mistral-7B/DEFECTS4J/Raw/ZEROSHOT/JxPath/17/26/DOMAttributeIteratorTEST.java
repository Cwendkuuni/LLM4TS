package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x=\"http://test.com/ns\"><el xmlns:y=\"http://test.com/ns2\"><attr1 x:a=\"val1\"/><attr2 x:b=\"val2\"/><attr3 y:c=\"val3\"/></el></root>";
 Document doc = JaxpFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
 element = (Element) doc.getElementsByTagName("el").item(0);
 }

 @Test
 public void testIteratorWithEmptyName() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), null);
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.getPosition());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("x", "a"));
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.getPosition());
 Attr attr = (Attr) iterator.next();
 assertEquals("attr1", attr.getName().getNodeName());
 assertEquals("val1", attr.getValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("x", "*"));
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.getPosition());
 Attr attr1 = (Attr) iterator.next();
 assertEquals("attr1", attr1.getName().getNodeName());
 assertEquals("val1", attr1.getValue());
 assertTrue(iterator.hasNext());
 Attr attr2 = (Attr) iterator.next();
 assertEquals("attr2", attr2.getName().getNodeName());
 assertEquals("val2", attr2.getValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithAttributeFromDifferentNamespace() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("y", "c"));
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.getPosition());
 Attr attr = (Attr) iterator.next();
 assertEquals("attr3", attr.getName().getNodeName());
 assertEquals("val3", attr.getValue());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithNegativePosition() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("x", "*"));
 iterator.setPosition(-1);
 iterator.next();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithPositionGreaterThanSize() {
 iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("x", "*"));
 iterator.setPosition(3);
 iterator.next();
 }
}

class DOMPointer implements NodePointer {
 private Node node;

 public DOMPointer(Node node) {
 this.node = node;
 }

 public Node getNode() {
 return node;
 }

 public String getNamespaceURI(String prefix) {
 return node.getNamespaceURI();
 }
}