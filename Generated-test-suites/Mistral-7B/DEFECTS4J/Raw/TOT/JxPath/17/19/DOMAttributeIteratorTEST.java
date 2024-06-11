package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com'><child xmlns:y='http://test.com' attr1='value1' attr2='value2'/></root>";
 Document doc = JaxpFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
 nodePointer = new DOMPointNode(doc.getDocumentElement());
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("root", "attr1"));
 assertNotNull(iterator.next());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("root", "*"));
 NodePointer attributePointer = iterator.next();
 assertNotNull(attributePointer.getNode());
 assertNotNull(iterator.next());
 }

 @Test
 public void testIteratorWithNamespace() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("x", "xmlns"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithInvalidNamespace() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "xmlns:z"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAndLocalName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("x", "xmlns:x"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithAttributeNameAndValue() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("root", "attr1"));
 NodePointer attributePointer = iterator.next();
 assertEquals("value1", attributePointer.getNode().getNodeValue());
 }

 @Test
 public void testIteratorWithAttributeNameAndDifferentValue() {
 iterator = new DOMAttributeIterator(nodePointer, new QName("root", "attr1"));
 NodePointer firstAttributePointer = iterator.next();
 NodePointer secondAttributePointer = iterator.next();
 assertNotSame(firstAttributePointer.getNode(), secondAttributePointer.getNode());
 }

 @Test
 public void testIteratorWithNullNodePointer() {
 iterator = new DOMAttributeIterator(null, new QName("root", "attr1"));
 assertNull(iterator);
 }

 @Test
 public void testIteratorWithNullQName() {
 iterator = new DOMAttributeIterator(nodePointer, null);
 assertNull(iterator);
 }

 @Test
 public void testIteratorWithEmptyQName() {
 iterator = new DOMAttributeIterator(nodePointer, new QName(""));
 assertFalse(iterator.hasNext());
 }
}