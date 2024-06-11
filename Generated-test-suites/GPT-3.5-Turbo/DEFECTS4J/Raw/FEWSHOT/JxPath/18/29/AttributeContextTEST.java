package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;
    private NodeIterator iterator;
    private NodePointer nodePointer1;
    private NodePointer nodePointer2;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        iterator = mock(NodeIterator.class);
        nodePointer1 = mock(NodePointer.class);
        nodePointer2 = mock(NodePointer.class);
    }

    @Test
    public void testGetCurrentNodePointer() {
        attributeContext.currentNodePointer = nodePointer1;
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertEquals(nodePointer1, result);
    }

    @Test
    public void testReset() {
        attributeContext.setCurrentPosition(5);
        attributeContext.setStarted = true;
        attributeContext.iterator = iterator;
        attributeContext.reset();
        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        attributeContext.setCurrentPosition(5);
        attributeContext.setStarted = true;
        attributeContext.iterator = iterator;
        
        when(iterator.setPosition(6)).thenReturn(true);
        boolean result1 = attributeContext.setPosition(6);
        assertTrue(result1);
        assertEquals(6, attributeContext.getCurrentPosition());

        when(iterator.setPosition(7)).thenReturn(false);
        boolean result2 = attributeContext.setPosition(7);
        assertFalse(result2);
        assertEquals(6, attributeContext.getCurrentPosition());

        when(iterator.setPosition(8)).thenReturn(false);
        boolean result3 = attributeContext.setPosition(8);
        assertFalse(result3);
        assertEquals(6, attributeContext.getCurrentPosition());

        attributeContext.reset();
        attributeContext.setStarted = true;
        attributeContext.iterator = null;

        boolean result4 = attributeContext.setPosition(9);
        assertFalse(result4);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        attributeContext.setStarted = false;
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodeTest instanceof NodeNameTest).thenReturn(true);
        when(nodeTest).thenReturn(mock(NodeNameTest.class));
        NodeNameTest nodeNameTest = (NodeNameTest) nodeTest;
        when(nodeNameTest.getNodeName()).thenReturn(mock(QName.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest.getNodeName())).thenReturn(iterator);

        when(iterator.setPosition(1)).thenReturn(true);
        when(iterator.getNodePointer()).thenReturn(nodePointer1);
        boolean result1 = attributeContext.nextNode();
        assertTrue(result1);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(nodePointer1, attributeContext.currentNodePointer);

        when(iterator.setPosition(2)).thenReturn(false);
        boolean result2 = attributeContext.nextNode();
        assertFalse(result2);
        assertEquals(1, attributeContext.getCurrentPosition());

        attributeContext.setStarted = true;
        boolean result3 = attributeContext.nextNode();
        assertFalse(result3);
        assertEquals(1, attributeContext.getCurrentPosition());

        attributeContext.iterator = null;
        boolean result4 = attributeContext.nextNode();
        assertFalse(result4);
        assertEquals(1, attributeContext.getCurrentPosition());
    }
}