package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
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
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext.getNodeTest());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, actual);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), position);
            }

            @Override
            public boolean setPosition(int newPosition) {
                position = newPosition;
                return true;
            }

            @Override
            public boolean hasNext() {
                return position < 5;
            }

            @Override
            public boolean hasPrevious() {
                return position > 0;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.setPosition(1);
        NodePointer expected = new NodePointer(new Object(), 1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionNegative() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionGreater() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.setPosition(10);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), position);
            }

            @Override
            public boolean setPosition(int newPosition) {
                position = newPosition;
                return true;
            }

            @Override
            public boolean hasNext() {
                return position < 5;
            }

            @Override
            public boolean hasPrevious() {
                return position > 0;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.nextNode();
        NodePointer expected = new NodePointer(new Object(), 1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextNodeNoMoreNodes() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), position);
            }

            @Override
            public boolean setPosition(int newPosition) {
                position = newPosition;
                return true;
            }

            @Override
            public boolean hasNext() {
                return position < 5;
            }

            @Override
            public boolean hasPrevious() {
                return position > 0;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.setCurrentNodePointer(new NodePointer(new Object(), 4));
        attributeContext.nextNode();
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }
}