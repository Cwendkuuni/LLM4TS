package org.apache.commons.jxpath.ri.axes.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * Test cases for AttributeContext class
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testgetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer expectedNodePointer = nodePointer;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        int position = 1;
        assertTrue(attributeContext.setPosition(position));
        NodePointer expectedNodePointer = new NodePointer(new Object(), 1);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals(position, attributeContext.getCurrentPosition());

        position = Integer.MAX_VALUE;
        assertFalse(attributeContext.setPosition(position));
        assertEquals(position, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.setIterator(new NodeIterator() {
            private int position = 0;

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), position);
            }

            @Override
            public boolean hasNext() {
                return position < 3;
            }

            @Override
            public boolean hasPrevious() {
                return position > 0;
            }
        });

        assertTrue(attributeContext.nextNode());
        NodePointer expectedNodePointer1 = new NodePointer(new Object(), 1);
        NodePointer actualNodePointer1 = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer1, actualNodePointer1);
        assertEquals(1, attributeContext.getCurrentPosition());

        assertTrue(attributeContext.nextNode());
        NodePointer expectedNodePointer2 = new NodePointer(new Object(), 2);
        NodePointer actualNodePointer2 = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer2, actualNodePointer2);
        assertEquals(2, attributeContext.getCurrentPosition());

        assertFalse(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());

        NodePointer nodePointer2 = new NodePointer(new Object(), 3);
        attributeContext.setCurrentNodePointer(nodePointer2);
        assertFalse(attributeContext.nextNode());
        assertEquals(3, attributeContext.getCurrentPosition());
    }
}