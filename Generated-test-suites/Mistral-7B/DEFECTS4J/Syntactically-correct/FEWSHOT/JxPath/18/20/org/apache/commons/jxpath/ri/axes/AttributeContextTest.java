package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNoAttribute() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithAttribute() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        NodePointer nodePointer = new NodePointer(new Object(), new int[0], 0);
        parentContext.setCurrentNodePointer(nodePointer);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), new int[0], position);
            }

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNode() {
                position++;
                return new NodePointer(new Object(), new int[0], position);
            }
        };
        parentContext.setAttributeIterator(iterator);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertTrue(currentNodePointer.getNode() instanceof Object);
        NodePointer nextNodePointer = attributeContext.nextNode();
        assertNotNull(nextNodePointer);
        assertNotSame(currentNodePointer, nextNodePointer);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        NodePointer nodePointer = new NodePointer(new Object(), new int[0], 0);
        parentContext.setCurrentNodePointer(nodePointer);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object(), new int[0], position);
            }

            @Override
            public int getPosition() {
                return position;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNode() {
                position++;
                return new NodePointer(new Object(), new int[0], position);
            }
        };
        parentContext.setAttributeIterator(iterator);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(2);
        NodePointer expectedNodePointer = new NodePointer(new Object(), new int[0], 2);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, currentNodePointer);
    }
}