package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Unit tests for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(2);
        assertNull(attributeContext.getCurrentNodePointer());

        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(1);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        parentContext.setCurrentNodePointer(nodePointer);
        parentContext.setCurrentPosition(0);
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
        parentContext.setCurrentPosition(1);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithIterator() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        NodeIterator iterator = new NodeIterator(new Object[]{new Object(), new Object()}, 1, 1);
        parentContext.setCurrentNodePointer(nodePointer);
        parentContext.setCurrentPosition(0);
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
        parentContext.setCurrentPosition(1);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.iterator = iterator;
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithIterator() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        NodeIterator iterator = new NodeIterator(new Object[]{new Object(), new Object()}, 1, 1);
        parentContext.setCurrentNodePointer(nodePointer);
        parentContext.setCurrentPosition(0);
        attributeContext.iterator = iterator;
        attributeContext.setPosition(1);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }
}