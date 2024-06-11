package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMParser;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    /**
     * Test constructor with valid arguments and getAttribute method.
     */
    @Test
    public void testDOMAttributeIteratorValidArg() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<element attribute1=\"value1\" xsi:id=\"element1\">" +
                "<subElement attribute2=\"value2\" />" +
                "</element>" +
                "</root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr expectedAttr = (Attr) doc.getDocumentElement().getAttributes().getNamedItem("attribute1");
        NodePointer nodePointer = iterator.getNodePointer();
        Attr actualAttr = (Attr) nodePointer.getNode();

        assertEquals(expectedAttr.getValue(), actualAttr.getValue());
        assertEquals(expectedAttr, actualAttr);
    }

    /**
     * Test constructor with invalid arguments (non-existing NodePointer) and getNext method.
     */
    @Test
    public void testDOMAttributeIteratorInvalidArg() {
        String xml = "<root><element attribute1=\"value1\">test</element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.createTextNode(""));
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertFalse(iterator.hasNext());

        iterator.next(); // consume the initial null NodePointer

        NodePointer currentNode = iterator.getNodePointer();
        assertNull(currentNode.getNode());
    }

    /**
     * Test constructor with valid and invalid arguments (different namespaces) and getNext method.
     */
    @Test
    public void testDOMAttributeIteratorDifferentNamespaces() {
        String xml = "<root xmlns:x1=\"http://namespace1.com\" xmlns:x2=\"http://namespace2.com\"><element x1:attribute1=\"value1\" x2:attribute2=\"value2\"></element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());

        QName name1 = new QName("*", "attribute1");
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent, name1);
        Attr expectedAttr1 = (Attr) doc.getDocumentElement().getAttributes().getNamedItemNS("http://namespace1.com", "attribute1");
        NodePointer nodePointer1 = iterator1.getNodePointer();
        Attr actualAttr1 = (Attr) nodePointer1.getNode();

        assertEquals(expectedAttr1.getValue(), actualAttr1.getValue());
        assertEquals(expectedAttr1, actualAttr1);

        assertFalse(iterator1.hasNext());

        QName name2 = new QName("*", "attribute2");
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent, name2);
        Attr expectedAttr2 = (Attr) doc.getDocumentElement().getAttributes().getNamedItemNS("http://namespace2.com", "attribute2");
        NodePointer nodePointer2 = iterator2.getNodePointer();
        Attr actualAttr2 = (Attr) nodePointer2.getNode();

        assertEquals(expectedAttr2.getValue(), actualAttr2.getValue());
        assertEquals(expectedAttr2, actualAttr2);

        assertTrue(iterator2.hasNext());
    }

    /**
     * Test getNext method with empty list and getPosition method.
     */
    @Test
    public void testDOMAttributeIteratorEmptyList() {
        String xml = "<root><element></element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.getPosition());

        iterator.next(); // consume the initial null NodePointer

        assertFalse(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
    }

    /**
     * Test getNext method with valid arguments and getPosition method.
     */
    @Test
    public void testDOMAttributeIteratorValidArgs() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element attribute1=\"value1\" xsi:id=\"element1\">" +
                "<subElement attribute2=\"value2\" />" +
                "</element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer firstNode = iterator.getNodePointer();
        assertEquals(firstNode.getNode().getNodeName(), "attribute1");
        assertEquals(firstNode.getNode().getValue(), "value1");

        iterator.next();
        NodePointer secondNode = iterator.getNodePointer();
        assertNull(secondNode.getNode());

        iterator.next();
        NodePointer thirdNode = iterator.getNodePointer();
        assertNull(thirdNode.getNode());

        iterator.next();
        NodePointer fourthNode = iterator.getNodePointer();
        assertNull(fourthNode.getNode());

        assertEquals(4, iterator.getPosition());
    }

    /**
     * Test getNext method with valid arguments and getNode method.
     */
    @Test
    public void testDOMAttributeIteratorValidArgsNode() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element attribute1=\"value1\" xsi:id=\"element1\">" +
                "<subElement attribute2=\"value2\" />" +
                "</element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer firstNode = iterator.getNodePointer();
        Node expectedNode = firstNode.getNode();
        Node actualNode = iterator.getNode();

        assertEquals(expectedNode, actualNode);

        iterator.next();
        NodePointer secondNode = iterator.getNodePointer();
        assertNull(secondNode.getNode());

        assertEquals(1, iterator.getPosition());
    }

    /**
     * Test getNext method with valid arguments and getNode method after the end of the list.
     */
    @Test
    public void testDOMAttributeIteratorValidArgsNodeEnd() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element attribute1=\"value1\" xsi:id=\"element1\">" +
                "<subElement attribute2=\"value2\" />" +
                "</element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        iterator.next(); // consume the initial null NodePointer

        NodePointer firstNode = iterator.getNodePointer();
        Node expectedNode = firstNode.getNode();
        Node actualNode = iterator.getNode();

        assertEquals(expectedNode, actualNode);

        iterator.next();
        NodePointer secondNode = iterator.getNodePointer();
        assertNull(secondNode.getNode());

        iterator.next(); // consume the end null NodePointer

        NodePointer thirdNode = iterator.getNodePointer();
        assertNull(thirdNode.getNode());

        assertNull(iterator.getNode());

        assertEquals(3, iterator.getPosition());
    }

    /**
     * Test getNext method with valid arguments and getNode method after the end of the list with
     * different NodePointer.
     */
    @Test
    public void testDOMAttributeIteratorValidArgsNodeEndDifferentPointer() {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><element attribute1=\"value1\" xsi:id=\"element1\">" +
                "<subElement attribute2=\"value2\" />" +
                "</element></root>";

        DOMParser parser = new DOMParser();
        Document doc = null;
        try {
            parser.parseFromString(xml, null);
            doc = parser.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse XML");
        }

        NodePointer parent = new DOMPointer(doc.getDocumentElement());
        QName name = new QName("*", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        iterator.next(); // consume the initial null NodePointer

        NodePointer firstNode = iterator.getNodePointer();
        Node expectedNode = firstNode.getNode();
        Node actualNode = iterator.getNode();

        assertEquals(expectedNode, actualNode);

        iterator.next();
        NodePointer secondNode = iterator.getNodePointer();
        assertNull(secondNode.getNode());

        iterator.next(); // consume the end null NodePointer

        iterator = new DOMAttributeIterator(parent, name);

        NodePointer thirdNode = iterator.getNodePointer();
        assertNull(thirdNode.getNode());

        assertNull(iterator.getNode());

        assertEquals(3, iterator.getPosition());
    }
}