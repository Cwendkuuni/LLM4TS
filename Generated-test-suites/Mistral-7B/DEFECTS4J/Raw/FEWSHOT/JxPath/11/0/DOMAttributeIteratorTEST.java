package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        Element element = createMockElement("root", "xmlns:abc=http://example.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:abc"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:abc", nodePointer.getName().getName());
        assertEquals("http://example.com", nodePointer.getNode().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = createMockElement("root", "foo='bar' xmlns:abc='http://example.com' data='123' xmlns:def='http://another-example.com'");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:abc"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:abc", nodePointer.getName().getName());
        assertEquals("http://example.com", nodePointer.getNode().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("data", nodePointer.getName().getName());
        assertEquals("123", nodePointer.getNode().getNodeValue());
        assertEquals(3, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("def", nodePointer.getName().getName());
        assertNull(nodePointer.getNode().getNodeValue());
        assertEquals(4, iterator.getPosition());
        assertFalse(iterator.setPosition(5));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_AttributesWithNullValues() {
        Element element = createMockElement("root", "foo='bar' xmlns:abc='' xmlns:def='null'");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:abc"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:abc", nodePointer.getName().getName());
        assertNull(nodePointer.getNode().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:def"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:def", nodePointer.getName().getName());
        assertNull(nodePointer.getNode().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("abc", nodePointer.getName().getName());
        assertNull(nodePointer.getNode().getNodeValue());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_AttributesWithEmptyStringValues() {
        Element element = createMockElement("root", "foo='' xmlns:abc='' xmlns:def=''");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:abc"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:abc", nodePointer.getName().getName());
        assertEquals("", nodePointer.getNode().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:def"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:def", nodePointer.getName().getName());
        assertEquals("", nodePointer.getNode().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("abc", nodePointer.getName().getName());
        assertEquals("", nodePointer.getNode().getNodeValue());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_AttributesWithPrefixes() {
        Element element = createMockElement("root", "xmlns:a='http://example.com/a' xmlns:b='http://example.com/b' foo='bar' xmlns:c='http://example.com/c' xmlns:d='http://example.com/d'");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:b"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:b", nodePointer.getName().getName());
        assertEquals("http://example.com/b", nodePointer.getNode().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:a"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:a", nodePointer.getName().getName());
        assertEquals("http://example.com/a", nodePointer.getNode().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:d"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("xmlns:d", nodePointer.getName().getName());
        assertEquals("", nodePointer.getNode().getNodeValue());
        assertEquals(5, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(element), new QName("foo"));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("foo", nodePointer.getName().getName());
        assertEquals("bar", nodePointer.getNode().getNodeValue());
        assertEquals(4, iterator.getPosition());
        assertTrue(iterator.setPosition(3));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertNotNull(nodePointer.getNode());
        assertEquals("c", nodePointer.getName().getName());
        assertEquals("", nodePointer.getNode().getNodeValue());
        assertEquals(5, iterator.getPosition());
        assertFalse(iterator.setPosition(6));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_EmptyList() {
        Element element = createMockElement("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("foo"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttributes() {
        Element element = createMockElement("root", "foo='bar' xmlns:abc='http://example.com/a' xmlns:b='http://example.com/b'");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:x"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.getNodePointer());
    }

    private Element createMockElement(String tagName, String attributeString) {
        Element element = new Element(tagName);
        NamedNodeMap attributes = element.getAttributes();
        String[] attrArray = attributeString.split("\\s+");
        for (String attr : attrArray) {
            if (attr.startsWith("xmlns:")) {
                int equals = attr.indexOf("=");
                String prefix = attr.substring(7, equals);
                String ns = attr.substring(equals + 1, attr.length() - 1);
                Attr attrNode = document.createAttributeNS(ns, prefix);
                attrNode.setValue(ns);
                attributes.setNamedItem(attrNode);
            } else {
                int equals = attr.indexOf("=");
                String name = equals > 0 ? attr.substring(0, equals) : attr;
                Attr attrNode = document.createAttribute(name);
                attrNode.setValue(attr.substring(equals + 1));
                attributes.setNamedItem(attrNode);
            }
        }
        return element;
    }

    private Document document = new DocumentImpl();
}