package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    private Document document;
    private Element rootElement;

    @Before
    public void setUp() {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        rootElement = document.createElement("root");
        document.appendChild(rootElement);

        rootElement.setAttribute("test1", "testValue1");
        rootElement.setAttribute("test2", null);
        rootElement.setAttribute("test3", "testValue3");
    }

    @Test
    public void testNextNodeWithEmptyContext() {
        // Given: empty context
        AttributeContext context = new AttributeContext(null, new NodeNameTest("test1"));

        // When: nextNode call
        assertFalse(context.nextNode());

        // Then: no node was returned
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithValidContext() {
        // Given: context with valid attribute node
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test1"));

        // When: nextNode call
        assertTrue(context.nextNode());

        // Then: attribute node was returned
        Attr attributeNode = (Attr) context.getCurrentNodePointer().getNode();
        assertEquals("test1", attributeNode.getNodeName());
        assertEquals("testValue1", attributeNode.getNodeValue());
    }

    @Test
    public void testNextNodeWithInvalidContext() {
        // Given: context with no attribute nodes
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test4"));

        // When: nextNode call
        assertFalse(context.nextNode());

        // Then: no node was returned
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithInvalidAttributeName() {
        // Given: context with no attribute nodes for given name
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test5"));

        // When: nextNode call
        assertFalse(context.nextNode());

        // Then: no node was returned
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        // Given: context with one attribute node
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test1"));
        context.nextNode();

        // When: setPosition call with valid position
        assertTrue(context.setPosition(1));

        // Then: first attribute node should be returned
        Attr attributeNode = (Attr) context.getCurrentNodePointer().getNode();
        assertEquals("test1", attributeNode.getNodeName());
        assertEquals("testValue1", attributeNode.getNodeValue());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        // Given: context with one attribute node
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test1"));
        context.nextNode();

        // When: setPosition call with invalid position
        assertFalse(context.setPosition(2));

        // Then: no node was returned
        assertNull(context.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithEndPosition() {
        // Given: context with three attribute nodes
        AttributeContext context = new AttributeContext(new EvalContext(document, rootElement), new NodeNameTest("test1"));
        context.nextNode();
        context.nextNode();

        // When: setPosition call with end position
        assertTrue(context.setPosition(3));

        // Then: no node was returned
        assertNull(context.getCurrentNodePointer());
    }
}