package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 private static final String TEST_NAMESPACE = "http://test.com";
 private static final String TEST_PREFIX = "test";
 private static final String TEST_LOCAL_NAME = "testAttr";
 private static final String TEST_VALUE = "testValue";

 @Test
 public void testDOMAttributeIterator_empty() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(-1, null), null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_singleAttribute() {
 Element element = new Element("testElement");
 Attr attr = element.setAttributeNode(new Attr("testAttr", TEST_VALUE));
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_PREFIX, TEST_LOCAL_NAME));
 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.hasNext());

 NodePointer attributePointer = iterator.next();
 assertSame(attr, attributePointer.getNode());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_multipleAttributes() {
 Element element = new Element("testElement");
 Attr attribute1 = element.setAttributeNodeNS(new Attr("xmlns:test", TEST_NAMESPACE), "xmlns:test");
 Attr attribute2 = element.setAttributeNode(new Attr("testAttr1", "testValue1"));
 Attr attribute3 = element.setAttributeNode(new Attr("testAttr2", "testValue2"));
 Attr attribute4 = element.setAttributeNode(new Attr("testAttr", TEST_VALUE));
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_PREFIX, TEST_LOCAL_NAME));

 assertEquals(0, iterator.getPosition());
 assertTrue(iterator.hasNext());

 NodePointer attributePointer1 = iterator.next();
 assertSame(attribute2, attributePointer1.getNode());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(1);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer2 = iterator.next();
 assertSame(attribute3, attributePointer2.getNode());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(2);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer3 = iterator.next();
 assertSame(attribute4, attributePointer3.getNode());
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_prefix() {
 Element element = new Element("testElement");
 Attr attr1 = element.setAttributeNodeNS(new Attr("xmlns:test", TEST_NAMESPACE), "xmlns:test");
 Attr attr2 = element.setAttributeNode(new Attr("testAttr1", "testValue1"));
 Attr attr3 = element.setAttributeNode(new Attr("test:testAttr", TEST_VALUE));
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_PREFIX, TEST_LOCAL_NAME));

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(1);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer1 = iterator.next();
 assertSame(attr1, attributePointer1.getNode());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(2);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer2 = iterator.next();
 assertSame(attr3, attributePointer2.getNode());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_prefix_namespace() {
 Element element = new Element("testElement");
 Attr attr1 = element.setAttributeNodeNS(new Attr("xmlns:test", TEST_NAMESPACE), "xmlns:test");
 Attr attr2 = element.setAttributeNode(new Attr("testAttr1", "testValue1"));
 Attr attr3 = element.setAttributeNodeNS(new Attr("test:testAttr", TEST_VALUE), TEST_PREFIX + ":testAttr");
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_PREFIX, TEST_LOCAL_NAME));

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(1);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer1 = iterator.next();
 assertSame(attr1, attributePointer1.getNode());
 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());

 iterator.setPosition(2);
 assertTrue(iterator.hasNext());

 NodePointer attributePointer2 = iterator.next();
 assertSame(attr3, attributePointer2.getNode());
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_prefix_namespace_empty() {
 Element element = new Element("testElement");
 Attr attr1 = element.setAttributeNodeNS(new Attr("xmlns:test", TEST_NAMESPACE), "xmlns:test");
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(TEST_PREFIX, TEST_LOCAL_NAME));

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_prefix_namespace_wrongPrefix() {
 Element element = new Element("testElement");
 Attr attr1 = element.setAttributeNodeNS(new Attr("xmlns:test", TEST_NAMESPACE), "xmlns:test");
 NodePointer parent = new NodePointer(new Node("testParent"), 0, null);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("wrongPrefix", TEST_LOCAL_NAME));

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }
}