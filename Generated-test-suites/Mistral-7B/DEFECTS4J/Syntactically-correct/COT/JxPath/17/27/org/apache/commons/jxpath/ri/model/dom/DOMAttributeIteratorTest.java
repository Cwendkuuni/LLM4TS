package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {
    private NodePointer parentNodePointer;
    private DOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element element = new Element("root");
        NodeList attributes = element.getAttributes();
        attributes.item(0).setNodeValue("attr1");
        attributes.item(1).setNodeValue("attr2");
        attributes.item(2).setNodeValue("xmlns:xlink");
        parentNodePointer = new NodePointer(element);
    }

    @Test
    public void testConstructor() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        assertSame(parentNodePointer, iterator.getParent());
        assertEquals(new QName("attr1"), iterator.getName());
        assertEquals(3, iterator.getSize());
    }

    @Test
    public void testConstructorWithWildcard() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:*"));
        assertEquals(3, iterator.getSize());
    }

    @Test
    public void testIsEmpty() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("nonexistent"));
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testHasNext() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        for (int i = 0; i < iterator.getSize(); i++) {
            iterator.next();
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        NodePointer nodePointer = iterator.next();
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getNodeValue());
        assertNull(iterator.next().getNode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNodePointerWhenEndOfIterator() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        NodePointer nodePointer = null;
        do {
            nodePointer = iterator.next();
        } while (nodePointer != null);
        iterator.next();
    }

    @Test
    public void testGetPosition() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        iterator.next();
        iterator.next();
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWhenEndOfIterator() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
    }

    @Test
    public void testSetPositionToStart() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        iterator.setPosition(0);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testTestAttr() {
        QName attrName = new QName("xmlns", "http://www.w3.org/2000/xmlns/", "xmlns");
        assertFalse(DOMAttributeIterator.testAttr(null));
        assertFalse(DOMAttributeIterator.testAttr(new Attr()));
        Attr attr = new Attr("xmlns:xlink", "http://www.w3.org/1999/xlink");
        assertTrue(DOMAttributeIterator.testAttr(attr));
        attr.setNodeValue("http://www.w3.org/1999/xlink");
        assertFalse(DOMAttributeIterator.testAttr(attr));
    }

    @Test
    public void testGetAttribute() {
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*:attr1"));
        Attr attr = iterator.next().getNode();
        assertEquals("attr1", attr.getNodeValue());
        attr = iterator.next().getNode();
        assertEquals("attr2", attr.getNodeValue());
        assertNull(iterator.next().getNode());
    }
}