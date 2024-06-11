package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        DOMNodePointer root = new DOMNodePointer(new Element() {
            {
                setAttribute("data", "value");
            }
        }, "http://www.w3.org/1999/xhtml");

        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("data"));
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals("value", iterator.getNodePointer().getNodeValue());
        assertNull(iterator.getNodePointer().getNodeNext());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Element element = new Element() {
            {
                setAttribute("attr1", "value1");
                setAttribute("attr2", "value2");
                setAttribute("attr3", "value3");
            }
        };
        DOMNodePointer root = new DOMNodePointer(element, "http://www.w3.org/1999/xhtml");

        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer().getNodeNext());
        assertEquals("value1", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value2", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value3", iterator.getNodePointer().getNodeValue());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.previous();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value2", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasPrevious());
        assertFalse(iterator.hasNext());

        iterator.previous();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value1", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasPrevious());
        assertFalse(iterator.hasNext());

        iterator.previous();
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MixedLocalAndNamespaceAttributes() {
        Element element = new Element() {
            {
                setAttributeNS("http://example.com/ns", "attr1", "value1");
                setAttribute("attr2", "value2");
                setAttribute("xmlns:x", "http://example.com/ns");
                setAttributeNS("http://example.com/ns", "attr3", "value3");
            }
        };
        DOMNodePointer root = new DOMNodePointer(element, "http://www.w3.org/1999/xhtml");

        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr", "http://example.com/ns"));
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer().getNodeNext());
        assertEquals("value1", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value2", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());

        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value3", iterator.getNodePointer().getNodeValue());
        assertEquals(3, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(root, new QName("attr"));
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer().getNodeNext());
        assertEquals("value2", iterator.getNodePointer().getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertEquals("value3", iterator.getNodePointer().getNodeValue());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyIterator() {
        Element element = new Element();
        DOMNodePointer root = new DOMNodePointer(element, "http://www.w3.org/1999/xhtml");

        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

}

class DOMAttributePointer extends DOMAttributeIterator.DOMAttributePointer {
    DOMAttributePointer(NodePointer parent, Attr attr) {
        super(parent, attr);
    }
}