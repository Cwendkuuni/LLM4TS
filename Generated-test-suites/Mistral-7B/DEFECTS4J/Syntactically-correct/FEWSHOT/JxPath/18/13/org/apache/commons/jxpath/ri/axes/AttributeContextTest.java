package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorImpl;
import org.apache.commons.jxpath.ri.model.NodePointerImpl;

import java.util.ArrayList;
import java.util.List;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertFalse(attributeContext.isSetStarted());
        assertFalse(attributeContext.hasNextNode());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(expectedNodePointer);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = expectedNodePointer;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);

        int position = 1;
        boolean expectedResult = true;
        attributeContext.iterator = iterator;
        when(iterator.setPosition(iterator.getPosition() + 1)).thenReturn(true);
        boolean actualResult = attributeContext.setPosition(position);
        assertTrue(actualResult);

        position = 5;
        expectedResult = false;
        when(iterator.setPosition(iterator.getPosition() + 1)).thenReturn(false);
        actualResult = attributeContext.setPosition(position);
        assertFalse(actualResult);

        position = 0;
        expectedResult = false;
        attributeContext.reset();
        actualResult = attributeContext.setPosition(position);
        assertFalse(actualResult);
    }

    @Test
    public void testNextNode() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = mock(NodeIterator.class);
        NodePointer expectedNodePointer = mock(NodePointer.class);
        when(iterator.getNodePointer()).thenReturn(expectedNodePointer);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        attributeContext.iterator = iterator;

        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertSame(expectedNodePointer, actualNodePointer);
        attributeContext.nextNode();
        actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(actualNodePointer);

        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(new NodeIteratorImpl(new ArrayList<>()));
        attributeContext.nextNode();
        actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(actualNodePointer);
    }

    @Test
    public void testHasNextNode() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(iterator.hasNext()).thenReturn(true);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        attributeContext.iterator = iterator;

        boolean expectedResult = true;
        boolean actualResult = attributeContext.hasNextNode();
        assertTrue(actualResult);

        when(iterator.hasNext()).thenReturn(false);
        actualResult = attributeContext.hasNextNode();
        assertFalse(actualResult);

        attributeContext.reset();
        actualResult = attributeContext.hasNextNode();
        assertFalse(actualResult);
    }
}