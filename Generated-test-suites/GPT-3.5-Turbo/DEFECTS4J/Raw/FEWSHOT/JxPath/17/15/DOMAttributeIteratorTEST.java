package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;


public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = null;
        QName name = null;
        DOMAttributeIterator domAttributeIterator = new DOMAttributeIterator(parent, name);
        assertNotNull(domAttributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new DOMAttributePointer();
        QName name = new QName();
        DOMAttributeIterator domAttributeIterator = new DOMAttributeIterator(parent, name);
        NodePointer result = domAttributeIterator.getNodePointer();
        assertNull(result);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new DOMAttributePointer();
        QName name = new QName();
        DOMAttributeIterator domAttributeIterator = new DOMAttributeIterator(parent, name);
        int result = domAttributeIterator.getPosition();
        assertEquals(0, result);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new DOMAttributePointer();
        QName name = new QName();
        DOMAttributeIterator domAttributeIterator = new DOMAttributeIterator(parent, name);
        boolean result = domAttributeIterator.setPosition(0);
        assertFalse(result);
    }
}