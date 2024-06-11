package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIteratorEmpty() {
        NodePointer parent = new DOMPointer("", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorSingleAttribute() {
        String xml = "<root attr='value'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("value", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMultipleAttributes() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr2"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("value2", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorNamespace() {
        String xml = "<root xmlns:x='http://example.com' x:attr='value'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals("value", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMoveNext() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr2"));
        iterator.next();
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value3", iterator.getNodePointer().getNodeValue());
        assertEquals(3, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIteratorMoveNextOutOfBounds() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr2"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testDOMAttributeIteratorMovePrevious() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr2"));
        iterator.next();
        iterator.next();
        iterator.previous();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value1", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIteratorMovePreviousOutOfBounds() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'/>";
        NodePointer parent = new DOMPointer(xml, "root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attr2"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.previous();
    }
}