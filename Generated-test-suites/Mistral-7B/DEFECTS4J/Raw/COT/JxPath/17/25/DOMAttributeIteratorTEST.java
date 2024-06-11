package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private NodePointer parentMock;
 private QName nameMock;
 private DOMAttributeIterator iterator;

 @org.junit.Before
 public void setUp() {
 parentMock = mock(DOMNodePointer.class);
 nameMock = new QName("test", "test");
 }

 @org.junit.Test
 public void testConstructor_EmptyNode() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertNull(iterator.getName());
 assertNull(iterator.getAttributes());
 assertNull(iterator.getNodePointer());
 assertNull(iterator.getPosition());
 }

 @org.junit.Test
 public void testConstructor_EmptyElement() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 assertNotNull(iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testConstructor_SingleAttribute() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 Attr attributeMock = mock(Attr.class);
 when(elementMock.getAttributeNode(nameMock.getName())).thenReturn(attributeMock);
 when(attributeMock.getNamespaceURI()).thenReturn("http://test.com");
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 List<Attr> attributes = (List<Attr>) iterator.getAttributes();
 assertSame(attributeMock, attributes.get(0));
 assertSame(iterator, iterator.next());
 }

 @org.junit.Test
 public void testConstructor_MultipleAttributes() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 NodeList nodeListMock = mock(NodeList.class);
 when(nodeListMock.getLength()).thenReturn(2);
 when(nodeListMock.item(0)).thenReturn(mock(Attr.class).get());
 when(nodeListMock.item(1)).thenReturn(mock(Attr.class).get());
 when(elementMock.getAttributes()).thenReturn(nodeListMock);
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 List<Attr> attributes = (List<Attr>) iterator.getAttributes();
 assertFalse(iterator.hasNext());
 assertSame(nodeListMock.item(0), attributes.get(0));
 assertSame(nodeListMock.item(1), attributes.get(1));
 }

 @org.junit.Test
 public void testConstructor_NoMatchingAttribute() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 assertNotNull(iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testConstructor_NamespaceMatch() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 Attr attributeMock = mock(Attr.class);
 when(attributeMock.getNamespaceURI()).thenReturn("http://test.com");
 when(elementMock.getAttributeNodeNS(nameMock.getNamespaceURI(), nameMock.getName())).thenReturn(attributeMock);
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 List<Attr> attributes = (List<Attr>) iterator.getAttributes();
 assertSame(attributeMock, attributes.get(0));
 assertSame(iterator, iterator.next());
 }

 @org.junit.Test
 public void testConstructor_NamespaceNoMatch() {
 Element elementMock = mock(Element.class);
 when(parentMock.getNode()).thenReturn(elementMock);
 Attr attributeMock = mock(Attr.class);
 when(attributeMock.getNamespaceURI()).thenReturn("http://test2.com");
 when(elementMock.getAttributeNodeNS(nameMock.getNamespaceURI(), nameMock.getName())).thenReturn(null);
 when(elementMock.getAttributeNode(nameMock.getName())).thenReturn(attributeMock);
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertSame(parentMock, iterator.getParent());
 assertSame(nameMock, iterator.getName());
 List<Attr> attributes = (List<Attr>) iterator.getAttributes();
 assertSame(attributeMock, attributes.get(0));
 assertSame(iterator, iterator.next());
 }

 @org.junit.Test
 public void testGetNodePointer_InvalidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testGetNodePointer_ValidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 assertSame(parentMock, nodePointer.getParent());
 assertSame(nameMock, nodePointer.getNodeName());
 assertSame(attributes.get(0), nodePointer.getNode());
 }

 @org.junit.Test
 public void testGetPosition_InvalidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertNull(iterator.getNodePointer());
 assertFalse(iterator.hasNext());
 assertNull(iterator.getNodePointer());
 assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testGetPosition_ValidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 iterator.next();
 iterator.next();
 int position = iterator.getPosition();
 assertSame(2, position);
 }

 @org.junit.Test
 public void testHasNext_EmptyIterator() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testHasNext_SingleAttribute() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 assertSame(iterator.getNodePointer().getNode(), iterator.next().getNodePointer().getNode());
 assertSame(iterator.getNodePointer().getNode(), iterator.next().getNodePointer().getNode());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testHasNext_MultipleAttributes() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testNext_EmptyIterator() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertNull(iterator.next().getNodePointer());
 }

 @org.junit.Test
 public void testNext_SingleAttribute() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 NodePointer nextNodePointer = iterator.next();
 assertSame(parentMock, nextNodePointer.getParent());
 assertSame(nameMock, nextNodePointer.getNodeName());
 assertSame(attributes.get(0), nextNodePointer.getNode());
 }

 @org.junit.Test
 public void testNext_MultipleAttributes() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 NodePointer nextNodePointer1 = iterator.next();
 NodePointer nextNodePointer2 = iterator.next();
 assertSame(parentMock, nextNodePointer1.getParent());
 assertSame(nameMock, nextNodePointer1.getNodeName());
 assertSame(attributes.get(0), nextNodePointer1.getNode());
 assertSame(parentMock, nextNodePointer2.getParent());
 assertSame(nameMock, nextNodePointer2.getNodeName());
 assertSame(attributes.get(1), nextNodePointer2.getNode());
 }

 @org.junit.Test
 public void testSetPosition_InvalidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertFalse(iterator.setPosition(3));
 }

 @org.junit.Test
 public void testSetPosition_ValidPosition() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 List<Attr> attributes = new ArrayList<>();
 attributes.add(mock(Attr.class).get());
 attributes.add(mock(Attr.class).get());
 iterator.setAttributes(attributes);
 assertTrue(iterator.setPosition(1));
 }

 @org.junit.Test
 public void testToString() {
 iterator = new DOMAttributeIterator(parentMock, nameMock);
 assertNotNull(iterator.toString());
 }
}