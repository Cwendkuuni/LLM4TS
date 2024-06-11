package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeNameTest.class);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);

        attributeContext.nextNode();
        NodePointer result = attributeContext.getCurrentNodePointer();
        verify(nodePointer).attributeIterator(ArgumentMatchers.eq(new QName("test")));
        assertSame(nodePointer, result);
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        NodePointer nodePointer = mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);

        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getPosition()).thenReturn(1);

        attributeContext.nextNode();
        attributeContext.nextNode();

        assertFalse(attributeContext.setPosition(0));
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(2));
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertNotNull(result);
        verify(nodePointer).attributeIterator(ArgumentMatchers.eq(new QName("test"))).setPosition(2);
        assertSame(result, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer = mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        NodeIterator iterator = mock(NodeIterator.class);
        NodePointer nextNodePointer = mock(NodePointer.class);

        Mockito.when(nodePointer.attributeIterator(ArgumentMatchers.eq(new QName("test")))).thenReturn(iterator);
        Mockito.when(iterator.getNodePointer()).thenReturn(nextNodePointer);
        Mockito.when(iterator.getPosition()).thenReturn(0);
        Mockito.when(nextNodePointer.getNodeName()).thenReturn(new QName("next"));

        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertSame(nextNodePointer, attributeContext.getCurrentNodePointer());
        verify(iterator).setPosition(iterator.getPosition() + 1);
    }
}