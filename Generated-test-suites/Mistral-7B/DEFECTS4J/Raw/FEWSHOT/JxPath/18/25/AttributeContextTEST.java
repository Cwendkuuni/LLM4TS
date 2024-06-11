package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorImpl;
import org.apache.commons.jxpath.ri.model.NodePointerImpl;
import org.apache.commons.jxpath.ri.QName;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstruction() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointerWithNoAttributeFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointerWithAttributeFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        QName attributeName = new QName("testNamespace", "testAttribute");
        NodeIterator nodeIterator = new NodeIteratorImpl(new NodePointerImpl(1, 1, null), 1);
        when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointerImpl(0, 0, null));
        when(parentContext.getAttributeIterator(attributeName)).thenReturn(nodeIterator);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointerImpl(1, 1, null);
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointerImpl(0, 0, null));
        when(parentContext.getAttributeIterator(new QName("testNamespace", "testAttribute"))).thenReturn(nodeIterator);

        attributeContext.setPosition(1);
        NodePointer expectedNodePointer = new NodePointerImpl(1, 1, null);
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.iterator = null;
        attributeContext.setPosition(1);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNoAttributeFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithAttributeFound() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        QName attributeName = new QName("testNamespace", "testAttribute");
        NodeIterator nodeIterator = new NodeIteratorImpl(new NodePointerImpl(1, 1, null), 1);
        when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointerImpl(0, 0, null));
        when(parentContext.getAttributeIterator(attributeName)).thenReturn(nodeIterator);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointerImpl(1, 1, null);
        assertTrue(attributeContext.nextNode());
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());

        NodePointer nextNode = new NodePointerImpl(2, 1, null);
        when(nodeIterator.getNodePointer()).thenReturn(nextNode);
        assertTrue(attributeContext.nextNode());
        assertEquals(nextNode, attributeContext.getCurrentNodePointer());
    }
}