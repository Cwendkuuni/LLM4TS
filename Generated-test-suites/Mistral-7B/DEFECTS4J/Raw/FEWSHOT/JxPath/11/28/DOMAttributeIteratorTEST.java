package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ElementImpl;
import org.w3c.dom.NamedNodeMapImpl;
import org.w3c.dom.AttrImpl;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    private Element element;

    @Before
    public void setUp() {
        parent = new DOMPointer("http://www.w3.org/1999/xhtml", "/html/body");
        name = new QName("", "id");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        element = new ElementImpl(doc, "html");
        doc.appendChild(element);
        element.setAttributeNodeNS(doc.createAttributeNS("http://www.w3.org/1999/xhtml", "xmlns:xhtml"), "xmlns:xhtml");
        element.setAttributeNodeNS(doc.createAttributeNS("http://www.w3.org/1999/xhtml", "xmlns"), "html");
        element.setAttributeNodeNS(doc.createAttributeNS("http://www.w3.org/1999/xhtml", "id"), "testID");
        element.setAttributeNodeNS(doc.createAttributeNS("http://www.w3.org/1999/xhtml", "class"), "testClass");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(parent, nodePointer.getNodePointer());
        assertSame(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName()), nodePointer.getNode());
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        NodePointer nodePointer = iterator.next();
        assertSame(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName()), nodePointer.getNode());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPosition() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.next();
        assertSame(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "class"), nodePointer.getNode());
        iterator.setPosition(0);
        nodePointer = iterator.next();
        assertSame(element.getAttributeNodeNS(name.getNamespaceURI(), name.getName()), nodePointer.getNode());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testTestAttr() {
        assertTrue(DOMAttributeIterator.testAttr(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "xmlns:xhtml"), name));
        assertFalse(DOMAttributeIterator.testAttr(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "xmlns"), name));
        assertTrue(DOMAttributeIterator.testAttr(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "id"), name));
        assertFalse(DOMAttributeIterator.testAttr(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "class"), name));
    }

    @Test
    public void testGetAttribute() {
        QName name2 = new QName("", "class");
        Attr attr = DOMAttributeIterator.getAttribute(element, name2);
        assertSame(element.getAttributeNodeNS("http://www.w3.org/1999/xhtml", "class"), attr);
        attr = DOMAttributeIterator.getAttribute(element, new QName("", "*"));
        assertNull(attr);
    }

    @Test
    public void testEquals() {
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent, name);
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent, name);
        assertTrue(iterator1.equals(iterator2));
        iterator1.next();
        assertFalse(iterator1.equals(iterator2));
        iterator2.next();
        assertTrue(iterator1.equals(iterator2));
    }
}