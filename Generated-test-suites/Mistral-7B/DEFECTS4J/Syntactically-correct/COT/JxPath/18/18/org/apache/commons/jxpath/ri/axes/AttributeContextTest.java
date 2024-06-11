package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for AttributeContext class.
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        attributeContext.currentNodePointer = expectedNodePointer;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
        Assert.assertTrue(super.isEmptyExpressionContext(attributeContext));
    }

    @Test
    public void testSetPositionWhenPositionIsLessThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.setPosition(1));
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionWhenPositionIsGreaterThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        int currentPosition = 3;
        Assert.assertTrue(attributeContext.setPosition(currentPosition));
        NodePointer expectedNodePointer = new NodePointer(new Object());
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodeIterator nodeIterator() {
                return this;
            }

            @Override
            public NodePointer getNodePointer() {
                return expectedNodePointer;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean setPosition(int position) {
                return position == 0;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNodePointer() {
                return expectedNodePointer;
            }

            @Override
            public NodePointer previousNodePointer() {
                return null;
            }
        };
        attributeContext.iterator = iterator;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWhenNodeTestIsNotInstanceOfNodeNameTest() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeTest() {};
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWhenIteratorIsNull() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.iterator.hasNext());
    }

    @Test
    public void testNextNodeWhenIteratorHasNoNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodeIterator nodeIterator() {
                return this;
            }

            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean setPosition(int position) {
                return position == 0;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodePointer nextNodePointer() {
                return null;
            }

            @Override
            public NodePointer previousNodePointer() {
                return null;
            }
        };
        attributeContext.iterator = iterator;
        assert (!attributeContext.nextNode());
    }
}