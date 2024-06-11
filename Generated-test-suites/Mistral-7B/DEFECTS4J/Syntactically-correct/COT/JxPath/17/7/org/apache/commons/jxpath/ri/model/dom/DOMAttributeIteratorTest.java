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
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private NodePointer parent;
 private Element element;
 private QName name;

 @Before
 public void setup() {
 parent = mock(NodePointer.class);
 element = mock(Element.class);
 name = new QName("", "testAttribute");
 }

 @Test
 public void testConstructorNullParent() {
 assertNull(new DOMAttributeIterator(null, name));
 }

 @Test
 public void testConstructorNullName() {
 assertNull(new DOMAttributeIterator(parent, null));
 }

 @Test
 public void testConstructor() {
 iterator = new DOMAttributeIterator(parent, name);
 }

 @Test
 public void testConstructorWithElementAndWildcardAttribute() {
 when(element.getAttributes()).thenReturn(mockNodeList());
 iterator = new DOMAttributeIterator(parent, name);
 assertSame(iterator.getClass(), DOMAttributeIterator.class);
 }

 @Test
 public void testConstructorWithElementAndSpecificAttribute() {
 Attr attribute = mock(Attr.class);
 when(element.getAttributeNodeNS(Mockito.eq("http://test.com"), Mockito.eq("testAttribute"))).thenReturn(attribute);
 iterator = new DOMAttributeIterator(parent, new QName("http://test.com", "testAttribute"));
 assertSame(iterator.getNodePointer().getNode(), attribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithElementAndInvalidNamespace() {
 iterator = new DOMAttributeIterator(parent, new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithElementAndEmptyName() {
 iterator = new DOMAttributeIterator(parent, new QName("", ""));
 }

 @Test
 public void testConstructorWithElementAndNoAttributes() {
 when(element.getAttributes()).thenReturn(mockNodeList(new Attr[0]));
 iterator = new DOMAttributeIterator(parent, name);
 assertSame(iterator.getClass(), DOMAttributeIterator.class);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testConstructorWithElementAndMultipleAttributes() {
 Attr attr1 = mock(Attr.class);
 Attr attr2 = mock(Attr.class);
 when(element.getAttributes()).thenReturn(mockNodeList(attr1, attr2));
 iterator = new DOMAttributeIterator(parent, name);
 assertSame(iterator.getClass(), DOMAttributeIterator.class);
 List<NodePointer> pointers = new ArrayList<>();
 pointers.add(new DOMAttributePointer(parent, attr1));
 pointers.add(new DOMAttributePointer(parent, attr2));
 assertSame(iterator.getNextNodeIterators().get(0), pointers);
 }

 @Test
 public void testGetPosition() {
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.setPosition(0));
 assertFalse(iterator.setPosition(1));
 assertTrue(iterator.setPosition(1));
 assertTrue(iterator.setPosition(2));
 assertFalse(iterator.setPosition(3));
 assertFalse(iterator.setPosition(-1));
 }

 @Test
 public void testSetPositionWithSameIterator() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 assertTrue(iterator.setPosition(1));
 }

 @Test
 public void testSetPositionWithDifferentIterator() {
 DOMAttributeIterator otherIterator = new DOMAttributeIterator(parent, name);
 otherIterator.setPosition(1);
 assertFalse(iterator.setPosition(1));
 }

 @Test
 public void testGetNodePointer() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(1);
 NodePointer nodePointer = iterator.getNodePointer();
 assertSame(nodePointer.getNode(), iterator.getNextNodeIterators().get(0).get(0).getNode());
 }

 private NodeList mockNodeList() {
 return mock(NodeList.class);
 }

 private NodeList mockNodeList(Attr... attrArray) {
 NodeList nodeList = mock(NodeList.class);
 when(nodeList.getLength()).thenReturn(attrArray.length);
 for (Attr attr : attrArray) {
 when(nodeList.item(Mockito.anyInt())).thenReturn(attr);
 }
 return nodeList;
 }

 private Document mockDocument() {
 Document document = mock(Document.class);
 when(document.createAttributeNS("http://test.com", "testAttribute"))
 .thenReturn(new Attr());
 return document;
 }
}