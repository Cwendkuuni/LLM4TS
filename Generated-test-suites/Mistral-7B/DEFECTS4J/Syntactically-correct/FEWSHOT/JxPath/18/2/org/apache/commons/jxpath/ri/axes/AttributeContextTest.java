package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Node;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstruction() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        NodePointer currentNodePointer = Mockito.mock(NodePointer.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(attributeContext.iterator).thenReturn(iterator);

        assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();

        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        NodePointer currentNodePointer = Mockito.mock(NodePointer.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(iterator.setPosition(Mockito.anyInt())).thenReturn(true);
        Mockito.when(attributeContext.iterator).thenReturn(iterator);

        assertTrue(attributeContext.setPosition(1));
        assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());

        Mockito.verify(iterator).setPosition(1);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        NodePointer currentNodePointer = Mockito.mock(NodePointer.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(iterator.setPosition(Mockito.anyInt())).thenReturn(true);
        Mockito.when(iterator.hasNext()).thenReturn(true);
        Mockito.when(iterator.next()).thenReturn(Mockito.mock(Node.class));
        Mockito.when(attributeContext.iterator).thenReturn(iterator);

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());

        Mockito.verify(iterator).setPosition(iterator.getPosition() + 1);
        Mockito.verify(iterator).hasNext();
        Mockito.verify(iterator).next();
    }

    @Test
    public void testNextNodeWhenNoAttributeFound() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(iterator.hasNext()).thenReturn(false);
        Mockito.when(attributeContext.iterator).thenReturn(iterator);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWhenParentNodeHasNoAttribute() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodePointer currentNodePointer = Mockito.mock(NodePointer.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(currentNodePointer);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}