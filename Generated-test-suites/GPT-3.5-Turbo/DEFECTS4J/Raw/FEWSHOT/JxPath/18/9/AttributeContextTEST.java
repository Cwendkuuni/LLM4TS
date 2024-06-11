package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.RIContextTestCase;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest extends RIContextTestCase {
    private AttributeContext attributeContext;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        NodeTest nodeTest = new NodeNameTest(null, "attributeName");
        attributeContext = new AttributeContext(context, nodeTest);
        nodePointer = createNodePointer();
        attributeContext.setParentContext(context);
        attributeContext.setCurrentNodePointer(nodePointer);
    }

    private NodePointer createNodePointer() {
        // Create and return a new NodePointer object
        return new NodePointer();
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertNotNull(attributeContext);
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setPosition(2));
        assertEquals(2, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setPosition(3));
        assertEquals(2, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        assertTrue(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
    }
}