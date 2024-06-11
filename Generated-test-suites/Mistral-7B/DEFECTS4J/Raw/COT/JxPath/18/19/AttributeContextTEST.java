package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        NodePointer nodePointer = new NodePointer(null);
        attributeContext.setCurrentNodePointer(nodePointer);
        Assert.assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(null);
        attributeContext.setCurrentNodePointer(nodePointer);
        AttributeContext newAttributeContext = new AttributeContext(parentContext, nodeTest);
        newAttributeContext.setPosition(1);
        Assert.assertFalse(newAttributeContext.nextNode());
        Assert.assertFalse(newAttributeContext.nextNode());
        Assert.assertNull(newAttributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        attributeContext.setPosition(1);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionNegative() {
        attributeContext.setPosition(-1);
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer1 = new NodePointer(null);
        NodePointer nodePointer2 = new NodePointer(null);
        attributeContext.setCurrentNodePointer(nodePointer1);
        attributeContext.nextNode();
        attributeContext.setCurrentNodePointer(nodePointer2);
        Assert.assertFalse(attributeContext.nextNode());

        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

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
            public NodePointer getNodePointer() {
                return new NodePointer(new Object());
            }
        };
        attributeContext.iterator = iterator;
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(attributeContext.nextNode());
        }
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWithoutSetStarted() {
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWithEmptyIterator() {
        attributeContext.iterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        Assert.assertFalse(attributeContext.nextNode());
    }
}