package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextCreation() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testAttributeContextGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer expectedNodePointer = nodePointer;
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContextReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();
        assertTrue(attributeContext.isReset());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testAttributeContextSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer);

        int position = 1;
        assertTrue(attributeContext.setPosition(position));
        assertEquals(1, attributeContext.getCurrentPosition());
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer expectedNodePointer = new NodePointer(null, 1, 0);
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        position = 2;
        assertFalse(attributeContext.setPosition(position));
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContextNextNode() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer1);
        assertFalse(attributeContext.nextNode());
        assertEquals(0, attributeContext.getCurrentPosition());
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());

        NodePointer nodePointer2 = new NodePointer(null, 1, 0);
        attributeContext.setCurrentNodePointer(nodePointer2);
        NodeIterator iterator = new NodeIteratorMock();
        attributeContext.setIterator(iterator);
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());

        iterator.setPosition(1);
        assertTrue(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContextIteratorNull() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(" foo", null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer);

        NodeIterator iterator = null;
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
        assertEquals(0, attributeContext.getCurrentPosition());
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    private static class NodeIteratorMock implements NodeIterator {

        int position = 0;

        @Override
        public boolean hasNext() {
            return position < 10;
        }

        @Override
        public NodePointer nextNode() {
            return new NodePointer(null, ++position, 0);
        }

        @Override
        public NodePointer currentNode() {
            return new NodePointer(null, position, 0);
        }

        @Override
        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public NodeIterator clone() throws CloneNotSupportedException {
            return new NodeIteratorMock();
        }
    }
}