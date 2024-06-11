package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "xml";
 private static final String NS_PREFIX = "ns";
 private static final String NS_URI = "http://example.com/ns";
 private static final String ATTRIBUTE_NAME = "testAttribute";
 private static final String ATTRIBUTE_VALUE = "testValue";

 @Test
 public void testEmptyAttributeIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName(XML_NS, "*"));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element root = new Element("root");
 Element child = new Element("child");
 child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME), ATTRIBUTE_VALUE);
 root.addContent(child);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName(NS_PREFIX, ATTRIBUTE_NAME));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(1, attributes.size());
 assertSame(child.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME)), attributes.get(0));
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element root = new Element("root");
 Element child = new Element("child");
 Attribute attr1 = child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_1"), ATTRIBUTE_VALUE);
 Attribute attr2 = child.setAttribute(new QName(NS_URI, ATTRIBUTE_NAME + "_2"), ATTRIBUTE_VALUE);
 root.addContent(child);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName(NS_PREFIX, "*"));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(2, attributes.size());
 assertTrue(iterator.hasNext());
 assertSame(attr1, iterator.next());
 assertSame(attr2, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNamespaceAwareAttributeIterator() {
 Element root = new Element("root");
 Element child = new Element("child");
 child.setAttribute(new QName(NS_URI, ATTRIBUTE_NAME), ATTRIBUTE_VALUE);
 root.addContent(child);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName(NS_URI, ATTRIBUTE_NAME));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(1, attributes.size());
 assertSame(child.getAttribute(new QName(NS_URI, ATTRIBUTE_NAME)), attributes.get(0));
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorPosition() {
 Element root = new Element("root");
 Element child = new Element("child");
 child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_1"), ATTRIBUTE_VALUE);
 child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_2"), ATTRIBUTE_VALUE);
 root.addContent(child);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName(NS_PREFIX, "*"));
 List<Attribute> attributes = iterator.getAttributes();
 assertSame(child.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_1")), iterator.next());
 assertSame(child.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_2")), iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPosition() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), new QName(XML_NS, "*"));
 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 Element root = new Element("root");
 Element child = new Element("child");
 child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_1"), ATTRIBUTE_VALUE);
 child.setAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME + "_2"), ATTRIBUTE_VALUE);
 root.addContent(child);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName(NS_PREFIX, "*"));
 iterator.setPosition(3);
 }
}