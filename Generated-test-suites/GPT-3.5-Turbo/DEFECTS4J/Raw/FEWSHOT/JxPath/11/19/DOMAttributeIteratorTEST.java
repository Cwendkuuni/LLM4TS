package org.apache.commons.jxpath.ri.model.dom;

import org.junit.Test;

import static org.junit.Assert.*;
public class DOMAttributeIteratorTest {
    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertTrue(iterator.setPosition(2));
    }
}