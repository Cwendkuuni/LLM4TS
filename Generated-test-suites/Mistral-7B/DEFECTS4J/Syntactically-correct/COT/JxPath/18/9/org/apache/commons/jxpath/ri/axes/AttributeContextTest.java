package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for AttributeContext
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPositionLessThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testSetPositionGreaterThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.setPosition(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionBeforeFirstNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testNextNodeFalseFirstCall() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeTrueFirstCall() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        AttributeContext spy = org.mockito.Mockito.spy(attributeContext);
        org.mockito.Mockito.doReturn(new NodeIterator() {
            int position = 0;

            @Override
            public NodeIterator clone() {
                return this;
            }

            @Override
            public boolean hasNext() {
                return position < 1;
            }

            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int newPosition) {
                position = newPosition;
                return true;
            }
        }).when(spy).iterator;
        Assert.assertTrue(spy.nextNode());
        Assert.assertNotNull(spy.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeAfterLastNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = NullPointerException.class)
    public void testSetPositionAfterLastNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.setPosition(1));
    }
}