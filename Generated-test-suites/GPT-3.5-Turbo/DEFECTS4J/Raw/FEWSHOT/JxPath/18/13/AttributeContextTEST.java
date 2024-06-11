package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AttributeContextTest {
    
    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;
    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        iterator = mock(NodeIterator.class);
        currentNodePointer = mock(NodePointer.class);
    }

    @Test
    public void testGetParentContext() {
        assertEquals(parentContext, attributeContext.getParentContext());
    }

    @Test
    public void testGetNodeTest() {
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());

        verify(parentContext).reset();
    }

    @Test
    public void testSetPosition() {
        int position = 5;

        when(attributeContext.getCurrentPosition()).thenReturn(3, 5);

        assertFalse(attributeContext.setPosition(position));
        assertTrue(attributeContext.setPosition(position));

        verify(attributeContext, times(2)).getCurrentPosition();
        verify(attributeContext).reset();
        verify(attributeContext, times(2)).nextNode();
    }

    @Test
    public void testNextNode() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest instanceof NodeNameTest).thenReturn(true);
        when(nodeTest).thenReturn(mock(NodeNameTest.class));
        when(((NodeNameTest) nodeTest).getNodeName()).thenReturn(mock(QName.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(any(QName.class))).thenReturn(iterator);
        when(iterator.setPosition(anyInt())).thenReturn(true, false);
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);

        assertTrue(attributeContext.nextNode());
        assertFalse(attributeContext.nextNode());

        verify(attributeContext).setPosition(2);
        verify(parentContext).getCurrentNodePointer();
        verify(parentContext).getCurrentNodePointer().attributeIterator(any(QName.class));
        verify(iterator, times(2)).setPosition(anyInt());
        verify(iterator).getNodePointer();
    }

}