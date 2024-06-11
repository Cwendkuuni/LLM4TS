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
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test:attr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextEmpty() {
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testAttributeContextSingleAttribute() {
        NodePointer nodePointer = new NodePointer(new Object(), null, 0);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return nodePointer;
            }

            @Override
            public boolean setPosition(int position) {
                return position == 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public NodeIterator clone() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer);
        parentContext.setAttributeIterator(iterator);

        attributeContext.nextNode();
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testAttributeContextMultipleAttributes() {
        NodePointer nodePointer1 = new NodePointer(new Object(), null, 0);
        NodePointer nodePointer2 = new NodePointer(new Object(), null, 1);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return (NodePointer) ArrayUtils.toObject(new Object[] { nodePointer1, nodePointer2 });
            }

            @Override
            public boolean setPosition(int position) {
                if (position == 0) {
                    return true;
                } else if (position == 1) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public NodeIterator clone() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer1);
        parentContext.setAttributeIterator(iterator);

        attributeContext.nextNode();
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());
        attributeContext.nextNode();
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testAttributeContextMoveToPosition() {
        NodePointer nodePointer1 = new NodePointer(new Object(), null, 0);
        NodePointer nodePointer2 = new NodePointer(new Object(), null, 1);
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return (NodePointer) ArrayUtils.toObject(new Object[] { nodePointer1, nodePointer2 });
            }

            @Override
            public boolean setPosition(int position) {
                if (position == 0) {
                    return true;
                } else if (position == 1) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public NodeIterator clone() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer1);
        parentContext.setAttributeIterator(iterator);

        attributeContext.nextNode();
        assertEquals(nodePointer1, attributeContext.getCurrentNodePointer());
        attributeContext.nextNode();
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());

        attributeContext.reset();
        attributeContext.setPosition(1);
        assertEquals(nodePointer2, attributeContext.getCurrentNodePointer());
    }
}