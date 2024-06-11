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
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    private NodePointer parentMock;
    private QName name;
    private DOMAttributeIterator iterator;
    private Element elementMock;
    private NamedNodeMap mapMock;

    @Before
    public void setup() {
        parentMock = mock(NodePointer.class);
        name = new QName("test", "test");
        elementMock = mock(Element.class);
        mapMock = mock(NamedNodeMap.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getAttributes()).thenReturn(mapMock);
    }

    @Test
    public void testDOMAttributeIteratorCreation() {
        iterator = new DOMAttributeIterator(parentMock, name);
        assertSame(parentMock, iterator.getParent());
        assertSame(name, iterator.getName());
    }

    @Test
    public void testGetNodePointerEmptyList() {
        when(mapMock.getLength()).thenReturn(0);
        iterator = new DOMAttributeIterator(parentMock, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerFirstAttribute() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
        assertSame(parentMock, nodePointer.getParent());
        assertSame(name, nodePointer.getNodeName());
    }

    @Test
    public void testGetNodePointerNextAttribute() {
        when(mapMock.getLength()).thenReturn(2);
        Attr attr1Mock = mock(Attr.class);
        Attr attr2Mock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attr1Mock);
        when(mapMock.item(1)).thenReturn(attr2Mock);
        when(attr1Mock.getNodeName()).thenReturn("test1");
        when(attr2Mock.getNodeName()).thenReturn("test2");
        iterator = new DOMAttributeIterator(parentMock, name);
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertSame(attr1Mock, nodePointer1.getNode());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertSame(attr2Mock, nodePointer2.getNode());
        assertSame(parentMock, nodePointer1.getParent());
        assertSame(parentMock, nodePointer2.getParent());
        assertSame(name, nodePointer1.getNodeName());
        assertSame(name, nodePointer2.getNodeName());
    }

    @Test
    public void testGetNodePointerNullName() {
        iterator = new DOMAttributeIterator(parentMock, null);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerDifferentName() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test1");
        iterator = new DOMAttributeIterator(parentMock, new QName("test2", "test2"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointerParentChange() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
        parentMock = mock(NodePointer.class);
        iterator.setParent(parentMock);
        NodePointer newNodePointer = iterator.getNodePointer();
        assertNull(newNodePointer);
    }

    @Test
    public void testSetPositionFirstPosition() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        assertTrue(iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
        parentMock = mock(NodePointer.class);
        iterator.setParent(parentMock);
        NodePointer newNodePointer = iterator.getNodePointer();
        assertNull(newNodePointer);
    }

    @Test
    public void testSetPositionLastPosition() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        assertTrue(iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
        assertTrue(iterator.setPosition(1));
        NodePointer newNodePointer = iterator.getNodePointer();
        assertNull(newNodePointer);
    }

    @Test
    public void testSetPositionInvalidPosition() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        assertFalse(iterator.setPosition(2));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
    }

    @Test
    public void testSetPositionPositionZero() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attrMock, nodePointer.getNode());
        iterator.setPosition(0);
        NodePointer newNodePointer = iterator.getNodePointer();
        assertSame(attrMock, newNodePointer.getNode());
    }

    @Test
    public void testSetPositionPreviousPosition() {
        when(mapMock.getLength()).thenReturn(2);
        Attr attr1Mock = mock(Attr.class);
        Attr attr2Mock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attr1Mock);
        when(mapMock.item(1)).thenReturn(attr2Mock);
        when(attr1Mock.getNodeName()).thenReturn("test1");
        when(attr2Mock.getNodeName()).thenReturn("test2");
        iterator = new DOMAttributeIterator(parentMock, name);
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertSame(attr1Mock, nodePointer1.getNode());
        iterator.setPosition(1);
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertSame(attr2Mock, nodePointer2.getNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionInvalidIterationDirection() {
        when(mapMock.getLength()).thenReturn(1);
        Attr attrMock = mock(Attr.class);
        when(mapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getNodeName()).thenReturn("test");
        iterator = new DOMAttributeIterator(parentMock, name);
        iterator.setPosition(-1);
    }
}