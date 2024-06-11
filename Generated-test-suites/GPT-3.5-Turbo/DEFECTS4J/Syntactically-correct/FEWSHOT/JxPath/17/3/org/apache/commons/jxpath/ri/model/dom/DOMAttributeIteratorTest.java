package org.apache.commons.jxpath.ri.model.dom;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = null;
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = null;
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer pointer = iterator.getNodePointer();
        assertNull(pointer);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = null;
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = null;
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(1);
        assertFalse(result);
    }
}