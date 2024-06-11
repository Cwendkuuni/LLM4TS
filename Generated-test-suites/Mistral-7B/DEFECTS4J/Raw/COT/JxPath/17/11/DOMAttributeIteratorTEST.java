package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jmock.lib.legacy.ClassImposterizer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentMock;
 private QName nameMock;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 ClassImposterizer imposterizer = new ClassImposterizer();
 parentMock = (NodePointer) imposterizer.impose(NodePointer.class);
 nameMock = new QName("test", "test");
 }

 @Test
 public void testConstructorNullParent() {
 assertNull(new DOMAttributeIterator(null, nameMock));
 }

 @Test
 public void testConstructorNullName() {
 assertNull(new DOMAttributeIterator(parentMock, null));
 }

 @Test
 public void testConstructorValidArguments() {
 Element elementMock = mock(Element.class);
 when(elementMock.getAttributeNodeNS(anyString(), anyString())).thenReturn(new Attr("test", "test"));
 when(elementMock.getAttributeNode("test")).thenReturn(new Attr("test2", "test2"));

 Node nodeMock = mock(Node.class);
 when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 when(nodeMock.getNodeName()).thenReturn("testElement");
 when(nodeMock.getAttributes()).thenReturn(createMock(NamedNodeMap.class));
 when(parentMock.getNode()).thenReturn(nodeMock);

 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertEquals(1, iterator.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidNodeType() {
 Node nodeMock = mock(Node.class);
 when(nodeMock.getNodeType()).thenReturn(Node.COMMENT_NODE);
 when(parentMock.getNode()).thenReturn(nodeMock);

 try {
 new DOMAttributeIterator(parentMock, nameMock);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetNodePointerAttributeExists() {
 Element elementMock = mock(Element.class);
 Attr attrMock = new Attr("test", "test");
 when(elementMock.getAttributeNodeNS(anyString(), nameMock.getName())).thenReturn(attrMock);
 when(parentMock.getNode()).thenReturn(elementMock);

 NodePointer nodePointer = iterator.getNodePointer();
 assertEquals(attrMock, nodePointer.getNode());
 }

 @Test
 public void testGetNodePointerAttributeDoesNotExist() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);

 NodePointer nodePointer = iterator.getNodePointer();
 assertNull(nodePointer.getNode());
 }

 @Test
 public void testSizeOneAttribute() {
 Element elementMock = mock(Element.class);
 Attr attrMock = new Attr("test", "test");
 when(elementMock.getAttributeNodeNS(anyString(), nameMock.getName())).thenReturn(attrMock);
 when(elementMock.getAttributeNode("test")).thenReturn(new Attr("test2", "test2"));
 when(parentMock.getNode()).thenReturn(elementMock);

 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertEquals(2, iterator.size());
 }

 @Test
 public void testSizeMultipleAttributes() {
 Element elementMock = mock(Element.class);
 Attr attr1Mock = new Attr("test1", "test1");
 Attr attr2Mock = new Attr("test2", "test2");
 when(elementMock.getAttributeNodes()).thenReturn(createMock(NamedNodeMap.class));
 when(parentMock.getNode()).thenReturn(elementMock);

 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertEquals(2, iterator.size());
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNodePointerBeyondEnd() {
 Element elementMock = mock(Element.class);
 Attr attr1Mock = new Attr("test1", "test1");
 Attr attr2Mock = new Attr("test2", "test2");
 when(elementMock.getAttributeNodes()).thenReturn(createMock(NamedNodeMap.class));
 when(parentMock.getNode()).thenReturn(elementMock);

 iterator = new DOMAttributeIterator(parentMock, nameMock);
 iterator.next();
 iterator.next();

 try {
 iterator.getNodePointer();
 fail();
 } catch (NoSuchElementException e) {
 // expected
 }
 }

 @Test
 public void testSetPositionValidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 iterator.next();
 iterator.next();

 assertTrue(iterator.setPosition(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionInvalidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 iterator.next();
 iterator.next();

 try {
 iterator.setPosition(3);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }
}