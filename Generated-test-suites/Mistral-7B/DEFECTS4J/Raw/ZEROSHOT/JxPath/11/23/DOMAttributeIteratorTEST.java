package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private Node testNode;
 private Element testElement;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() throws Exception {
 testNode = mock(Node.class);
 testElement = mock(Element.class);
 when(testNode.getElementsByTagName("test"))
 .thenReturn(createMock(NodeList.class));
 when(testElement.getTagName()).thenReturn("testElement");
 when(testElement.getAttributeNode(eq("testAttr")))
 .thenReturn(createMock(Attr.class));
 when(testElement.getAttributeNodeNS(eq("http://test"), eq("testAttr")))
 .thenReturn(createMock(Attr.class));
 when(testElement.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 }

 @Test
 public void testDOMAttributeIteratorEmpty() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), null);
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorSingleAttribute() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testNode, 0), new QName("testAttr"));
 assertNotNull(iterator.getNodePointer());
 assertEquals("testElement", iterator.getNodePointer().getNode().getNodeName());
 assertEquals("testAttr", iterator.getNodePointer().getNode().getNodeName());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorMultipleAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("testAttr"));
 iterator.next();
 assertNotNull(iterator.getNodePointer());
 assertEquals("testElement", iterator.getNodePointer().getNode().getNodeName());
 assertEquals("testAttr", iterator.getNodePointer().getNode().getNodeName());
 assertTrue(iterator.hasNext());
 iterator.next();
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorDifferentNamespaces() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("{http://test}testAttr"));
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorWrongPrefix() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test:testAttr"));
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorPrefixAndLocalName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test", "testAttr"));
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorSamePrefixAndLocalName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test", "testAttr"));
 assertNotNull(iterator.getNodePointer());
 assertEquals("testElement", iterator.getNodePointer().getNode().getNodeName());
 assertEquals("testAttr", iterator.getNodePointer().getNode().getNodeName());
 assertTrue(iterator.hasNext());
 iterator.next();
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorDifferentPrefixes() {
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test1", "testAttr"));
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test2", "testAttr"));
 assertSame(iterator1, iterator2);
 }

 @Test
 public void testDOMAttributeIteratorSamePrefixDifferentLocalName() {
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test1", "testAttr1"));
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("test1", "testAttr2"));
 assertNotSame(iterator1, iterator2);
 }

 @Test
 public void testDOMAttributeIteratorGetPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("testAttr"));
 iterator.next();
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testDOMAttributeIteratorSetPosition() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement, 0), new QName("testAttr"));
 assertFalse(iterator.setPosition(-1));
 assertFalse(iterator.setPosition(3));
 assertTrue(iterator.setPosition(1));
 assertSame(iterator.getNodePointer(), iterator.getNodePointer());
 }
}