package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AttributeContext}.
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

    @After
    public void tearDown() {
        attributeContext.reset();
    }

    @Test
    public void testConstructor() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(Mockito.any(QName.class)))
                .thenReturn(iterator);
        attributeContext.setPosition(0);
        assertTrue(attributeContext.iterator != null);
        Mockito.verify(iterator).setPosition(0);
        attributeContext.setPosition(1);
        Mockito.verify(iterator).setPosition(1);
    }

    @Test
    public void testNextNode() {
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(Mockito.any(QName.class)))
                .thenReturn(iterator);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer);
        Mockito.when(iterator.setPosition(Mockito.anyInt()))
                .thenReturn(true);

        attributeContext.nextNode();
        Mockito.verify(iterator).setPosition(1);
        assertNull(attributeContext.currentNodePointer);
        attributeContext.nextNode();
        Mockito.verify(iterator).setPosition(2);
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }
}