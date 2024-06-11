package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Test cases for {@link AttributeContext}.
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstruction() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getNodeTest());
        assertEquals(parentContext, attributeContext.getParentContext());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        attributeContext.setCurrentNodePointer(nodePointer);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);

        NodePointer nodePointer2 = mock(NodePointer.class);
        NodeIterator nodeIterator2 = mock(NodeIterator.class);
        when(nodeIterator1.getNodePointer()).thenReturn(nodePointer2);
        when(nodeIterator2.getNodePointer()).thenReturn(nodePointer2);
        when(nodeIterator2.hasNext()).thenReturn(true);
        when(nodeIterator2.next()).thenReturn(nodePointer2);

        attributeContext.reset();
        assertFalse(attributeContext.hasNext());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);

        attributeContext.setPosition(-1);
        assertFalse(attributeContext.hasNext());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithCurrentPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(false);

        attributeContext.setPosition(0);
        assertFalse(attributeContext.hasNext());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithNextPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);
        when(nodeIterator1.hasNext()).thenReturn(false);

        attributeContext.setPosition(1);
        assertFalse(attributeContext.hasNext());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);

        attributeContext.setPosition(0);
        assertFalse(attributeContext.hasNext());
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(1);
        assertTrue(attributeContext.hasNext());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNoAttributes() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(null);

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithSingleAttribute() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);
        when(nodeIterator1.hasNext()).thenReturn(false);

        NodePointer nodePointer2 = mock(NodePointer.class);
        when(nodePointer1.getAttributeNode(nodeTest.getNodeName().getLocalPart())).thenReturn(nodePointer2);

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithMultipleAttributes() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);
        when(nodeIterator1.hasNext()).thenReturn(true);
        when(nodeIterator1.next()).thenReturn(nodePointer1);

        NodePointer nodePointer2 = mock(NodePointer.class);
        NodePointer nodePointer3 = mock(NodePointer.class);
        when(nodePointer1.getAttributeNode(nodeTest.getNodeName().getLocalPart())).thenReturn(nodePointer2);
        when(nodePointer1.getAttributeNode("other-attribute-name")).thenReturn(nodePointer3);

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(nodePointer3, attributeContext.getCurrentNodePointer());

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithEmptyIterator() {
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = mock(NodePointer.class);
        NodeIterator nodeIterator1 = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer1);
        when(nodePointer1.attributeIterator(nodeTest.getNodeName())).thenReturn(nodeIterator1);

        List<NodePointer> nodePointers = new ArrayList<>();
        when(nodeIterator1.getNext()).thenAnswer(i -> {
            NodePointer nodePointer = nodePointers.size() > i ? nodePointers.get(i) : null;
            nodePointers.add(nodePointer);
            return nodePointer;
        });
        when(nodeIterator1.hasNext()).thenReturn(false);

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}