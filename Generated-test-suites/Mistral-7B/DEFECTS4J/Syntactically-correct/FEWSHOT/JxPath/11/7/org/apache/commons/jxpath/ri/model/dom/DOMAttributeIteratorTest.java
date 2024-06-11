package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        NodePointer parentPointer = new DOMElementPointer(createTestElement("root", "abc"));
        QName name = new QName("test:attr", "test:ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:attr", ((DOMAttributePointer) iterator.getNodePointer()).getName().getName());
        assertEquals("test:ns", ((DOMAttributePointer) iterator.getNodePointer()).getNode().getNamespaceURI());

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        NodePointer parentPointer = new DOMElementPointer(createTestElement("root", "abc"));
        QName name = new QName("test:attr", "test:ns");
        QName name2 = new QName("test:attr2", "test:ns2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:attr", ((DOMAttributePointer) iterator.getNodePointer()).getName().getName());
        assertEquals("test:ns", ((DOMAttributePointer) iterator.getNodePointer()).getNode().getNamespaceURI());

        iterator.next();
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:attr2", ((DOMAttributePointer) iterator.getNodePointer()).getName().getName());
        assertEquals("test:ns2", ((DOMAttributePointer) iterator.getNodePointer()).getNode().getNamespaceURI());

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_EmptyList() {
        NodePointer parentPointer = new DOMElementPointer(createTestElement("root", "abc"));
        QName name = new QName("test:attr", "test:ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.setPosition(0));
    }

    private Element createTestElement(String tagName, String textContent) {
        Document document = createDocument();
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        document.getDocumentElement().appendChild(element);
        return element;
    }

    private Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = null;
        try {
            document = builder.newDocument();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}