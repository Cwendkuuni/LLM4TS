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
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/**
 * Tests for DOMAttributeIterator class
 */
public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    private Element mockElement;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("test", "testNS");
        mockElement = mock(Element.class);
    }

    @Test
    public void testConstructor() {
        iterator = new DOMAttributeIterator(parent, name);
        assertSame(parent, iterator.getParent());
        assertSame(name, iterator.getName());
    }

    @Test
    public void testGetNodePointer() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(mock(Element.class).getAttributeNode(name.getName())).thenReturn(mock(Attr.class));
        iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(mock(Attr.class), nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_NoMatchingAttribute() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(mock(Element.class).getAttributeNode(name.getName())).thenReturn(null);
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer_MultipleAttributes() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(mock(Element.class).getAttributes()).thenReturn(nodeMap);
        Attr firstAttribute = mock(Attr.class);
        Attr secondAttribute = mock(Attr.class);
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(firstAttribute);
        when(nodeMap.item(1)).thenReturn(secondAttribute);
        when(firstAttribute.getNodeName()).thenReturn(name.getName());
        when(firstAttribute.getNodeValue()).thenReturn("firstValue");
        when(secondAttribute.getNodeName()).thenReturn(name.getName());
        when(secondAttribute.getNodeValue()).thenReturn("secondValue");
        when(testAttr(firstAttribute, name)).thenReturn(true);
        when(testAttr(secondAttribute, name)).thenReturn(false);

        iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(firstAttribute, nodePointer.getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_OutOfBounds() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(mock(Element.class).getAttributes()).thenReturn(nodeMap);
        Attr firstAttribute = mock(Attr.class);
        Attr secondAttribute = mock(Attr.class);
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(firstAttribute);
        when(nodeMap.item(1)).thenReturn(secondAttribute);
        when(firstAttribute.getNodeName()).thenReturn(name.getName());
        when(firstAttribute.getNodeValue()).thenReturn("firstValue");
        when(secondAttribute.getNodeName()).thenReturn(name.getName());
        when(secondAttribute.getNodeValue()).thenReturn("secondValue");
        when(testAttr(firstAttribute, name)).thenReturn(true);
        when(testAttr(secondAttribute, name)).thenReturn(false);

        iterator = new DOMAttributeIterator(parent, name);
        iterator.next(); // move to next attribute
        iterator.next(); // move to next attribute again
        iterator.getNodePointer(); // this should throw IndexOutOfBoundsException
    }

    @Test
    public void testGetPosition() {
        iterator = new DOMAttributeIterator(parent, name);
        assertSame(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_NextAttribute() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(mock(Element.class).getAttributes()).thenReturn(nodeMap);
        Attr firstAttribute = mock(Attr.class);
        Attr secondAttribute = mock(Attr.class);
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(firstAttribute);
        when(nodeMap.item(1)).thenReturn(secondAttribute);
        when(firstAttribute.getNodeName()).thenReturn(name.getName());
        when(firstAttribute.getNodeValue()).thenReturn("firstValue");
        when(secondAttribute.getNodeName()).thenReturn(name.getName());
        when(secondAttribute.getNodeValue()).thenReturn("secondValue");
        when(testAttr(firstAttribute, name)).thenReturn(true);
        when(testAttr(secondAttribute, name)).thenReturn(false);

        iterator = new DOMAttributeIterator(parent, name);
        iterator.next(); // move to next attribute
        boolean isPositionSet = iterator.setPosition(1);
        assertTrue(isPositionSet);
        assertSame(1, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_OutOfBounds() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(mock(Element.class).getAttributes()).thenReturn(nodeMap);
        Attr firstAttribute = mock(Attr.class);
        Attr secondAttribute = mock(Attr.class);
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(firstAttribute);
        when(nodeMap.item(1)).thenReturn(secondAttribute);
        when(firstAttribute.getNodeName()).thenReturn(name.getName());
        when(firstAttribute.getNodeValue()).thenReturn("firstValue");
        when(secondAttribute.getNodeName()).thenReturn(name.getName());
        when(secondAttribute.getNodeValue()).thenReturn("secondValue");
        when(testAttr(firstAttribute, name)).thenReturn(true);
        when(testAttr(secondAttribute, name)).thenReturn(false);

        iterator = new DOMAttributeIterator(parent, name);
        iterator.next(); // move to next attribute
        iterator.next(); // move to next attribute again
        boolean isPositionSet = iterator.setPosition(3); // position out of bounds
        assertFalse(isPositionSet);
    }

    @Test
    public void testSetPosition_SamePosition() {
        when(parent.getNode()).thenReturn(mock(Node.class));
        when(mock(Node.class).getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(mock(Element.class).getAttributes()).thenReturn(nodeMap);
        Attr firstAttribute = mock(Attr.class);
        Attr secondAttribute = mock(Attr.class);
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(firstAttribute);
        when(nodeMap.item(1)).thenReturn(secondAttribute);
        when(firstAttribute.getNodeName()).thenReturn(name.getName());
        when(firstAttribute.getNodeValue()).thenReturn("firstValue");
        when(secondAttribute.getNodeName()).thenReturn(name.getName());
        when(secondAttribute.getNodeValue()).thenReturn("secondValue");
        when(testAttr(firstAttribute, name)).thenReturn(true);
        when(testAttr(secondAttribute, name)).thenReturn(false);

        iterator = new DOMAttributeIterator(parent, name);
        boolean isPositionSet = iterator.setPosition(1);
        assertTrue(isPositionSet);
        assertSame(1, iterator.getPosition());
        boolean isPositionSetAgain = iterator.setPosition(1);
        assertTrue(isPositionSetAgain);
        assertSame(1, iterator.getPosition());
    }

    private boolean testAttr(Attr attr, QName testName) {
        DOMAttributeIteratorTest.this.iterator = new DOMAttributeIterator(parent, name);
        return iterator.testAttr(attr, testName);
    }
}