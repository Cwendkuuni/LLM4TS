package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Test;

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
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
        assertNotNull(attributeContext.nodeTest);
    }

    @Test
    public void testSetPositionWhenStarted() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator mockedIterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mockedIterator);
        when(mockedIterator.getNodePointer()).thenReturn(mock(NodePointer.class));

        attributeContext.setPosition(11);
        NodePointer expectedNodePointer = mock(NodePointer.class);
        when(mockedIterator.getNodePointer()).thenReturn(expectedNodePointer);
        attributeContext.nextNode();
        assertNotNull(attributeContext.iterator);
        assertNotNull(attributeContext.currentNodePointer);
        assertSame(expectedNodePointer, attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNodeWhenStarted() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator mockedIterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mockedIterator);
        when(mockedIterator.getNodePointer()).thenReturn(mock(NodePointer.class));

        attributeContext.nextNode();
        NodePointer expectedNodePointer = mock(NodePointer.class);
        when(mockedIterator.getNodePointer()).thenReturn(expectedNodePointer);
        attributeContext.nextNode();
        assertNotNull(attributeContext.iterator);
        assertNotNull(attributeContext.currentNodePointer);
        assertSame(expectedNodePointer, attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNodeWhenEndOfIterator() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator mockedIterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mockedIterator);
        when(mockedIterator.getNodePointer()).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class))
                .thenReturn(mock(NodePointer.class)).thenReturn(mock(NodePointer.class)).thenReturn(null);

        attributeContext.nextNode();
        attributeContext.nextNode();
        assertNull(attributeContext.currentNodePointer);
    }
}