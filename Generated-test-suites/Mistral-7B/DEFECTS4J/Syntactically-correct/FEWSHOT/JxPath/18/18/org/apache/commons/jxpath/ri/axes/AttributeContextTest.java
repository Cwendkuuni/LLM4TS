package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
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
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertSame(parentContext, attributeContext.getParentContext());
        assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer expected = new NodePointer(new Object(), 0);
        attributeContext.currentNodePointer = expected;
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertSame(expected, actual);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertTrue(attributeContext.isReset());
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPositionWithValidPosition() {
        int position = 1;
        attributeContext.setPosition(position);
        assertTrue(attributeContext.isStarted());
        assertTrue(attributeContext.iterator != null);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        int position = 0;
        attributeContext.setPosition(position);
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testNextNodeWithNotStarted() {
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithStartedButNoAttribute() {
        attributeContext.nextNode();
        attributeContext.reset();
        QName name = new QName("test2");
        attributeContext.iterator = parentContext.getCurrentNodePointer().attributeIterator(name);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithStartedAndAttribute() {
        attributeContext.nextNode();
        attributeContext.reset();
        QName name = new QName("test");
        NodePointer expectedFirstAttribute = new NodePointer(new Object(), 0);
        NodeIterator iterator = new NodeIterator() {
            private NodePointer nodePointer = expectedFirstAttribute;

            @Override
            public boolean setPosition(int position) {
                if (nodePointer == null) {
                    return false;
                }
                if (position == 0) {
                    return true;
                }
                nodePointer = null;
                return false;
            }

            @Override
            public NodePointer getNodePointer() {
                return nodePointer;
            }
        };
        attributeContext.iterator = iterator;
        NodePointer actual = attributeContext.nextNode().getCurrentNodePointer();
        assertSame(expectedFirstAttribute, actual);
    }
}