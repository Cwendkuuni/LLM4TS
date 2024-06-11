package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for AttributeContext class
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertFalse(attributeContext.setStarted);
    }

    @Test
    public void testgetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.currentNodePointer = nodePointer;
        Assert.assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), 0);
            }

            @Override
            public NodePointer nextNodePointer() {
                return new NodePointer(new Object(), 1);
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
        attributeContext.currentNodePointer = new NodePointer(new Object(), 0);
        attributeContext.setStarted = true;
        attributeContext.reset();
        Assert.assertNull(attributeContext.iterator);
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), 0);
            }

            @Override
            public NodePointer nextNodePointer() {
                return new NodePointer(new Object(), 1);
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object(), 0);
        Assert.assertTrue(attributeContext.setPosition(0));
        Assert.assertEquals(attributeContext.getCurrentNodePointer(), attributeContext.getCurrentNodePointer());
        Assert.assertTrue(attributeContext.setPosition(1));
        Assert.assertNotEquals(attributeContext.getCurrentNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWhenNotStarted() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWhenIteratorHasNoNext() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), 0);
            }

            @Override
            public NodePointer nextNodePointer() {
                return new NodePointer(new Object(), 1);
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object(), 0);
        attributeContext.setStarted = true;
        attributeContext.nextNode();
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), 0);
            }

            @Override
            public NodePointer nextNodePointer() {
                return new NodePointer(new Object(), 1);
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean hasNext() {
                return true;
            }
        };
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = new NodePointer(new Object(), 0);
        attributeContext.setStarted = true;
        attributeContext.nextNode();
        Assert.assertNotEquals(attributeContext.getCurrentNodePointer(), attributeContext.getCurrentNodePointer());
    }
}