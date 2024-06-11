package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

 private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
 private static final String PREFIX = "xml";
 private static final String URI = XML_NAMESPACE;
 private static final String LNAME = "attr";
 private static final String NS_NAME = "xmlns";
 private static final String NC_NAME = "name";
 private static final String ATTR_VALUE = "value";
 private static final String ELEMENT_NAME = "element";
 private static final String ATTRIBUTE_NAME = "attribute";
 private static final String ATTRIBUTE_VALUE = "attributeValue";
 private static final String ANOTHER_ATTRIBUTE_NAME = "anotherAttributeName";
 private static final String ANOTHER_ATTRIBUTE_VALUE = "anotherAttributeValue";

 private Element createTestElement() {
 return new Element(ELEMENT_NAME);
 }

 @Test
 public void testConstructorWhenParentIsNull() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName(NS_NAME, NC_NAME));
 assertNull(iterator.parent);
 assertNull(iterator.attributes);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWhenParentIsNotElement() {
 NodePointer parentPointer = new NodePointer(new Object());
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(NS_NAME, NC_NAME));
 assertNull(iterator.parent);
 assertNull(iterator.attributes);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWhenNameIsWildcard() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, "*"));
 assertSame(parent, iterator.parent);
 assertEquals(2, iterator.size());
 }

 @Test
 public void testConstructorWhenNameIsSpecific() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 assertSame(parent, iterator.parent);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWhenNamespaceIsDefault() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(NC_NAME));
 assertSame(parent, iterator.parent);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWhenNamespaceIsSpecific() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 assertSame(parent, iterator.parent);
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWhenNoAttributes() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 assertSame(parent, iterator.parent);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWhenAttributesArePresent() {
 Element element = createTestElement();
 element.setAttribute(new QName(URI, LNAME), new Attribute(LNAME, ATTR_VALUE));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(URI, LNAME));
 assertSame(element, iterator.parent.getNode());
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWhenAttributesArePresentWithNamespace() {
 Element element = createTestElement();
 element.setAttribute(new QName(URI, LNAME), new Attribute(LNAME, ATTR_VALUE));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(NS_NAME, LNAME));
 assertSame(element, iterator.parent.getNode());
 assertEquals(1, iterator.size());
 }

 @Test
 public void testConstructorWhenAttributesArePresentWithMultipleNames() {
 Element element = createTestElement();
 element.setAttribute(new QName(URI, LNAME), new Attribute(LNAME, ATTR_VALUE));
 element.setAttribute(new QName(URI, ANOTHER_ATTRIBUTE_NAME), new Attribute(ANOTHER_ATTRIBUTE_NAME, ANOTHER_ATTRIBUTE_VALUE));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(URI, "*"));
 assertSame(element, iterator.parent.getNode());
 assertEquals(2, iterator.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWhenParentIsElementButNameIsInvalid() {
 Element element = createTestElement();
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
 assertSame(element, iterator.parent.getNode());
 assertEquals(0, iterator.size());
 }

 @Test
 public void testGetNodePointerWhenIteratorIsInitialized() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 NodePointer nodePointer = iterator.next();
 assertSame(parent, nodePointer.getParent());
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNodePointerWhenIteratorIsNotInitialized() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 assertNull(iterator.next());
 }

 @Test
 public void testGetNodePointerWhenPositionIsSetToLastElement() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.next();
 assertSame(parent, nodePointer.getParent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNodePointerWhenPositionIsGreaterThanSize() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 @Test
 public void testSetPositionWhenPositionIsValid() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 iterator.next();
 iterator.setPosition(1);
 NodePointer nodePointer = iterator.next();
 assertSame(parent, nodePointer.getParent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWhenPositionIsInvalid() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 iterator.next();
 iterator.setPosition(3);
 }

 @Test
 public void testHasNextWhenIteratorIsNotInitialized() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testHasNextWhenIteratorIsInitializedWithOneElement() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, LNAME));
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextWhenIteratorIsInitializedWithMultipleElements() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(URI, "*"));
 iterator.next();
 iterator.next();
 assertTrue(iterator.hasNext());
 }
}