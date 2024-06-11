package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextCreation() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertTrue(attributeContext instanceof AttributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodeIterator iterator = new NodeIterator(new Object());
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());
        assertTrue(attributeContext.getCurrentNodePointer() == attributeContext.currentNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.iterator = new NodeIterator(new Object());
        attributeContext.currentNodePointer = new NodePointer(new Object());

        attributeContext.reset();

        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
        assertNull(parentContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);

        attributeContext.setPosition(0);
        assertNull(attributeContext.getCurrentNodePointer());

        NodeIterator iterator = new NodeIterator(new Object());
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        attributeContext.setPosition(1);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        assertTrue(attributeContext.getCurrentNodePointer() == expectedNodePointer);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);

        NodeIterator iterator = new NodeIterator(new Object());
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        iterator.setPosition(0);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        assertTrue(attributeContext.nextNode() && attributeContext.getCurrentNodePointer() == expectedNodePointer);

        iterator.setPosition(1);
        expectedNodePointer = new NodePointer(new Object());
        assertTrue(attributeContext.nextNode() && attributeContext.getCurrentNodePointer() == expectedNodePointer);

        iterator.setPosition(2);
        assertNull(attributeContext.nextNode());
    }
}