package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.mockito.MockitoAnnotations;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorEmpty() {
        Element element = mock(Element.class);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(null, null);
        assertNull(attributeIterator.getNodePointer());
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorSingleAttribute() {
        Element element = mock(Element.class);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNode(QName.valueOf("test"))).thenReturn(attribute);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertSame(attribute, nodePointer.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorMultipleAttributes() {
        Element element = mock(Element.class);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(element.getAttributeNode(QName.valueOf("test1"))).thenReturn(attribute1);
        when(element.getAttributeNode(QName.valueOf("test2"))).thenReturn(attribute2);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), null);
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertSame(attribute1, nodePointer1.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertSame(attribute2, nodePointer2.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorNoMatchingAttribute() {
        Element element = mock(Element.class);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = mock(Element.class);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test", "test:namespace"));
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertSame(attribute, nodePointer.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleNamespaces() {
        Element element = mock(Element.class);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        when(element.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute1);
        when(element.getAttributeNodeNS(Mockito.anyString(), "test:namespace")).thenReturn(attribute2);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test", "test:namespace"));
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertSame(attribute1, nodePointer1.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertSame(attribute2, nodePointer2.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorEmptyElement() {
        Element element = mock(Element.class);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), null);
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyAttributeList() {
        Element element = mock(Element.class);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*,*"));
        assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMixedAttributeList() {
        Element element = mock(Element.class);
        Attr attribute1 = mock(Attr.class);
        Attr attribute2 = mock(Attr.class);
        Attr attribute3 = mock(Attr.class);
        when(element.getAttributeNode(Mockito.anyString())).thenReturn(attribute3);
        when(element.getAttributeNodes()).thenReturn(mock(NodeList.class));
        NodeList attributeNodes = mock(NodeList.class);
        when(attributeNodes.item(0)).thenReturn(attribute1);
        when(attributeNodes.item(1)).thenReturn(attribute2);
        when(element.getAttributes()).thenReturn(attributeNodes);
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*", "*"));
        assertTrue(attributeIterator.hasNext());
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertSame(attribute1, nodePointer1.getNode());
        attributeIterator.next();
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertSame(attribute2, nodePointer2.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        NodePointer nodePointer3 = attributeIterator.getNodePointer();
        assertSame(attribute3, nodePointer3.getNode());
        assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        assertFalse(attributeIterator.hasNext());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}