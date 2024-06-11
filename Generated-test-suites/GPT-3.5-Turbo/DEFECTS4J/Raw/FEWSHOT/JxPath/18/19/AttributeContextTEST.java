package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid arguments
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());

        // Test constructor with null parentContext
        attributeContext = new AttributeContext(null, nodeTest);
        assertNull(attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());

        // Test constructor with null nodeTest
        attributeContext = new AttributeContext(parentContext, null);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertNull(attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext();
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        NodeTest nodeTest = new NodeNameTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.currentNodePointer = new NodePointer();

        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext();
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);
        parentContext.setCurrentPosition(3);

        NodeTest nodeTest = new NodeNameTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Test setPosition with position less than current position
        assertTrue(attributeContext.setPosition(2));
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
        assertEquals(2, attributeContext.getCurrentPosition());

        // Test setPosition with position greater than current position
        assertTrue(attributeContext.setPosition(5));
        assertTrue(attributeContext.setStarted);
        assertTrue(attributeContext.iterator instanceof NodeIterator);
        assertTrue(attributeContext.currentNodePointer instanceof NodePointer);
        assertEquals(5, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext();
        NodePointer nodePointer = new NodePointer();
        parentContext.setCurrentNodePointer(nodePointer);

        NodeTest nodeTest = new NodeNameTest();
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Test nextNode before setStarted is true
        assertFalse(attributeContext.nextNode());

        // Test nextNode with null iterator
        attributeContext.setStarted = true;
        assertFalse(attributeContext.nextNode());

        // Test nextNode with valid iterator
        attributeContext.iterator = new NodeIterator();
        assertFalse(attributeContext.nextNode());

        // Test nextNode with valid iterator and setPosition
        attributeContext.setPosition(2);
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.currentNodePointer instanceof NodePointer);
        assertEquals(2, attributeContext.getCurrentPosition());
    }

}