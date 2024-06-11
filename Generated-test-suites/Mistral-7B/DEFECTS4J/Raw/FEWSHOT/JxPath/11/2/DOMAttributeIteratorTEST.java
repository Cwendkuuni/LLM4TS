package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructorWithElementAndEmptyQName() {
        NodePointer parentPointer = new DOMPoint("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(""));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithElementAndQName() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr", attrPointer.getName().getName());
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());
    }

    @Test
    public void testConstructorWithElementAndWildcardQName() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        String attrName = attrPointer.getName().getName();
        assertEquals("*", attrName);
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());

        iterator.setPosition(2);
        attrPointer = (DOMAttribePointer) iterator.getNodePointer();
        attrName = attrPointer.getName().getName();
        assertEquals("*", attrName);
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());

        iterator.setPosition(0);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        attrName = attrPointer.getName().getName();
        assertEquals("*", attrName);
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());
    }

    @Test
    public void testConstructorWithElementAndNamespaceQName() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("http://example.com/ns", "attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr", attrPointer.getName().getName());
        assertEquals("http://example.com/ns", attrPointer.getNodePointer().getNode().getNamespaceURI());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(0);
        assertNotNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr", attrPointer.getName().getName());
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());

        iterator.setPosition(0);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testHasNext() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());

        iterator.setPosition(2);
        assertNotNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasNext());

        iterator.setPosition(3);
        assertNull(iterator.getNodePointer());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        NodePointer parentPointer = new DOMPoint("test");
        Element testElement = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals("attr", attrPointer.getName().getName());
        assertEquals("", attrPointer.getNodePointer().getNode().getNodeName());

        iterator.setPosition(2);
        attrPointer = (DOMAttributePointer) iterator.next();
        assertNull(attrPointer);
        assertEquals(3, iterator.getPosition());
    }
}