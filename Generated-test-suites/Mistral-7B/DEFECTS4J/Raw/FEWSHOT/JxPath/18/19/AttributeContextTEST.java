package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;

import java.util.ArrayList;
import java.util.List;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        NodeTest nodeTest = mock(NodeTest.class);
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        QName nodeName = mock(QName.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        NodePointer nodePointer = mock(NodePointer.class);

        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodeTest.getNodeName()).thenReturn(nodeName);
        when(nodePointer.attributeIterator(nodeName)).thenReturn(nodeIterator);
        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        when(nodeIterator.hasNext()).thenReturn(true);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertEquals(nodePointer, currentNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        QName nodeName = mock(QName.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        NodePointer nodePointer = mock(NodePointer.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodeTest.getNodeName()).thenReturn(nodeName);
        when(nodePointer.attributeIterator(nodeName)).thenReturn(nodeIterator);
        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        when(nodeIterator.hasNext()).thenReturn(true);

        List<Integer> positions = new ArrayList<>();
        positions.add(0);
        positions.add(2);

        for (Integer position : positions) {
            attributeContext.setPosition(position);
            NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
            assertNotNull(currentNodePointer);
            assertEquals(nodePointer, currentNodePointer);
        }

        positions.add(Integer.MAX_VALUE);
        assertFalse(attributeContext.setPosition(positions.get(positions.size())));
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        QName nodeName = mock(QName.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        NodePointer nodePointer = mock(NodePointer.class);

        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodeTest.getNodeName()).thenReturn(nodeName);
        when(nodePointer.attributeIterator(nodeName)).thenReturn(nodeIterator);
        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        when(nodeIterator.hasNext()).thenReturn(true);
        when(nodeIterator.hasNext()).thenReturn(false);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        boolean nextNode = attributeContext.nextNode();
        assertTrue(nextNode);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);

        nextNode = attributeContext.nextNode();
        assertTrue(nextNode);
        currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);

        nextNode = attributeContext.nextNode();
        assertFalse(nextNode);
    }
}