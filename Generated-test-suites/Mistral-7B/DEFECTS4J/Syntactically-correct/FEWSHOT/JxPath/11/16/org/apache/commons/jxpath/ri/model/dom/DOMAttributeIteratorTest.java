package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_nullParent() {
        assertNull(new DOMAttributeIterator(null, new QName("")));
    }

    @Test
    public void testDOMAttributeIterator_emptyElement() {
        Element emptyElement = mock(Element.class);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), null), new QName(""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_singleAttribute() {
        Element element = mock(Element.class);
        Attr attribute = mock(Attr.class);
        when(element.getAttributeNode(ArgumentMatchers.eq("attrName"))).thenReturn(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), null), new QName("attrName"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attribute, nodePointer.getNode());
        assertSame(new QName("attrName"), nodePointer.getQName());
    }

    @Test
    public void testDOMAttributeIterator_multipleAttributes() {
        Element element = mock(Element.class);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        NodeList nodeList = mock(NodeList.class);
        when(element.getAttributes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(2);

        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        when(nodeList.item(0)).thenReturn(attr1);
        when(nodeList.item(1)).thenReturn(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), null), new QName(""));
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertSame(attr1, nodePointer1.getNode());
        assertSame(new QName(""), nodePointer1.getQName());

        NodePointer nodePointer2 = iterator.getNodePointer();
        assertSame(attr2, nodePointer2.getNode());
        assertSame(new QName(""), nodePointer2.getQName());

        // Test position
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_multipleAttributesWithFilter() {
        Element element = mock(Element.class);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        NodeList nodeList = mock(NodeList.class);
        when(element.getAttributes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(3);

        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        Attr attr3 = mock(Attr.class);
        when(nodeList.item(0)).thenReturn(attr1);
        when(nodeList.item(1)).thenReturn(attr2);
        when(nodeList.item(2)).thenReturn(attr3);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), null), new QName("attrName"));
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNull(nodePointer1);

        iterator.setPosition(1);
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertSame(attr2, nodePointer2.getNode());
        assertSame(new QName("attrName"), nodePointer2.getQName());
    }

    @Test
    public void testDOMAttributeIterator_namespaceAware() {
        Element element = mock(Element.class);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        NodeList nodeList = mock(NodeList.class);
        when(element.getAttributes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);

        Attr attr = mock(Attr.class);
        when(nodeList.item(0)).thenReturn(attr);
        String attrValue = "http://example.com:namespace1";
        when(attr.getValue()).thenReturn(attrValue);
        String attrPrefix = "ns1";
        when(attr.getPrefix()).thenReturn(attrPrefix);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), "http://example.com:namespace"), new QName("", "attrName"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attr, nodePointer.getNode());
        assertSame(new QName("", "attrName"), nodePointer.getQName());
        assertEquals("http://example.com:namespace1", nodePointer.getNodePointer().getNode().getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_namespaceAware_emptyNamespace() {
        Element element = mock(Element.class);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        NodeList nodeList = mock(NodeList.class);
        when(element.getAttributes()).thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);

        Attr attr = mock(Attr.class);
        when(nodeList.item(0)).thenReturn(attr);
        String attrValue = "attrValue";
        when(attr.getValue()).thenReturn(attrValue);
        when(attr.getPrefix()).thenReturn("");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(mock(Node.class), null), new QName("", "attrName"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(attr, nodePointer.getNode());
        assertSame(new QName("", "attrName"), nodePointer.getQName());
        assertEquals("attrValue", nodePointer.getNodePointer().getNode().getNodeValue());
    }
}