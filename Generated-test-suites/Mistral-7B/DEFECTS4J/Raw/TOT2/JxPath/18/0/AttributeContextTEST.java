package org.apache.commons.jxpath.ri.axes.test;

import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {
    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContextMock = mock(EvalContext.class);
        NodeTest nodeTestMock = mock(NodeTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContextMock, nodeTestMock);
        verify(parentContextMock).getInstance(); // parentContext should call getInstance()
    }

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer nodePointerMock = mock(NodePointer.class);
        when(attributeContext.getCurrentNodePointer()).thenReturn(nodePointerMock);
        assertSame(nodePointerMock, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.reset();
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPositionValidPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer nodePointerMock = mock(NodePointer.class);
        when(attributeContext.nextNode()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(attributeContext.getCurrentNodePointer()).thenReturn(nodePointerMock);
        when(nodePointerMock.getPosition()).thenReturn(1);
        assertTrue(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPositionInvalidPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testNextNodeValidArgument() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer currentNodePointerMock = mock(NodePointer.class);
        NodeIterator iteratorMock = mock(NodeIterator.class);
        when(attributeContext.getCurrentNodePointer()).thenReturn(currentNodePointerMock);
        when(iteratorMock.getNodePointer()).thenReturn(currentNodePointerMock);
        when(iteratorMock.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(attributeContext.iterator).thenReturn(iteratorMock);
        when(currentNodePointerMock.getNodeName()).thenReturn(new QName("test"));

        attributeContext.nodeTest = new NodeNameTest(new QName("test"));
        assertTrue(attributeContext.nextNode());
        assertSame(currentNodePointerMock, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeInvalidArgument() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertFalse(attributeContext.nextNode());
    }
}