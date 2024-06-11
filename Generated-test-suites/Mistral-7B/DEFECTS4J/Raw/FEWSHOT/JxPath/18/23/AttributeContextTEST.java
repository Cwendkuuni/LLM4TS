package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testAttributeContextConstructor() {
        assertEquals("AttributeContext constructor should set parent context correctly", parentContext, attributeContext.getParentContext());
        assertEquals("AttributeContext constructor should set node test correctly", nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse("Reset should reset iterator", attributeContext.hasNextNode());
        assertNull("Reset should set currentNodePointer to null", attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWhenPositionIsGreaterThanCurrentPosition() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(mock(NodeIterator.class));

        attributeContext.setPosition(1);
        assertTrue("Set position should set position correctly", attributeContext.hasNextNode());
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull("Set position should set currentNodePointer", currentNodePointer);
    }

    @Test
    public void testSetPositionWhenPositionIsLessThanCurrentPosition() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(mock(NodeIterator.class));

        attributeContext.setPosition(0);
        assertFalse("Set position should not move the position back when it's less than the current position", attributeContext.hasNextNode());
        assertNull("Set position should not change the currentNodePointer", attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWhenHasNextNode() {
        NodePointer nodePointer = mock(NodePointer.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(nodeIterator);
        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        when(nodeIterator.hasNext()).thenReturn(true);

        attributeContext.nextNode();
        NodePointer nextNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("Next node should return the next node pointer", nodePointer, nextNodePointer);
        assertTrue("Next node should move to the next node", attributeContext.hasNextNode());
    }

    @Test
    public void testNextNodeWhenHasNoNextNode() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(mock(NodeIterator.class));

        attributeContext.nextNode();
        assertFalse("Next node should not move to the next node when there's no next node", attributeContext.hasNextNode());
        assertNull("Next node should not change the currentNodePointer", attributeContext.getCurrentNodePointer());
    }
}