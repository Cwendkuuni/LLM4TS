package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private Element mockElement;
    private NodeMock mockParent;
    private DOMAttributeIterator iterator;

    @Before
    public void setup() {
        mockParent = new NodeMock();
        mockElement = mock(Element.class);
        when(mockElement.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(new AttrMock());
        when(mockElement.getAttributeNode(Mockito.anyString())).thenReturn(new AttrMock());
        when(mockParent.getNode()).thenReturn(mockElement);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, null);
        assertTrue(iterator.isEmpty());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttributeIterator() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr = new AttrMock();
        attributes.add(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("test", nodePointer.getNodeName().getLocalPart());
        assertEquals("test", nodePointer.getNodeName().getPrefix());
        assertEquals("test", nodePointer.getNodeName().getNamespaceURI());
        assertEquals(attr, nodePointer.getNode());
        assertTrue(iterator.hasNext());
        assertNull(iterator.getNodePointer());
        iterator.next();
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testMultipleAttributesIterator() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals("test1", nodePointer1.getNodeName().getLocalPart());
        assertEquals("test1", nodePointer1.getNodeName().getPrefix());
        assertEquals("test", nodePointer1.getNodeName().getNamespaceURI());
        assertEquals(attr1, nodePointer1.getNode());
        assertTrue(iterator.hasNext());

        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals("test2", nodePointer2.getNodeName().getLocalPart());
        assertEquals("test2", nodePointer2.getNodeName().getPrefix());
        assertEquals("test", nodePointer2.getNodeName().getNamespaceURI());
        assertEquals(attr2, nodePointer2.getNode());
        assertTrue(iterator.hasNext());
        assertNull(iterator.getNodePointer());
        iterator.next();
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testNextAttributeWithEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, null);
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testNextAttribute() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals("test1", nodePointer1.getNodeName().getLocalPart());
        assertEquals("test1", nodePointer1.getNodeName().getPrefix());
        assertEquals("test", nodePointer1.getNodeName().getNamespaceURI());
        assertEquals(attr1, nodePointer1.getNode());
        assertTrue(iterator.hasNext());

        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals("test2", nodePointer2.getNodeName().getLocalPart());
        assertEquals("test2", nodePointer2.getNodeName().getPrefix());
        assertEquals("test", nodePointer2.getNodeName().getNamespaceURI());
        assertEquals(attr2, nodePointer2.getNode());
        assertTrue(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testGetPosition() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        int position = iterator.getPosition();
        assertEquals(0, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(1, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(2, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(3, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(4, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(5, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(6, position);

        iterator.next();
        position = iterator.getPosition();
        assertEquals(7, position);

        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testSetPosition() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("test2", nodePointer.getNodeName().getLocalPart());
        assertEquals("test2", nodePointer.getNodeName().getPrefix());
        assertEquals("test", nodePointer.getNodeName().getNamespaceURI());
        assertEquals(attr2, nodePointer.getNode());

        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        assertEquals("test1", nodePointer.getNodeName().getLocalPart());
        assertEquals("test1", nodePointer.getNodeName().getPrefix());
        assertEquals("test", nodePointer.getNodeName().getNamespaceURI());
        assertEquals(attr1, nodePointer.getNode());
    }

    @Test
    public void testSetPositionOutOfRange() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        iterator.setPosition(10);
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionNegative() {
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = new AttrMock();
        Attr attr2 = new AttrMock();
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(mockParent, new QName("test", "test"));
        iterator.setAttributes(attributes);

        iterator.setPosition(-1);
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }
}

class NodeMock implements Node {
    @Override
    public String getNodeName() {
        return null;
    }

    @Override
    public String getNodeValue() {
        return null;
    }

    @Override
    public int getNodeType() {
        return Node.ELEMENT_NODE;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }
}

class AttrMock implements Attr {
    @Override
    public String getNodeName() {
        return "test";
    }

    @Override
    public String getNodeValue() {
        return "test";
    }

    @Override
    public int getNodeType() {
        return Attr.ATTRIBUTE_NODE;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getValueNormalized() {
        return null;
    }

    @Override
    public String getSpecifiedValue() {
        return null;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override
    public String getNamespaceURI() {
        return "test";
    }

    @Override
    public String getPrefix() {
        return "test";
    }

    @Override
    public String getLocalName() {
        return "test";
    }
}