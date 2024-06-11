package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Node root = new Element("root");
        NodePointer rootPointer = new DOMNodePointer(root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*"));
        assertEquals(rootPointer, iterator.getParent());
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithQName() {
        Node root = new Element("root");
        NodePointer rootPointer = new DOMNodePointer(root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("foo:bar"));
        assertEquals(rootPointer, iterator.getParent());
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testConstructorWithElementAndQName() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertEquals(elementPointer, iterator.getParent());
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute = element.getAttributeNode("foo:bar");
        iterator.next();
        assertSame(attribute, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testConstructorWithElementAndWildcardQName() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("*"));
        assertEquals(elementPointer, iterator.getParent());
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.getAttributeNode("foo");
        iterator.next();
        assertSame(attribute1, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.getAttributeNode("bar");
        iterator.next();
        assertSame(attribute2, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        Attr attribute3 = element.getAttributeNode("baz");
        iterator.next();
        assertSame(attribute3, iterator.getCurrentNode());
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertSame(attribute1, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "bar"), "value2");
        iterator.next();
        assertSame(attribute2, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertSame(attribute1, attributePointer1.getNode());
        assertSame(attribute1, attributePointer2.getNode());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "bar"), "value2");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertTrue(iterator.hasNext());

        assertFalse(iterator.setPosition(-1));
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.setPosition(5));
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());

        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testHasNext() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "bar"), "value2");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testCurrentNode() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertSame(attribute1, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "bar"), "value2");
        iterator.next();
        assertSame(attribute2, iterator.getCurrentNode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertNull(iterator.getCurrentNode());
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testNext() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        Attr attribute2 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "bar"), "value2");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testRemove() {
        Element element = new Element("element");
        NodePointer elementPointer = new DOMNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(elementPointer, new QName("foo"));
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        Attr attribute1 = element.setAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo"), "value1");
        iterator.next();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
        assertNull(element.getAttributeNodeNS(element.getOwnerDocument(), new QName("http://example.com", "foo")));

        iterator.next();
        Attr attribute2 = iterator.getCurrentNode();
        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(2, iterator.getPosition());
        assertNull(attribute2);
    }
}