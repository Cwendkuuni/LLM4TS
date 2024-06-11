package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito. Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeNameTest.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("attrName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertSame(parentContext, attributeContext.getParentContext());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertFalse(attributeContext.isSetStarted());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer nodePointer = mock(NodePointer.class);
        when(iterator.getNodePointer()).thenReturn(nodePointer);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(nodePointer);

        attributeContext = new AttributeContext(parentContext, nodeTest);
        nextNode();

        NodePointer result = attributeContext.getCurrentNodePointer();
        assertSame(nodePointer, result);
    }

    @Test
    public void testReset() {
        nextNode();
        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        nextNode();
        nextNode();

        boolean result = attributeContext.setPosition(1);
        assertTrue(result);

        NodePointer nodePointer = mock(NodePointer.class);
        when(iterator.getNodePointer()).thenReturn(nodePointer);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(nodePointer);

        result = attributeContext.setPosition(3);
        assertFalse(result);
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);

        when(iterator.getNodePointer()).thenReturn(nodePointer1);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(nodePointer2);

        nextNode();
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertSame(nodePointer2, result);
    }

    private void nextNode() {
        attributeContext.nextNode();
    }
}