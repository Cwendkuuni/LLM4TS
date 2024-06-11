package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    @org.junit.Test
    public void testAttributeContextConstruction() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertFalse(attributeContext.isSetStarted());
    }

    @org.junit.Test
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertFalse(attributeContext.isSetStarted());
    }

    @org.junit.Test
    public void testSetPosition_PositionLessThanCurrentPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        int position = 1;
        assertFalse(attributeContext.setPosition(position));
        assertSame(attributeContext, attributeContext);
    }

    @org.junit.Test
    public void testSetPosition_PositionGreaterThanCurrentPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        int currentPosition = 2;
        int position = 4;
        when(parentContext.getCurrentPosition()).thenReturn(currentPosition);
        NodePointer nodePointer = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        NodeIterator iterator = mock(NodeIterator.class);
        when(nodePointer.attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.getNodePointer()).thenReturn(nodePointer);
        when(iterator.getPosition()).thenReturn(2);
        when(iterator.setPosition(3)).thenReturn(true);
        attributeContext.setPosition(position);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
        assertTrue(attributeContext.isSetStarted());
    }

    @org.junit.Test
    public void testNextNode_NoIterators() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
        assertSame(attributeContext, attributeContext);
    }

    @org.junit.Test
    public void testNextNode_NodeIteratorNull() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        assertFalse(attributeContext.nextNode());
        assertSame(attributeContext, attributeContext);
    }

    @org.junit.Test
    public void testNextNode_NodeIteratorHasNextNode() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        NodeIterator iterator = mock(NodeIterator.class);
        when(nodePointer.attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.getNodePointer()).thenReturn(nodePointer);
        when(iterator.getPosition()).thenReturn(0);
        when(iterator.setPosition(1)).thenReturn(true);
        attributeContext.nextNode();
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
        assertTrue(attributeContext.isSetStarted());
    }

    @org.junit.Test
    public void testNextNode_NodeIteratorNoMoreNodes() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        NodeIterator iterator = mock(NodeIterator.class);
        when(nodePointer.attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.getNodePointer()).thenReturn(nodePointer);
        when(iterator.getPosition()).thenReturn(1);
        when(iterator.setPosition(2)).thenReturn(false);
        assertFalse(attributeContext.nextNode());
        assertSame(attributeContext, attributeContext);
    }
}