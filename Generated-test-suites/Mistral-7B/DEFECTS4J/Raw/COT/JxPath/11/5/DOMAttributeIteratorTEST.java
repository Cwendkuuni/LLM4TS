package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private NodePointer parent;
 private Element element;

 @Before
 public void setUp() {
 parent = new DOMPointers.DOMElementPointer(createElement("root"));
 QName name = new QName("test", "http://test.com", "attr");
 element.setAttributeNS("http://test.com", "attr", "testValue");
 element.setAttributeNode(createElement("otherAttr"));
 element.setAttributeNode(createElement("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", "xsi"));
 }

 @Test
 public void testConstructor() {
 iterator = new DOMAttributeIterator(parent, new QName("*", "*", "*"));
 assertNotNull(iterator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullParent() {
 iterator = new DOMAttributeIterator(null, new QName("test", "http://test.com", "attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullName() {
 iterator = new DOMAttributeIterator(parent, null);
 }

 @Test
 public void testHasNext() {
 iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com", "attr"));
 assertEquals(true, iterator.hasNext());
 }

 @Test
 public void testHasNextEmpty() {
 iterator = new DOMAttributeIterator(parent, new QName("*", "*", "*"));
 assertEquals(false, iterator.hasNext());
 }

 @Test
 public void testNext() {
 iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com", "attr"));
 Attr nextAttr = (Attr) iterator.next();
 assertNotNull(nextAttr);
 assertEquals("attr", nextAttr.getNodeName());
 assertEquals("testValue", nextAttr.getNodeValue());
 }

 @Test(expected = NoSuchElementException.class)
 public void testNextEmpty() {
 iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com", "nonexistentAttr"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test
 public void testNextSameNameDifferentPrefix() {
 iterator = new DOMAttributeIterator(parent, new QName("test2", "http://test.com", "attr"));
 Attr nextAttr = (Attr) iterator.next();
 assertNotNull(nextAttr);
 assertEquals("attr", nextAttr.getNodeName());
 assertEquals("testValue", nextAttr.getNodeValue());
 }

 @Test
 public void testNextSameNameSamePrefix() {
 iterator = new DOMAttributeIterator(parent, new QName("test", "http://test.com", "attr"));
 Attr nextAttr1 = (Attr) iterator.next();
 Attr nextAttr2 = (Attr) iterator.next();
 assertNotNull(nextAttr1);
 assertNotNull(nextAttr2);
 assertEquals("attr", nextAttr1.getNodeName());
 assertEquals("attr", nextAttr2.getNodeName());
 assertEquals("testValue", nextAttr1.getNodeValue());
 assertEquals("testValue", nextAttr2.getNodeValue());
 }

 @Test
 public void testNextMixedNames() {
 iterator = new DOMAttributeIterator(parent, new QName("*", "*", "*"));
 Attr attr1 = (Attr) iterator.next();
 Attr attr2 = (Attr) iterator.next();
 Attr attr3 = (Attr) iterator.next();
 Attr attr4 = (Attr) iterator.next();
 Attr attr5 = (Attr) iterator.next();
 assertNotNull(attr1);
 assertNotNull(attr2);
 assertNotNull(attr3);
 assertNotNull(attr4);
 assertNotNull(attr5);
 assertEquals("otherAttr", attr1.getNodeName());
 assertNull(attr1.getNodeValue());
 assertNull(attr2.getNodeValue());
 assertEquals("xmlns:xsi", attr3.getNodeName());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr3.getNodeValue());
 assertNull(attr4.getNodeValue());
 assertNull(attr5.getNodeValue());
 }

 @Test
 public void testNextNullName() {
 iterator = new DOMAttributeIterator(parent, new QName(null, null, null));
 assertNull(iterator.next());
 }
}