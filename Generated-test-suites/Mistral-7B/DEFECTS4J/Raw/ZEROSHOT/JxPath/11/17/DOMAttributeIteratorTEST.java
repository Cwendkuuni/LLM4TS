package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Test class for {@link DOMAttributeIterator}
 */
public class DOMAttributeIteratorTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));
        assertTrue(iterator.hasNext());
        assertEquals(element, iterator.getNodePointer().getNode().getParentNode());
    }

    /**
     * Test constructor with specific attribute name
     */
    @org.junit.Test
    public void testConstructorWithName() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr1"));
        assertTrue(iterator.hasNext());
        assertEquals(new QName("http://test:attr1"), iterator.getNodePointer().getName());
    }

    /**
     * Test constructor with specific prefix and local name
     */
    @org.junit.Test
    public void testConstructorWithPrefixAndLocalName() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "prefix:localName", "value1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("prefix:localName"));
        assertTrue(iterator.hasNext());
        assertEquals(new QName("prefix:localName"), iterator.getNodePointer().getName());
    }

    /**
     * Test hasNext() method
     */
    @org.junit.Test
    public void testHasNext() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        assertTrue(iterator.hasNext());
        assertEquals(iterator.getNodePointer().getName(), new QName("http://test:attr1"));
        assertTrue(iterator.hasNext());
        assertEquals(iterator.getNodePointer().getName(), new QName("http://test:attr2"));
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getNodePointer() method
     */
    @org.junit.Test
    public void testGetNodePointer() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(nodePointer, iterator.getNodePointer());
        assertSame(nodePointer.getNode(), iterator.getNodePointer().getNode());

        assertNull(nodePointer.getNode().getPreviousSibling());
        assertEquals(nodePointer.getParent(), new NodePointer(element));
    }

    /**
     * Test getPosition() method
     */
    @org.junit.Test
    public void testGetPosition() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        iterator.next();
        assertEquals(iterator.getPosition(), 1);

        iterator.next();
        assertEquals(iterator.getPosition(), 2);

        iterator.next();
        assertEquals(iterator.getPosition(), 3);

        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(iterator.getPosition(), 3);
    }

    /**
     * Test setPosition() method
     */
    @org.junit.Test
    public void testSetPosition() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        iterator.next();
        iterator.next();

        iterator.setPosition(1);
        assertEquals(iterator.getNodePointer().getName(), new QName("http://test:attr1"));

        iterator.setPosition(2);
        assertEquals(iterator.getNodePointer().getName(), new QName("http://test:attr2"));

        iterator.setPosition(0);
        assertFalse(iterator.hasNext());
        assertEquals(iterator.getNodePointer().getName(), new QName("http://test:attr1"));
    }

    /**
     * Test equals() method
     */
    @org.junit.Test
    public void testEquals() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        assertTrue(iterator1.equals(iterator2));

        Object obj = new Object();
        assertFalse(iterator1.equals(obj));

        DOMAttributeIterator iterator3 = new DOMAttributeIterator(new NodePointer(new Element("root")), new QName("http://test:attr*"));
        assertFalse(iterator1.equals(iterator3));
    }

    /**
     * Test hashCode() method
     */
    @org.junit.Test
    public void testHashCode() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        assertEquals(iterator1.hashCode(), iterator2.hashCode());
    }

    /**
     * Test toString() method
     */
    @org.junit.Test
    public void testToString() {
        Element element = new Element("root");
        element.setAttributeNS("http://test", "attr1", "value1");
        element.setAttributeNS("http://test", "attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test:attr*"));

        String expected = "DOMAttributeIterator{position=0, iterator=[Attr{name=http://test:attr1, value=value1, namespaceURI=http://test}, Attr{name=http://test:attr2, value=value2, namespaceURI=http://test}]}";
        String actual = iterator.toString();
        assertEquals(expected, actual);
    }
}