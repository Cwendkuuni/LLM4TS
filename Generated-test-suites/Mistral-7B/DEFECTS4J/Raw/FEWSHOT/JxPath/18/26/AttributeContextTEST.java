package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("test:attr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testEmptyContext() {
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithNoAttribute() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        parentContext.setCurrentNodePointer(nodePointer);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithSingleAttribute() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithMultipleAttributes() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr1", "test:attr2"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithNoAttribute() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        parentContext.setCurrentNodePointer(nodePointer);
        assertFalse(attributeContext.setPosition(0));
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithSingleAttribute() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(0));
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithMultipleAttributes() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr1", "test:attr2"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(1));
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionOutOfRange() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertFalse(attributeContext.setPosition(2));
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        nodePointer.setAttributes(new Object[]{"test:attr"});
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}