package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = mock(NodePointer.class);
        attributeContext.currentNodePointer = currentNodePointer;

        NodePointer result = attributeContext.getCurrentNodePointer();

        assertEquals(currentNodePointer, result);
    }

    @Test
    public void testReset() {
        attributeContext.setStarted = true;
        attributeContext.iterator = mock(NodeIterator.class);

        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        verify(parentContext).reset();
    }

    @Test
    public void testSetPosition_lowerThanCurrentPosition_resetsContext() {
        attributeContext.currentPosition = 5;

        boolean result = attributeContext.setPosition(3);

        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
        verify(attributeContext).reset();
    }

    @Test
    public void testSetPosition_greaterThanCurrentPosition_callsNextNodeUntilPositionReached() {
        attributeContext.currentPosition = 5;
        when(attributeContext.nextNode()).thenReturn(true);

        boolean result = attributeContext.setPosition(8);

        assertTrue(result);
        assertEquals(8, attributeContext.getCurrentPosition());
        verify(attributeContext, times(3)).nextNode();
    }

    @Test
    public void testNextNode_setStartedFalse_returnsFalse() {
        attributeContext.setStarted = false;

        boolean result = attributeContext.nextNode();

        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_nodeTestNotNodeNameTest_returnsFalse() {
        attributeContext.setStarted = true;
        attributeContext.nodeTest = mock(NodeTest.class);

        boolean result = attributeContext.nextNode();

        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_iteratorNull_returnsFalse() {
        attributeContext.setStarted = true;
        attributeContext.nodeTest = mock(NodeNameTest.class);

        boolean result = attributeContext.nextNode();

        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_iteratorSetPositionFalse_returnsFalse() {
        attributeContext.setStarted = true;
        attributeContext.nodeTest = mock(NodeNameTest.class);
        NodeNameTest nodeNameTest = (NodeNameTest) attributeContext.nodeTest;
        nodeNameTest.nodeName = mock(QName.class);
        attributeContext.iterator = mock(NodeIterator.class);
        when(attributeContext.iterator.setPosition(anyInt())).thenReturn(false);

        boolean result = attributeContext.nextNode();

        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_positionIncrementedAndCurrentNodePointerSet_returnsTrue() {
        attributeContext.setStarted = true;
        attributeContext.nodeTest = mock(NodeNameTest.class);
        NodeNameTest nodeNameTest = (NodeNameTest) attributeContext.nodeTest;
        nodeNameTest.nodeName = mock(QName.class);
        attributeContext.iterator = mock(NodeIterator.class);
        when(attributeContext.iterator.setPosition(anyInt())).thenReturn(true);
        NodePointer currentNodePointer = mock(NodePointer.class);
        when(attributeContext.iterator.getNodePointer()).thenReturn(currentNodePointer);

        boolean result = attributeContext.nextNode();

        assertTrue(result);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(currentNodePointer, attributeContext.currentNodePointer);
    }
}