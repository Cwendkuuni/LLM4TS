package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.currentNodePointer = nodePointer;
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.setStarted = true;
        attributeContext.reset();
        verify(attributeContext).super.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPositionWhenCurrentPositionIsLessThanPosition() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.setPosition(1);
        assertFalse(attributeContext.nextNode());
        verify(attributeContext).reset();
    }

    @Test
    public void testSetPositionWhenCurrentPositionIsEqualToPosition() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.setPosition(0);
        verify(attributeContext).nextNode();
        verifyNoMoreInteractions(attributeContext.iterator);
    }

    @Test
    public void testSetPositionWhenCurrentPositionIsGreaterThanPosition() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.iterator.getNodePointer = () -> nodePointer;
        attributeContext.setPosition(1);
        verify(attributeContext).nextNode();
        verifyNoMoreInteractions(attributeContext.iterator);
    }

    @Test
    public void testNextNodeWhenHasNextNode() {
        NodePointer nextNodePointer = Mockito.mock(NodePointer.class);
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.iterator.getNodePointer = () -> nextNodePointer;
        attributeContext.nextNode();
        verify(attributeContext.iterator).setPosition(attributeContext.iterator.getPosition() + 1);
        verify(attributeContext).currentNodePointer = nextNodePointer;
    }

    @Test
    public void testNextNodeWhenHasNoNextNode() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.nextNode();
        verify(attributeContext.iterator).setPosition(attributeContext.iterator.getPosition() + 1);
        verify(attributeContext).currentNodePointer = null;
    }

    @Test
    public void testNextNodeWhenHasNoNextNodeButPreviousNodeIsEmpty() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.nextNode();
        attributeContext.nextNode();
        verify(attributeContext.iterator).setPosition(attributeContext.iterator.getPosition() + 2);
        verify(attributeContext).currentNodePointer = null;
    }
}