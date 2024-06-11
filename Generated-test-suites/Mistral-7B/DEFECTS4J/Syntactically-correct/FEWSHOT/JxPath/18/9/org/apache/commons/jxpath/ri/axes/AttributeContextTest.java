package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.mockito.Mockito;

public class AttributeContextTest {

    @SuppressWarnings("unchecked")
    private EvalContext parentContextMock;
    private NodeTest nodeTestMock;
    private AttributeContext attributeContext;
    private NodeIterator iteratorMock;
    private NodePointer currentNodePointerMock;

    @before
    public void setUp() {
        parentContextMock = Mockito.mock(EvalContext.class);
        nodeTestMock = Mockito.mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContextMock, nodeTestMock);
        iteratorMock = Mockito.mock(NodeIterator.class);
        currentNodePointerMock = Mockito.mock(NodePointer.class);
        Mockito.when(parentContextMock.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(iteratorMock.getNodePointer()).thenReturn(currentNodePointerMock);
        Mockito.when(iteratorMock.hasNext()).thenReturn(true);
        Mockito.when(iteratorMock.next()).thenReturn(currentNodePointerMock);
    }

    @Test
    public void testAttributeContextConstruction() {
        assertEquals(parentContextMock, attributeContext.getParentContext());
        assertEquals(nodeTestMock, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        Mockito.when(iteratorMock.hasNext()).thenReturn(true);
        attributeContext.iterator = iteratorMock;
        assertEquals(currentNodePointerMock, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        Mockito.when(iteratorMock.hasNext()).thenReturn(true);
        attributeContext.iterator = iteratorMock;
        attributeContext.getCurrentNodePointer();

        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        Mockito.when(iteratorMock.hasNext()).thenReturn(true);
        attributeContext.iterator = iteratorMock;

        attributeContext.setPosition(5);

        verify(iteratorMock).setPosition(5);
        assertEquals(currentNodePointerMock, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        Mockito.when(iteratorMock.hasNext()).thenReturn(true);
        attributeContext.iterator = iteratorMock;

        attributeContext.nextNode();

        verify(iteratorMock).next();
        assertEquals(currentNodePointerMock, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_whenIteratorHasNoNextNode() {
        Mockito.when(iteratorMock.hasNext()).thenReturn(false);
        attributeContext.iterator = iteratorMock;

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}