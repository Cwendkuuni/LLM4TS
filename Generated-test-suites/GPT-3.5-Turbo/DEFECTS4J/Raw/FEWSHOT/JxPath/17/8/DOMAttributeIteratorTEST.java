package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator domAttributeIterator;
    private Node node;
    private Element element;

    @Before
    public void setup() {
        parent = Mockito.mock(NodePointer.class);
        name = Mockito.mock(QName.class);
        domAttributeIterator = new DOMAttributeIterator(parent, name);
        node = Mockito.mock(Node.class);
        element = Mockito.mock(Element.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, domAttributeIterator.parent);
        assertEquals(name, domAttributeIterator.name);
        assertTrue(domAttributeIterator.attributes.isEmpty());
        assertEquals(0, domAttributeIterator.position);
        Mockito.verifyZeroInteractions(parent);
        Mockito.verifyZeroInteractions(name);
    }

    @Test
    public void testTestAttrReturnsFalseWhenNodePrefixIsXmlns() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn("xmlns");
        boolean result = domAttributeIterator.testAttr(attr);
        assertEquals(false, result);
        Mockito.verify(attr).getPrefix();
        Mockito.verifyZeroInteractions(parent);
    }

    @Test
    public void testTestAttrReturnsFalseWhenNodePrefixIsNullAndNodeLocalNameIsXmlns() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn(null);
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("xmlns");
        boolean result = domAttributeIterator.testAttr(attr);
        assertEquals(false, result);
        Mockito.verify(attr).getPrefix();
        Mockito.verify(attr).getLocalName();
        Mockito.verifyZeroInteractions(parent);
    }

    @Test
    public void testTestAttrReturnsTrueWhenTestLocalNameEqualsNodeLocalNameAndTestPrefixEqualsNodePrefix() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn("testPrefix");
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("testLocalName");
        Mockito.when(name.getPrefix()).thenReturn("testPrefix");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn(null);
        Mockito.when(parent.getNamespaceURI("nodePrefix")).thenReturn(null);
        boolean result = domAttributeIterator.testAttr(attr);
        assertEquals(true, result);
        Mockito.verify(attr).getPrefix();
        Mockito.verify(attr).getLocalName();
        Mockito.verify(name).getName();
        Mockito.verify(name).getPrefix();
        Mockito.verify(parent).getNamespaceURI("testPrefix");
        Mockito.verify(parent).getNamespaceURI("nodePrefix");
    }

    @Test
    public void testTestAttrReturnsTrueWhenTestLocalNameEqualsWildcardAndTestPrefixIsNullAndNodePrefixIsNull() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn(null);
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(parent.getNamespaceURI("testPrefix")).thenReturn(null);
        Mockito.when(parent.getNamespaceURI(null)).thenReturn(null);
        boolean result = domAttributeIterator.testAttr(attr);
        assertEquals(true, result);
        Mockito.verify(attr).getPrefix();
        Mockito.verify(attr).getLocalName();
        Mockito.verify(name).getName();
        Mockito.verify(name).getPrefix();
        Mockito.verify(parent).getNamespaceURI("testPrefix");
        Mockito.verify(parent).getNamespaceURI(null);
    }

    @Test
    public void testGetAttributeReturnsAttrWhenTestLocalNameNotWildcard() {
        QName attributeQName = Mockito.mock(QName.class);
        Mockito.when(name.getName()).thenReturn("attributeName");
        Mockito.when(getAttribute(element, attributeQName)).thenReturn(Mockito.mock(Attr.class));
        Attr result = domAttributeIterator.getAttribute(element, attributeQName);
        assertNull(result);
        Mockito.verify(element).getAttributeNode("attributeName");
        Mockito.verifyZeroInteractions(parent);
    }

    @Test
    public void testGetPosition() {
        domAttributeIterator.position = 5;
        int result = domAttributeIterator.getPosition();
        assertEquals(5, result);
    }

    @Test
    public void testSetPositionReturnsTrueWhenPositionIsBetween1AndSize() {
        List<Attr> attributes = new ArrayList<>();
        domAttributeIterator.attributes = attributes;
        attributes.add(Mockito.mock(Attr.class));
        attributes.add(Mockito.mock(Attr.class));
        attributes.add(Mockito.mock(Attr.class));
        boolean result = domAttributeIterator.setPosition(2);
        assertEquals(true, result);
    }

    @Test
    public void testSetPositionReturnsFalseWhenPositionIsZero() {
        List<Attr> attributes = new ArrayList<>();
        domAttributeIterator.attributes = attributes;
        boolean result = domAttributeIterator.setPosition(0);
        assertEquals(false, result);
    }

    @Test
    public void testSetPositionReturnsFalseWhenPositionIsGreaterThanSize() {
        List<Attr> attributes = new ArrayList<>();
        domAttributeIterator.attributes = attributes;
        attributes.add(Mockito.mock(Attr.class));
        attributes.add(Mockito.mock(Attr.class));
        attributes.add(Mockito.mock(Attr.class));
        boolean result = domAttributeIterator.setPosition(5);
        assertEquals(false, result);
    }

    @Test
    public void testGetNodePointerWhenPositionIsZero() {
        domAttributeIterator.position = 0;
        Mockito.when(parent.getNode()).thenReturn(Mockito.mock(Node.class));
        NodePointer result = domAttributeIterator.getNodePointer();
        assertNull(result);
    }

    @Test
    public void testGetNodePointerWhenNodeIsElementNode() {
        domAttributeIterator.position = 1;
        domAttributeIterator.attributes.add(Mockito.mock(Attr.class));
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(element.getAttributeNode("attributeName")).thenReturn(Mockito.mock(Attr.class));
        NodePointer result = domAttributeIterator.getNodePointer();
        assertNull(result);
        Mockito.verify(element).getAttributeNode("attributeName");
    }

    @Test
    public void testGetNodePointerWhenPositionIsGreaterThanSize() {
        domAttributeIterator.position = 5;
        domAttributeIterator.attributes.add(Mockito.mock(Attr.class));
        domAttributeIterator.attributes.add(Mockito.mock(Attr.class));
        domAttributeIterator.attributes.add(Mockito.mock(Attr.class));
        NodePointer result = domAttributeIterator.getNodePointer();
        assertNull(result);
    }
    
}