package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DOMParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private NodeIterator iterator;
    private Element element;

    @Before
    public void setUp() throws Exception {
        String xml = "<root xmlns:x='http://test.com' xmlns:y='http://test.net'><child attr1='value1' attr2='value2' xmlns:z='http://test.org'/></root>";
        Document doc = DocumentBuilderFactory.newDocumentBuilder().parse(new org.jdom2.input.StringBuilderParser().parse(xml));
        element = (Element) doc.getDocumentElement();
        iterator = new DOMAttributeIterator(new DOMPointer(doc, doc.getDocumentElement()), new QName("*", ""));
    }

    @Test
    public void testEmptyIterator() {
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.next();
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getNodeName());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testGetNodePointerWithNamespace() {
        QName qname = new QName("x", "attr1");
        NodePointer nodePointer = iterator.next();
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals(qname.getLocalPart(), attr.getNodeName());
        assertEquals(qname.getNamespaceURI(), attr.getNamespaceURI());
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testNextAfterEnd() {
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionAfterEnd() {
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.setPosition(5);
    }

    @Test
    public void testSetPositionWithinRange() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.next();
        iterator.setPosition(2);
        NodePointer nextNodePointer = iterator.next();
        assertEquals(nodePointer.getNode(), nextNodePointer.getNode());
        iterator.setPosition(0);
        NodePointer firstNodePointer = iterator.next();
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testSetPositionBeforeStart() {
        iterator.setPosition(-1);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNoMatchingAttributes() {
        iterator = new DOMAttributeIterator(new DOMPointer(element, element.getChildNodes().item(0)), new QName("z", "attr1"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMatchingAttributes() {
        iterator = new DOMAttributeIterator(new DOMPointer(element, element.getChildNodes().item(0)), new QName("", "attr2"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr2", attr.getNodeName());
        assertEquals("value2", attr.getValue());
    }

    @Test
    public void testIteratorWithMatchingNamespaceAttributes() {
        iterator = new DOMAttributeIterator(new DOMPointer(element, element.getChildNodes().item(0)), new QName("x", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        Attr attr = (Attr) nodePointer.getNode();
        assertEquals("attr1", attr.getNodeName());
        assertEquals("value1", attr.getValue());
    }
}