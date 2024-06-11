package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ElementImpl;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private Node rootNode;
    private NodeIterator iterator;

    @Before
    public void setUp() throws Exception {
        Document document = Document.createDocument(null, "test", null);
        Element rootElement = document.getDocumentElement();

        rootElement.setAttribute("test1", "value1");
        rootElement.setAttributeNS("http://example.com", "test2", "value2");
        rootElement.setAttributeNS("http://example.com", "xmlns:x", "http://example.com/x");

        Element childElement = document.createElement("child");
        childElement.setAttribute("test3", "value3");
        childElement.setAttributeNS("http://example.com", "test4", "value4");
        rootElement.appendChild(childElement);

        rootElement.setAttribute("test5", null);

        rootNode = document;
        iterator = new DOMAttributeIterator(new DOMPoint(rootNode), new QName("*"));
    }

    @After
    public void tearDown() {
        if (rootNode != null) {
            rootNode.getOwnerDocument().releaseResource();
        }
    }

    @Test
    public void testDOMAttributeIteratorConstructor() {
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        nodePointer = iterator.next();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        NodePointer nodePointer = iterator.next();
        assertEquals(1, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(2, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(3, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(4, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(5, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(6, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(7, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(8, iterator.getPosition());

        nodePointer = iterator.next();
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(0);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(10);
        assertEquals(-1, iterator.getPosition());

        iterator.setPosition(5);
        assertEquals(5, iterator.getPosition());

        iterator.setPosition(9);
        assertEquals(7, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        iterator.setPosition(10);
    }

    @Test
    public void testNextWhenAtEnd() {
        NodePointer nodePointer = iterator.next();
        assertNull(nodePointer);

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNextWhenNotAtEnd() {
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("test1", nodePointer.getNode().getNodeName());
        assertEquals("value1", nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("xmlns:x", nodePointer.getNode().getNodeName());
        assertNull(nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("test2", nodePointer.getNode().getNodeName());
        assertEquals("value2", nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("test3", nodePointer.getNode().getNodeName());
        assertEquals("value3", nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("test4", nodePointer.getNode().getNodeName());
        assertEquals("value4", nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);

        assertEquals("test5", nodePointer.getNode().getNodeName());
        assertNull(nodePointer.getNode().getNodeValue());

        nodePointer = iterator.next();
        assertNull(nodePointer);
    }

    @Test
    public void testHasNextWhenAtEnd() {
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextWhenNotAtEnd() {
        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextWithEmptyIterator() {
        iterator = new DOMAttributeIterator(new DOMPoint(rootNode), new QName(""));
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyElement() {
        Document document = Document.createDocument(null, "", null);
        Element element = document.createElement("empty");
        iterator = new DOMAttributeIterator(new DOMPoint(element), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDocumentType() {
        Document document = Document.createDocument(null, "test", null);
        DocumentType documentType = document.createDocumentType("root", null, null);
        iterator = new DOMAttributeIterator(new DOMPoint(documentType), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithProcessingInstruction() {
        Document document = Document.createDocument(null, "test", null);
        ProcessingInstruction processingInstruction = document.createProcessingInstruction("xml", "version='1.0'");
        iterator = new DOMAttributeIterator(new DOMPoint(processingInstruction), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithTextNode() {
        Document document = Document.createDocument(null, "test", null);
        Text textNode = document.createTextNode("test text");
        iterator = new DOMAttributeIterator(new DOMPoint(textNode), new QName(""));
        assertFalse(iterator.hasNext());
    }
}