package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        Element element = newElement("root", null);
        element.setAttribute("id", "1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("id"));

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("id", nodePointer.getNodeName().getName());
        assertEquals("1", nodePointer.getNodeName().getLocalPart());
        assertEquals("1", nodePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = newElement("root", null);
        element.setAttribute("id", "1");
        element.setAttribute("class", "test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        List<NodePointer> nodePointers = new ArrayList<>();

        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            nodePointers.add(nodePointer);
            iterator.next();
        }

        NodePointer idNodePointer = nodePointers.get(0);
        assertEquals("id", idNodePointer.getNodeName().getName());
        assertEquals("1", idNodePointer.getNodeName().getLocalPart());
        assertEquals("1", idNodePointer.getNodeValue());

        NodePointer classNameNodePointer = nodePointers.get(1);
        assertEquals("class", classNameNodePointer.getNodeName().getName());
        assertEquals("test", classNameNodePointer.getNodeName().getLocalPart());
        assertNull(classNameNodePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_NoMatchingAttribute() {
        Element element = newElement("root", null);
        element.setAttribute("id", "1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("class"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAttribute() {
        Element element = newElement("root", "xmlns:x");
        element.setAttributeNS("xmlns:x", "xml:id", "1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xml:id"));

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("xml:id", nodePointer.getNodeName().getName());
        assertEquals("xml:id", nodePointer.getNodeName().getPrefix() + ":" + nodePointer.getNodeName().getLocalPart());
        assertEquals("1", nodePointer.getNodeValue());
    }

    @Test
    public void testDOMAttributeIterator_EmptyNode() {
        Element element = newElement("root", null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_MixedAttributeTypes() {
        Element element = newElement("root", null);
        element.setAttribute("id", "1");
        element.setAttributeNS("xmlns:x", "xml:id", "2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));

        List<NodePointer> nodePointers = new ArrayList<>();

        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            nodePointers.add(nodePointer);
            iterator.next();
        }

        NodePointer idNodePointer = nodePointers.get(0);
        assertEquals("id", idNodePointer.getNodeName().getName());
        assertEquals("1", idNodePointer.getNodeName().getLocalPart());
        assertEquals("1", idNodePointer.getNodeValue());

        NodePointer xmlIDNodePointer = nodePointers.get(1);
        assertEquals("xml:id", xmlIDNodePointer.getNodeName().getName());
        assertEquals("xml:id", xmlIDNodePointer.getNodeName().getPrefix() + ":" + xmlIDNodePointer.getNodeName().getLocalPart());
        assertEquals("2", xmlIDNodePointer.getNodeValue());
    }

    private Node newElement(String tagName, String namespaceURI) {
        Document doc = newDocument();
        Element element = doc.createElement(tagName);
        if (namespaceURI != null) {
            doc.setDocumentElementNS(namespaceURI, tagName);
        }
        return doc;
    }

    private Document newDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.newDocument();
        } catch (DocumentInitializationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}