package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.apache.commons.cli.QName;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();

        QName name = new QName("test", "test", "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNotNull(iterator);
        assertEquals(1, iterator.getPosition());

        Attr attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test1", attr.getValue());
        assertEquals(name, iterator.getNodePointer().getNodeName());
        assertEquals("http://test.com", iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test2", attr.getValue());
        assertEquals(name, iterator.getNodePointer().getNodeName());
        assertEquals("http://test.com", iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithWildcardQName() {
        Element element = createTestElement();

        QName name = new QName("*", "*", "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNotNull(iterator);
        assertEquals(2, iterator.getPosition());

        Attr attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test1", attr.getValue());
        assertEquals(new QName("test", "test", "http://test.com"), iterator.getNodePointer().getNodeName());
        assertEquals("http://test.com", iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test2", attr.getValue());
        assertEquals(new QName("test", "test2", "http://test.com"), iterator.getNodePointer().getNodeName());
        assertEquals("http://test.com", iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyNamespaceURI() {
        Element element = createTestElement();

        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNotNull(iterator);
        assertEquals(2, iterator.getPosition());

        Attr attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test1", attr.getValue());
        assertEquals(name, iterator.getNodePointer().getNodeName());
        assertNull(iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        attr = (Attr) iterator.getNodePointer().getNode();
        assertEquals("test2", attr.getValue());
        assertEquals(name, iterator.getNodePointer().getNodeName());
        assertNull(iterator.getNodePointer().getNamespaceURI());
        iterator.next();

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithNullName() {
        Element element = createTestElement();

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorWithNullParentPointer() {
        QName name = new QName("test", "test", "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorEmptyList() {
        Element element = createTestElement();

        QName name = new QName("test", "test", "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithWrongNamespaceURI() {
        Element element = createTestElement();

        QName name = new QName("test", "test", "http://other.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    private Element createTestElement() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        element.setAttribute("test1", "test1");
        element.setAttribute("test2", "test2");
        return element;
    }
}