package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeNameTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test:attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContext() {
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetPosition());
        assertFalse(attributeContext.isSetStarted());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionWithZero() {
        attributeContext.setPosition(0);
        assertTrue(attributeContext.isSetPosition());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(1);
        assertTrue(attributeContext.isSetPosition());
        assertTrue(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWithoutSetStarted() {
        assertFalse(attributeContext.nextNode());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWithSetStarted() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWithIteratorNull() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWithIteratorNotNext() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodePointer getNextNode() {
                return null;
            }

            @Override
            public NodePointer getCurrentNodePointer() {
                return nodePointer;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWithIteratorNext() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;
            private NodePointer nextNodePointer = new NodePointer(new Object(), 1);

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer getNextNode() {
                return nextNodePointer;
            }

            @Override
            public NodePointer getCurrentNodePointer() {
                return nodePointer;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testReset() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(1);
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(1, attributeContext.getCurrentPosition());
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }
}