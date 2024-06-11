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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("testAttribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer);
        Mockito.when(iterator.hasNext()).thenReturn(true);
        attributeContext.nextNode();
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer1 = Mockito.mock(NodePointer.class);
        NodePointer nodePointer2 = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer1);
        Mockito.when(iterator.hasNext()).thenReturn(true);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer2);
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(1));
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());
        assertTrue(attributeContext.setPosition(0));
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer1 = Mockito.mock(NodePointer.class);
        NodePointer nodePointer2 = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer1);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        attributeContext.nextNode();
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());

        Mockito.reset(iterator);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        QName name = Mockito.mock(QName.class);
        Mockito.when(nodeTest.getNodeName()).thenReturn(name);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(name)).thenReturn(iterator);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());
    }
}