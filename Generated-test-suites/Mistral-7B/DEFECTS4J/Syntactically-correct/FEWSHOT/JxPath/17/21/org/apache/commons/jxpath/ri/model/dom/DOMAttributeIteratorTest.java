package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.mockito.MockitoAnnotations;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getNamespaceURI("xmlns")).thenReturn("http://example.com/xmlns");
        when(element.getNodeName()).thenReturn("elementName");
        when(element.getTagName()).thenReturn("elementName");
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());

        DOMAttributeIterator iteratorWithAttr = new DOMAttributeIterator(parentMock, new QName("test"));
        assertNull(iteratorWithAttr.getNodePointer());
        assertEquals(0, iteratorWithAttr.getPosition());
        assertFalse(iteratorWithAttr.hasNext());

        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(nodeMap);

        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        Node attr1Node = mock(Node.class);
        Node attr2Node = mock(Node.class);
        when(attr1.getNodeName()).thenReturn("testAttr1");
        when(attr1.getNodeValue()).thenReturn("testVal1");
        when(attr1.getPrefix()).thenReturn("prefix1");
        when(attr1.getNamespaceURI()).thenReturn("http://example.com/namespace1");
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(attr1);
        when(nodeMap.item(1)).thenReturn(attr2);
        when(attr2.getNodeName()).thenReturn("testAttr2");
        when(attr2.getNodeValue()).thenReturn("testVal2");
        when(attr2.getPrefix()).thenReturn("prefix2");
        when(attr2.getNamespaceURI()).thenReturn("http://example.com/namespace2");

        when(parentMock.getNodePointer(attr1)).thenReturn(new DOMAttributePointer(parentMock, attr1));
        when(parentMock.getNodePointer(attr2)).thenReturn(new DOMAttributePointer(parentMock, attr2));

        iterator = new DOMAttributeIterator(parentMock, new QName("test"));
        assertEquals(attr1, iterator.next());
        assertEquals("testAttr1", iterator.getNodePointer().getNodeName());
        assertEquals("testVal1", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix1", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace1", iterator.getNodePointer().getNamespaceURI());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertEquals(attr2, iterator.next());
        assertEquals("testAttr2", iterator.getNodePointer().getNodeName());
        assertEquals("testVal2", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix2", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace2", iterator.getNodePointer().getNamespaceURI());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithWildcardName() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, new QName("*"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());

        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(nodeMap);

        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        Node attr1Node = mock(Node.class);
        Node attr2Node = mock(Node.class);
        when(attr1.getNodeName()).thenReturn("testAttr1");
        when(attr1.getNodeValue()).thenReturn("testVal1");
        when(attr1.getPrefix()).thenReturn("prefix1");
        when(attr1.getNamespaceURI()).thenReturn("http://example.com/namespace1");
        when(nodeMap.getLength()).thenReturn(2);
        when(nodeMap.item(0)).thenReturn(attr1);
        when(nodeMap.item(1)).thenReturn(attr2);
        when(attr2.getNodeName()).thenReturn("testAttr2");
        when(attr2.getNodeValue()).thenReturn("testVal2");
        when(attr2.getPrefix()).thenReturn("prefix2");
        when(attr2.getNamespaceURI()).thenReturn("http://example.com/namespace2");

        when(parentMock.getNodePointer(attr1)).thenReturn(new DOMAttributePointer(parentMock, attr1));
        when(parentMock.getNodePointer(attr2)).thenReturn(new DOMAttributePointer(parentMock, attr2));

        iterator = new DOMAttributeIterator(parentMock, new QName("*"));
        assertEquals(attr1, iterator.next());
        assertEquals("testAttr1", iterator.getNodePointer().getNodeName());
        assertEquals("testVal1", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix1", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace1", iterator.getNodePointer().getNamespaceURI());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertEquals(attr2, iterator.next());
        assertEquals("testAttr2", iterator.getNodePointer().getNodeName());
        assertEquals("testVal2", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix2", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace2", iterator.getNodePointer().getNamespaceURI());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        String namespaceURI = "http://example.com";
        QName attrName = new QName("test", namespaceURI);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, attrName);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());

        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(nodeMap);

        Attr attr = mock(Attr.class);
        Node attrNode = mock(Node.class);
        when(attr.getNodeName()).thenReturn(attrName.getName());
        when(attr.getNodeValue()).thenReturn("testVal");
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(attr.getNamespaceURI()).thenReturn(namespaceURI);
        when(nodeMap.getLength()).thenReturn(1);
        when(nodeMap.item(0)).thenReturn(attr);

        when(parentMock.getNodePointer(attr)).thenReturn(new DOMAttributePointer(parentMock, attr));

        iterator = new DOMAttributeIterator(parentMock, attrName);
        assertEquals(attr, iterator.next());
        assertEquals("test", iterator.getNodePointer().getPrefix());
        assertEquals(namespaceURI, iterator.getNodePointer().getNamespaceURI());
        assertEquals("test", iterator.getNodePointer().getNodeName());
        assertEquals("testVal", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNoMatchingAttribute() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        String namespaceURI = "http://example.com";
        QName attrName = new QName("test", namespaceURI);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, attrName);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());

        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(nodeMap);

        iterator = new DOMAttributeIterator(parentMock, attrName);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyList() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, new QName("test"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());

        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(nodeMap);
        when(nodeMap.getLength()).thenReturn(0);

        iterator = new DOMAttributeIterator(parentMock, new QName("test"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleAttributes() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getParentNode()).thenReturn(element);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodePointer parentMock = new NodePointer(node, null);

        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        when(attr1.getNodeName()).thenReturn("testAttr1");
        when(attr1.getNodeValue()).thenReturn("testVal1");
        when(attr1.getPrefix()).thenReturn("prefix1");
        when(attr1.getNamespaceURI()).thenReturn("http://example.com/namespace1");
        when(attr2.getNodeName()).thenReturn("testAttr2");
        when(attr2.getNodeValue()).thenReturn("testVal2");
        when(attr2.getPrefix()).thenReturn("prefix2");
        when(attr2.getNamespaceURI()).thenReturn("http://example.com/namespace2");
        when(attributes.size()).thenReturn(2);
        when(nodeMap.getLength()).thenReturn(attributes.size());
        when(nodeMap.item(0)).thenReturn(attr1);
        when(nodeMap.item(1)).thenReturn(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, new QName("test"));
        assertEquals(attr1, iterator.next());
        assertEquals("testAttr1", iterator.getNodePointer().getNodeName());
        assertEquals("testVal1", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix1", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace1", iterator.getNodePointer().getNamespaceURI());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertEquals(attr2, iterator.next());
        assertEquals("testAttr2", iterator.getNodePointer().getNodeName());
        assertEquals("testVal2", iterator.getNodePointer().getNodeValue());
        assertEquals("prefix2", iterator.getNodePointer().getPrefix());
        assertEquals("http://example.com/namespace2", iterator.getNodePointer().getNamespaceURI());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}