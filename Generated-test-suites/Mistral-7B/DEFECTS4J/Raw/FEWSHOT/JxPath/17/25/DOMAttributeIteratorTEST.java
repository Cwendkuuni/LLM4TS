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
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator);
        assertEquals(3, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithQName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test", "test"));
        assertNotNull(iterator);
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:test", iterator.getNodePointer().getNodeName());
    }

    @Test
    public void testDOMAttributeIteratorEmpty() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("nonexistent", "*"));
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorMixedQName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test", "*"));
        assertNotNull(iterator);
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:test2", iterator.getNodePointer().getNodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorNullNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorNullQName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
    }

    @Test
    public void testDOMAttributeIteratorNext() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        iterator.next();
        iterator.next();
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorReset() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        iterator.next();
        iterator.next();
        iterator.reset();
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorPrevious() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        iterator.next();
        iterator.next();
        iterator.previous();
        assertNotNull(iterator.getNodePointer());
        assertEquals("test:test1", iterator.getNodePointer().getNodeName());
    }

    private Element createTestElement() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:test", "test-namespace");
        element.setAttributeNS(null, "test1", "value1");
        element.setAttributeNS(null, "test2", "value2");
        element.setAttributeNS(null, "test:test1", "value3");
        element.setAttributeNS(null, "test:test2", "value4");
        return element;
    }
}