package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertEquals(2, iterator.getSize());
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("foo:bar"));
        assertEquals(0, iterator.getSize());
    }

    @Test
    public void testDOMAttributeIteratorWithName() {
        Element element = createElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        assertEquals(1, iterator.getSize());
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = createElement("root", null);
        element.setAttributeNS("xmlns:xsi", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xsi"));
        assertEquals(1, iterator.getSize());
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespaceURI() {
        Element element = createElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        assertEquals(1, iterator.getSize());
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespaceResolver() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        assertEquals(1, iterator.getSize());
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefixAndNamespaceURI() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xsi"));
        assertEquals(1, iterator.getSize());
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNodeValue());
    }

    @Test
    public void testDOMAttributeIteratorWithDifferentName() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:foo:xsi"));
        assertEquals(0, iterator.getSize());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyList() {
        Element element = createElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertEquals(0, iterator.getSize());
    }

    @Test
    public void testDOMAttributeIteratorMoveNext() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMovePrevious() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi1", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttributeNS("", "xmlns:xsi2", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        iterator.next();
        iterator.next();
        iterator.previous();
        assertEquals(1, iterator.getPosition());
        iterator.previous();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMoveToEnd() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi1", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttributeNS("", "xmlns:xsi2", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        iterator.next();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertEquals(4, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMoveToBeginning() {
        Element element = createElement("root", null);
        element.setAttributeNS("", "xmlns:xsi1", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttributeNS("", "xmlns:xsi2", "http://www.w3.org/2001/XMLSchema-instance");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:xsi"));
        iterator.next();
        iterator.next();
        iterator.previous();
        iterator.previous();
        iterator.previous();
        assertTrue(iterator.hasPrevious());
        iterator.previous();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorEmptiesWhenParentIsRemoved() {
        Element root = createElement("root", null);
        Element child = createElement("child", null);
        root.appendChild(child);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(root), new QName("*"));
        assertEquals(2, iterator.getSize());

        root.removeChild(child);
        iterator = new DOMAttributeIterator(new DOMPointer(root), new QName("*"));
        assertEquals(0, iterator.getSize());
    }

    private Element createElement(String tagName, String namespaceURI) {
        Document document = createDocument();
        Element element = document.createElement(tagName);
        if (namespaceURI != null) {
            document.setNamespaceURI(namespaceURI);
            element.setNamespaceURI(namespaceURI);
        }
        document.appendChild(element);
        return element;
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.newDocument();
    }
}