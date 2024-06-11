package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        assertEquals(iterator.getPosition(), 0);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getNodeType(), Node.ATTRIBUTE_NODE);
        assertEquals(iterator.next().getNodeName().getNodeValue(), "test");
        assertEquals(iterator.next().getNodeName().getLocalPart(), "test");
        assertEquals(iterator.next().getNodeName().getPrefix(), "test");
        assertEquals(iterator.next().getNodeName().getNamespaceURI(), "test");
        assertEquals(iterator.next().getNodeValue(), "testValue");
        assertEquals(iterator.hasNext(), false);
        assertEquals(iterator.next(), null);
    }

    @Test
    public void testDOMAttributeIteratorWithWildcard() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        assertEquals(iterator.getPosition(), 0);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getNodeType(), Node.ATTRIBUTE_NODE);
        assertEquals(iterator.next().getNodeName().getNodeValue(), "test1");
        assertEquals(iterator.next().getNodeName().getLocalPart(), "test1");
        assertEquals(iterator.next().getNodeName().getPrefix(), null);
        assertEquals(iterator.next().getNodeName().getNamespaceURI(), null);
        assertEquals(iterator.next().getNodeValue(), "testValue1");
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getNodeType(), Node.ATTRIBUTE_NODE);
        assertEquals(iterator.next().getNodeName().getNodeValue(), "test2");
        assertEquals(iterator.next().getNodeName().getLocalPart(), "test2");
        assertEquals(iterator.next().getNodeName().getPrefix(), null);
        assertEquals(iterator.next().getNodeName().getNamespaceURI(), null);
        assertEquals(iterator.next().getNodeValue(), "testValue2");
        assertEquals(iterator.hasNext(), false);
        assertEquals(iterator.next(), null);
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("testUri", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        assertEquals(iterator.getPosition(), 0);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getNodeType(), Node.ATTRIBUTE_NODE);
        assertEquals(iterator.next().getNodeName().getNodeValue(), "test:test");
        assertEquals(iterator.next().getNodeName().getLocalPart(), "test");
        assertEquals(iterator.next().getNodeName().getPrefix(), "testUri");
        assertEquals(iterator.next().getNodeName().getNamespaceURI(), "testUri");
        assertEquals(iterator.next().getNodeValue(), "testValue");
        assertEquals(iterator.hasNext(), false);
        assertEquals(iterator.next(), null);
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("testUri", "test:test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        assertEquals(iterator.getPosition(), 0);
        assertEquals(iterator.hasNext(), true);
        assertEquals(iterator.next().getNodeType(), Node.ATTRIBUTE_NODE);
        assertEquals(iterator.next().getNodeName().getNodeValue(), "test:test");
        assertEquals(iterator.next().getNodeName().getLocalPart(), "test");
        assertEquals(iterator.next().getNodeName().getPrefix(), "test");
        assertEquals(iterator.next().getNodeName().getNamespaceURI(), "testUri");
        assertEquals(iterator.next().getNodeValue(), "testValue");
        assertEquals(iterator.hasNext(), false);
        assertEquals(iterator.next(), null);
    }

    @Test
    public void testDOMAttributeIteratorNextNull() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        when(iterator.next()).thenReturn(null);
        List<Node> nodes = new ArrayList<>();
        while (iterator.hasNext()) {
            nodes.add(iterator.next());
        }
        assertEquals(nodes.size(), 0);
    }

    @Test
    public void testDOMAttributeIteratorNoMatch() {
        Element element = mock(Element.class);
        when(element.getAttributes()).thenReturn(createMockNodeList());
        DOMNodePointer domNodePointer = new DOMNodePointer(null, element);
        QName qName = new QName("test", "test2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(domNodePointer, qName);
        assertEquals(iterator.getPosition(), 0);
        assertEquals(iterator.hasNext(), false);
        assertEquals(iterator.next(), null);
    }

    private NodeList createMockNodeList() {
        Node node = mock(Node.class);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        NodeList nodeList = mock(NodeList.class);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);
        return nodeList;
    }
}