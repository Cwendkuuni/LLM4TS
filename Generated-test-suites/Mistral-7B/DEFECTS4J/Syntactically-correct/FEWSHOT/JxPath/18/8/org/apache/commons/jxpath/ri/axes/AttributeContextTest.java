package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstruction() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertNotNull(attributeContext.getNodeTest());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer expectedNodePointer = mock(NodePointer.class);
        when(attributeContext.iterator.getNodePointer()).thenReturn(expectedNodePointer);

        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertNotNull(actualNodePointer);
        assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.iterator = mock(NodeIterator.class);

        attributeContext.reset();

        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodeIterator iterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.setPosition(0)).thenReturn(true);

        attributeContext.reset();
        assertTrue(attributeContext.setPosition(0));
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodeIterator iterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.setPosition(0)).thenReturn(true);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));

        attributeContext.reset();
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_noMoreElements() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodeIterator iterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName())).thenReturn(iterator);
        when(iterator.setPosition(0)).thenReturn(true);
        when(iterator.getNodePointer()).thenReturn(null);

        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_noAttributeIterator() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPosition_invalidPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();
        attributeContext.setPosition(-1);
    }
}