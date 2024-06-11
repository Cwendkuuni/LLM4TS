package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DOMAttributeIteratorTest {

    private Node testNode;
    private Element testElement;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        Document document = DocumentBuilderFactory.newDocumentBuilder().newDocument();
        testElement = document.createElement("test");
        testNode = testElement;
        testElement.setAttribute("test", "testValue");
        testElement.setAttribute("xmlns:t", "http://test.com");
        testElement.setAttributeNS("http://test.com", "test:test", "testValue2");
        testElement.setAttribute("ns:test", "testValue3");
        Node parent = document.createElement("parent");
        parent.appendChild(testElement);
        testNode.appendChild(parent);
    }

    @Test
    public void testIteratorWithWildcardName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("*"));
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithSpecificName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test"));
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithSpecificNamespaceAndName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("t:test"));
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithSpecificNamespaceAndNameAndPrefix() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("xmlns:t", "test"));
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithNullName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), null);
        assertSame(testElement, iterator.getNodePointer().getNode());
        iterator.next();
        iterator.next();
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test"));
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test"));
        iterator.next();
        assertFalse(iterator.setPosition(1));
        iterator.next();
        assertFalse(iterator.setPosition(-1));
        iterator.next();
        assertFalse(iterator.setPosition(4));
        iterator.next();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testNextWhenEndOfIteration() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test"));
        iterator.next();
        iterator.next();
        assertNull(iterator.getNodePointer());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextWhenNotAtBeginning() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }
}