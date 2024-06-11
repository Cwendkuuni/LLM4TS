package org.apache.commons.jxpath.ri.axes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.apache.commons.jxpath.ri.model.NodeIterator;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, new NodeNameTest("test"));
        nodePointer = new NodePointer();
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(1);
        attributeContext.reset();
        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setPosition(2));
        assertEquals(2, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setPosition(3));
        assertEquals(3, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        attributeContext.setPosition(1);
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

}