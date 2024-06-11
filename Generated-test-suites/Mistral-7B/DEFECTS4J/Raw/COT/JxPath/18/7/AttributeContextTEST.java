package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertSame(nodeTest, attributeContext.getNodeTest());
    }

    /**
     * Test getCurrentNodePointer method when an attribute is found.
     */
    @Test
    public void testGetCurrentNodePointer_AttributeFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(new AttributeIteratorMock());
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, nodeTest);
        Assert.assertSame(nodePointer, attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test getCurrentNodePointer method when an attribute is not found.
     */
    @Test
    public void testGetCurrentNodePointer_AttributeNotFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(null);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, nodeTest);
        Assert.assertNull(attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.getIterator());
    }

    /**
     * Test setPosition method when position is greater than current position.
     */
    @Test
    public void testSetPosition_PositionGreaterThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(2);
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.getIterator());
    }

    /**
     * Test setPosition method when position is equal to current position.
     */
    @Test
    public void testSetPosition_PositionEqualToCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(0);
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.getIterator());
    }

    /**
     * Test setPosition method when position is less than current position and an attribute is found.
     */
    @Test
    public void testSetPosition_PositionLessThanCurrentPosition_AttributeFound() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(1);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, null);
        NodePointer nodePointer = mock(NodePointer.class);
        when(attributeContextTest.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(new AttributeIteratorMock());
        when(attributeContextTest.nextNode()).thenReturn(true);
        Assert.assertTrue(attributeContextTest.nextNode());
        Assert.assertSame(nodePointer, attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test setPosition method when position is less than current position and no attribute is found.
     */
    @Test
    public void testSetPosition_PositionLessThanCurrentPosition_NoAttributeFound() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(1);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, null);
        NodePointer nodePointer = mock(NodePointer.class);
        when(attributeContextTest.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(null);
        Assert.assertFalse(attributeContextTest.nextNode());
        Assert.assertNull(attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test nextNode method when an attribute is found.
     */
    @Test
    public void testNextNode_AttributeFound() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer nodePointer = mock(NodePointer.class);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, null);
        when(attributeContextTest.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(new AttributeIteratorMock());
        when(attributeContextTest.nextNode()).thenReturn(true);
        Assert.assertTrue(attributeContextTest.nextNode());
        Assert.assertSame(nodePointer, attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test nextNode method when no attribute is found.
     */
    @Test
    public void testNextNode_NoAttributeFound() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer nodePointer = mock(NodePointer.class);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, null);
        when(attributeContextTest.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getAttributeIterator(new QName("test"))).thenReturn(null);
        Assert.assertFalse(attributeContextTest.nextNode());
        Assert.assertNull(attributeContextTest.getCurrentNodePointer());
    }

    /**
     * Test nextNode method when an iterator is not set.
     */
    @Test(expected = IllegalStateException.class)
    public void testNextNode_NoIteratorSet() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        AttributeContext attributeContextTest = new AttributeContext(attributeContext, null);
        attributeContextTest.nextNode();
    }

    /**
     * Mock AttributeIterator.
     */
    private static class AttributeIteratorMock implements NodeIterator {
        private int position = 0;

        @Override
        public boolean hasNext() {
            return position < 3;
        }

        @Override
        public NodePointer nextNode() {
            return new NodePointerMock();
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public void setPosition(int position) {
            this.position = position;
        }
    }

    /**
     * Mock NodePointer.
     */
    private static class NodePointerMock implements NodePointer {
        @Override
        public NodeIterator attributeIterator(QName name) {
            return new AttributeIteratorMock();
        }
    }
}