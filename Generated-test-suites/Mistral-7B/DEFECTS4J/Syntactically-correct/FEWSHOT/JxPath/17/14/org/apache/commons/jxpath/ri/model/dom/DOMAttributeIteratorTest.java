package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorWithElement() {
        Element element = newTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("*"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals(5, iterator.getPosition());
        iterator.setPosition(6);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithAttribute() {
        Element element = newTestElement();
        Attr attribute = element.getAttributeNode(new QName("color"));
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("color"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("red", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("size", nodePointer.getNodeValue());
        iterator.setPosition(0);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("color", nodePointer.getNodeValue());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithQName() {
        Element element = newTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("color"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("red", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("size", nodePointer.getNodeValue());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element element = newTestElementWithNamespace();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("http://www.example.com/ns:myAttribute"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("red", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("blue", nodePointer.getNodeValue());
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullParent() {
        new DOMAttributeIterator(null, new QName("color"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullQName() {
        Element element = newTestElement();
        new DOMAttributeIterator(new NodePointer(null, element), null);
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyList() {
        Element element = newTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("*"));
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithSingleAttribute() {
        Element element = newTestElementWithSingleAttribute();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("color"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("red", nodePointer.getNodeValue());
        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleAttributes() {
        Element element = newTestElementWithMultipleAttributes();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("*"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("color", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("size", nodePointer.getNodeValue());
        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("shape", nodePointer.getNodeValue());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithTestAttr() {
        Element element = newTestElementWithTestAttr();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("testAttr"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("value1", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("value2", nodePointer.getNodeValue());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithMismatchedPrefix() {
        Element element = newTestElementWithMismatchedPrefix();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("xmlns:testAttr"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithNullAttribute() {
        Element element = newTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("color"));
        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(Node.ATTRIBUTE_NODE, nodePointer.getNodeType());
        assertEquals("red", nodePointer.getNodeValue());
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    private Element newTestElement() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("color", "red"));
        element.setAttributeNode(new Attr("size", "10"));
        return element;
    }

    private Element newTestElementWithNamespace() {
        Element element = new Element("root");
        element.setAttributeNodeNS(new Attr("http://www.example.com/ns:color", "red"));
        element.setAttributeNodeNS(new Attr("http://www.example.com/ns:size", "10"));
        return element;
    }

    private Element newTestElementWithSingleAttribute() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("color", "red"));
        return element;
    }

    private Element newTestElementWithMultipleAttributes() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("color", "red"));
        element.setAttributeNode(new Attr("size", "10"));
        element.setAttributeNode(new Attr("shape", "circle"));
        return element;
    }

    private Element newTestElementWithTestAttr() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("testAttr", "value1"));
        element.setAttributeNode(new Attr("testAttr", "value2"));
        return element;
    }

    private Element newTestElementWithMismatchedPrefix() {
        Element element = new Element("root");
        element.setAttributeNodeNS(new Attr("http://www.example.com/ns:xmlns:testAttr", "value"));
        return element;
    }
}