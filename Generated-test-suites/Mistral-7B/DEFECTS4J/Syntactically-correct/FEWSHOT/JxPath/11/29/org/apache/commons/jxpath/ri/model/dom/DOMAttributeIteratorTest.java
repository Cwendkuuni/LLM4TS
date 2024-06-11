package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer parentPointer;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = createTestElement();
        parentPointer = new NodePointer(createTestDocument(), element);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, null);
        assertNull(iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        String testAttributeName = "data-test";
        Attr testAttribute = createTestAttribute(testAttributeName);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, testAttributeName));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        assertNull(iterator.getNodePointer()); // Moves to next position
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        String testAttribute1Name = "data-test1";
        String testAttribute2Name = "data-test2";
        Attr testAttribute1 = createTestAttribute(testAttribute1Name);
        Attr testAttribute2 = createTestAttribute(testAttribute2Name);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute1);
        element.setAttributeNode(testAttribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, "*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithNamespacedAttribute() {
        String testNamespace = "http://test.com";
        String testAttributeName = "test";
        Attr testAttribute = createTestAttribute(new QName(testNamespace, testAttributeName));
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNodeNS(testAttribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(testNamespace, testAttributeName));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        assertNull(iterator.getNodePointer()); // Moves to next position
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        new DOMAttributeIterator(null, new QName(null, "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        new DOMAttributeIterator(parentPointer, null);
    }

    @Test
    public void testMoveToNextAttribute() {
        String testAttribute1Name = "data-test1";
        String testAttribute2Name = "data-test2";
        Attr testAttribute1 = createTestAttribute(testAttribute1Name);
        Attr testAttribute2 = createTestAttribute(testAttribute2Name);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute1);
        element.setAttributeNode(testAttribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, "*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testMoveToPreviousAttribute() {
        String testAttribute1Name = "data-test1";
        String testAttribute2Name = "data-test2";
        Attr testAttribute1 = createTestAttribute(testAttribute1Name);
        Attr testAttribute2 = createTestAttribute(testAttribute2Name);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute1);
        element.setAttributeNode(testAttribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, "*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
        iterator.previous();
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testMoveToFirstAttribute() {
        String testAttribute1Name = "data-test1";
        String testAttribute2Name = "data-test2";
        Attr testAttribute1 = createTestAttribute(testAttribute1Name);
        Attr testAttribute2 = createTestAttribute(testAttribute2Name);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute1);
        element.setAttributeNode(testAttribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, "*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.previous();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(0);
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testMoveToLastAttribute() {
        String testAttribute1Name = "data-test1";
        String testAttribute2Name = "data-test2";
        Attr testAttribute1 = createTestAttribute(testAttribute1Name);
        Attr testAttribute2 = createTestAttribute(testAttribute2Name);
        Element element = (Element) parentPointer.getNode();
        element.setAttributeNode(testAttribute1);
        element.setAttributeNode(testAttribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName(null, "*"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(testAttribute1, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(1);
        iterator.next();
        assertNull(iterator.getNodePointer());
        iterator.setPosition(2);
        iterator.previous();
        assertEquals(testAttribute2, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());
    }

    private Node createTestDocument() {
        Document document = new DocumentImpl();
        document.setNodeName("document");
        document.setNodeValue("");
        return document;
    }

    private Element createTestElement() {
        Element element = document.createProcessingInstruction("xml", "version=\"1.0\"");
        element.setNodeName("test");
        return element;
    }

    private Attr createTestAttribute(String attributeName) {
        Element element = document.createProcessingInstruction("xml", "version=\"1.0\"");
        Attr attribute = document.createAttribute(attributeName);
        element.setAttributeNode(attribute);
        return attribute;
    }
}