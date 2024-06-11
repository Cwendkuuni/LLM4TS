package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
    }

    @Test
    public void testNextAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testPreviousAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        iterator.next();
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        iterator.previous();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testHasNextAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertFalse(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasPreviousAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertFalse(iterator.hasPrevious());
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasPrevious());
        iterator.previous();
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testGetPositionAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetNameAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(name.getName(), iterator.getName().getName());
        assertEquals(name.getNamespaceURI(), iterator.getName().getNamespaceURI());
        assertEquals(name.getPrefix(), iterator.getName().getPrefix());
    }

    @Test
    public void testGetParentAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals(element, iterator.getParent().getNode());
    }

    @Test
    public void testGetValueAttribute() {
        Element element = createTestElement();
        QName name = new QName("test", "localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(element), name);
        assertNotNull(iterator);
        assertEquals("testValue", iterator.getValue());
        iterator.next();
        assertNull(iterator.getValue());
        iterator.next();
        assertNull(iterator.getValue());
    }

    private Element createTestElement() {
        Element element = newElement("testElement");
        element.setAttributeNS("http://test.com/ns", "testAttribute1", "testValue1");
        element.setAttributeNS("http://test.com/ns", "testAttribute2", "testValue2");
        return element;
    }
}