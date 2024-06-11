package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        Document document = DocumentBuilderFactory.newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        root.setAttribute("test", "testValue");
        root.setAttribute("anotherTest", "anotherTestValue");
        root.setAttribute("nullTest", null);
        document.appendChild(root);
        parentContext = new EvalContext(document.getDocumentElement());
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    /**
     * Test nextNode method with valid attribute name.
     */
    @Test
    public void testNextNodeWithValidAttribute() {
        assertTrue(attributeContext.nextNode());
        NodePointer expectedNodePointer = new NodePointer(root, root.getAttributeNode("test"));
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    /**
     * Test nextNode method with invalid attribute name.
     */
    @Test
    public void testNextNodeWithInvalidAttribute() {
        nodeTest = new NodeNameTest(new QName("nonExistentTest"));
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test setPosition method with valid position.
     */
    @Test
    public void testSetPositionWithValidPosition() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        attributeContext.setPosition(2);
        assertTrue(attributeContext.nextNode());
        assertEquals(root.getAttributeNode("test"), attributeContext.getCurrentNodePointer().getNode());
    }

    /**
     * Test setPosition method with invalid position.
     */
    @Test
    public void testSetPositionWithInvalidPosition() {
        attributeContext.setPosition(-1);
        assertFalse(attributeContext.nextNode());
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    /**
     * Test nextNode method after reset.
     */
    @Test
    public void testNextNodeAfterReset() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test nextNode method when there are no more nodes.
     */
    @Test
    public void testNextNodeWhenNoMoreNodes() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}