package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(1, 1);
        parentContext.setCurrentNodePointer(nodePointer);

        attributeContext.setPosition(1);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(nodePointer, currentNodePointer);

        attributeContext.setPosition(0);
        currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = new NodePointer(1, 1);
        parentContext.setCurrentNodePointer(nodePointer1);
        NodeIterator iterator = new NodeIteratorMock();
        iterator.setNodePointer(new NodePointer(1, 2));
        parentContext.setAttributeIterator(iterator);

        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = new NodePointer(1, 2);
        assertEquals(expectedNodePointer, currentNodePointer);

        attributeContext.nextNode();
        currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testNextNodeWithNullIterator() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = new NodePointer(1, 1);
        parentContext.setCurrentNodePointer(nodePointer1);

        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testNextNodeWithEmptyIterator() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = new NodePointer(1, 1);
        parentContext.setCurrentNodePointer(nodePointer1);
        NodeIterator iterator = new NodeIteratorMock();

        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer1 = new NodePointer(1, 1);
        parentContext.setCurrentNodePointer(nodePointer1);

        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = new NodePointer(1, 2);
        assertEquals(expectedNodePointer, currentNodePointer);

        attributeContext.reset();
        currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }
}

class NodeIteratorMock implements NodeIterator {
    private NodePointer nodePointer;
    private int position;

    public NodeIteratorMock() {
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < 10;
    }

    @Override
    public NodePointer nextNode() {
        NodePointer nodePointer1 = new NodePointer(1, position++);
        this.nodePointer = nodePointer1;
        return nodePointer1;
    }

    @Override
    public NodePointer getNodePointer() {
        return nodePointer;
    }

    @Override
    public void setPosition(int newPosition) {
        position = newPosition;
    }

    @Override
    public int getPosition() {
        return position;
    }
}