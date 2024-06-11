package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    private NodePointer parentNodePointer;
    private DOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element testElement = new Element("test");
        testElement.setAttribute("test1", "testValue1");
        testElement.setAttribute("test2", "testValue2");
        testElement.setAttribute("xmlns:x", "http://test.com");
        testElement.setAttribute("x:test3", "testValue3");

        NodeList attributes = testElement.getAttributes();

        parentNodePointer = new NodePointer(testElement);
        iterator = new DOMAttributeIterator(parentNodePointer, new QName("*"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithSpecificName() {
        DOMAttributeIterator specificIterator = new DOMAttributeIterator(parentNodePointer, new QName("test1"));
        assertEquals(1, specificIterator.getPosition());
        assertEquals("testValue1", ((DOMAttributePointer) specificIterator.getNodePointer()).getAttributeValue());
    }

    @Test
    public void testNext() {
        iterator.next();
        assertEquals("testValue2", ((DOMAttributePointer) iterator.getNodePointer()).getAttributeValue());
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPrevious() {
        iterator.next();
        iterator.previous();
        assertEquals("testValue1", ((DOMAttributePointer) iterator.getNodePointer()).getAttributeValue());
    }

    @Test
    public void testNoAttributes() {
        NodePointer noAttributePointer = new NodePointer(new Element("test"));;
        DOMAttributeIterator noAttributeIterator = new DOMAttributeIterator(noAttributePointer, new QName("*"));
        assertFalse(noAttributeIterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidPosition() {
        iterator.next();
        iterator.next();
        iterator.previous();
        iterator.previous();
        iterator.previous();
    }

    @Test
    public void testIteratorTraversal() {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testIteratorTraversalWithSpecificName() {
        int count = 0;
        DOMAttributeIterator specificIterator = new DOMAttributeIterator(parentNodePointer, new QName("test1"));
        while (specificIterator.hasNext()) {
            specificIterator.next();
            count++;
        }
        assertEquals(1, count);

        specificIterator = new DOMAttributeIterator(parentNodePointer, new QName("test2"));
        count = 0;
        while (specificIterator.hasNext()) {
            specificIterator.next();
            count++;
        }
        assertEquals(1, count);
    }
}