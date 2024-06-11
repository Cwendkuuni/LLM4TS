package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;
    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        iterator = mock(NodeIterator.class);
        currentNodePointer = mock(NodePointer.class);
    }

    @After
    public void tearDown() {
        attributeContext = null;
        currentNodePointer = null;
        iterator = null;
        nodeTest = null;
        parentContext = null;
    }

    @Test
    public void testAttributeContextConstructor() {
        Assert.assertNotNull(attributeContext);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        attributeContext.iterator = iterator;
        Assert.assertSame(currentNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.iterator = iterator;
        attributeContext.currentNodePointer = currentNodePointer;
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        when(iterator.setPosition(0)).thenReturn(true);
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        attributeContext.reset();
        attributeContext.iterator = iterator;
        Assert.assertTrue(attributeContext.setPosition(0));
        Assert.assertSame(currentNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionNegative() {
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setPosition(-1));
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionOutOfBounds() {
        when(iterator.setPosition(1)).thenReturn(false);
        attributeContext.reset();
        attributeContext.iterator = iterator;
        Assert.assertFalse(attributeContext.setPosition(1));
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        when(iterator.setPosition(0)).thenReturn(true);
        when(iterator.setPosition(1)).thenReturn(true);
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        attributeContext.reset();
        attributeContext.iterator = iterator;
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertSame(currentNodePointer, attributeContext.getCurrentNodePointer());
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotSame(currentNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWithoutStart() {
        attributeContext.iterator = iterator;
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        Assert.assertFalse(attributeContext.nextNode());
    }
}