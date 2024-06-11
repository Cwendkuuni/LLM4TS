package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        Element emptyElement = createEmptyElement("empty");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(emptyElement), null);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeWithWildcardName() {
        Element element = createElementWithAttribute("elem", "attr", "value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr", nodePointer.getNodeName().getName());
        assertEquals("value", nodePointer.getNodeValue());
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testAttributeWithNamespaceAndLocalName() {
        Element element = createElementWithAttribute("elem", "xmlns:x", "ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:x", "x"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("xmlns:x", nodePointer.getNodeName().getNamespaceURI() + nodePointer.getNodeName().getName());
        assertEquals("ns", nodePointer.getNodeValue());
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testAttributeWithName() {
        Element element = createElementWithAttribute("elem", "name", "value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("name"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("name", nodePointer.getNodeName().getName());
        assertEquals("value", nodePointer.getNodeValue());
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(1);
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testParentNodeNotElement() {
        Node nonElementParent = createTextNode("non-element parent");
        Element childElement = createElement("elem");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(nonElementParent), null);
        iterator.setPosition(1);
        iterator.getNodePointer();
    }

    @Test
    public void testMultipleAttributes() {
        Element element = createElementWithAttributes("elem", "attr1", "value1", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals("attr1", nodePointer1.getNodeName().getName());
        assertEquals("value1", nodePointer1.getNodeValue());
        iterator.setPosition(1);
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals("attr2", nodePointer2.getNodeName().getName());
        assertEquals("value2", nodePointer2.getNodeValue());
    }

    @Test
    public void testNextAttributeAfterLast() {
        Element element = createElementWithAttributes("elem", "attr1", "value1", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testPreviousAttributeBeforeFirst() {
        Element element = createElementWithAttributes("elem", "attr1", "value1", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        iterator.setPosition(0);
        iterator.previous();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testPreviousAttributeAfterFirst() {
        Element element = createElementWithAttributes("elem", "attr1", "value1", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        iterator.previous();
        NodePointer previousNodePointer = iterator.getNodePointer();
        assertNotNull(previousNodePointer);
        assertEquals("attr1", previousNodePointer.getNodeName().getName());
        assertEquals("value1", previousNodePointer.getNodeValue());
    }

    private Element createElement(String tagName) {
        Document document = createDocument();
        return document.createElement(tagName);
    }

    private Element createElementWithAttribute(String tagName, String name, String value) {
        Element element = createElement(tagName);
        element.setAttribute(name, value);
        return element;
    }

    private Element createElementWithAttributes(String tagName, String... namesAndValues) {
        Element element = createElement(tagName);
        for (String nameValue : namesAndValues) {
            String[] nameValuePair = nameValue.split(" ", 2);
            String name = nameValuePair[0];
            String value = nameValuePair.length > 1 ? nameValuePair[1] : "";
            element.setAttribute(name, value);
        }
        return element;
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Node createTextNode(String text) {
        Document document = createDocument();
        return document.createTextNode(text);
    }
}