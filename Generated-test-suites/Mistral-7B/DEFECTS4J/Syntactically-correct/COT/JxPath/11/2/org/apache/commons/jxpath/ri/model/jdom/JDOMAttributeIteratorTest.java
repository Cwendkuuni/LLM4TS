package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private JDOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Element element = new Element("root");
 element.addAttribute("attr1", "value1");
 element.addAttribute("attr2", "value2");
 nodePointer = new NodePointer(element);
 }

 @Test
 public void testConstructorWithQualifiedName() {
 QName qname = new QName("", "attr1");
 iterator = new JDOMAttributeIterator(nodePointer, qname);
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testConstructorWithUnqualifiedName() {
 QName qname = new QName("attr1");
 iterator = new JDOMAttributeIterator(nodePointer, qname);
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testConstructorWithWildcardName() {
 QName qname = new QName("*");
 iterator = new JDOMAttributeIterator(nodePointer, qname);
 assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testConstructorWithEmptyList() {
 NodePointer emptyParent = new NodePointer(null);
 iterator = new JDOMAttributeIterator(emptyParent, new QName(""));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetNodePointer() {
 Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
 assertEquals("value1", attribute.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointerOutOfBounds() {
 iterator.setPosition(-1);
 iterator.getNodePointer();
 }

 @Test
 public void testSetPosition() {
 iterator.setPosition(1);
 Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
 assertEquals("value2", attribute.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 iterator.setPosition(3);
 }

 @Test
 public void testGetPosition() {
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testHasNext() {
 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
 }
}