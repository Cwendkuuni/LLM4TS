package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_ElementWithAttribute() {
        Node root = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(root), new QName("test:attr"));
        assertNotNull(iterator);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test-value", attribute.getValue());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithoutAttribute() {
        Node root = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(root), new QName("non-existent:attr"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_ElementWithMultipleAttributes() {
        Node root = createTestElementWithMultipleAttributes();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(root), new QName("*"));
        assertNotNull(iterator);

        Attr firstAttribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test-value-1", firstAttribute.getValue());

        iterator.next();
        Attr secondAttribute = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test-value-2", secondAttribute.getValue());

        iterator.next(); // reach end
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullParentPointer() {
        new DOMAttributeIterator(null, new QName("test:attr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullQName() {
        NodePointer parent = new DOMPointer(createTestElement());
        new DOMAttributeIterator(parent, null);
    }

    private Node createTestElement() {
        Element element = newTestElement("root");
        element.setAttribute("test:attr", "test-value");
        return element;
    }

    private Node createTestElementWithMultipleAttributes() {
        Element element = newTestElement("root");
        element.setAttributeNodeNS(createElementWithNS("attr1", "http://test.com", "test-value-1"));
        element.setAttributeNodeNS(createElementWithNS("attr2", "http://test.com", "test-value-2"));
        return element;
    }

    private Element newTestElement(String tagName) {
        Document doc = newDocument();
        Element element = doc.createElement(tagName);
        doc.appendChild(element);
        return element;
    }

    private Node createElementWithNS(String localName, String namespaceURI, String qName) {
        Document doc = newDocument();
        Attr attr = doc.createAttributeNS(namespaceURI, localName);
        attr.setValue(qName);
        return attr;
    }

    private Document newDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return builder.newDocument();
    }
}