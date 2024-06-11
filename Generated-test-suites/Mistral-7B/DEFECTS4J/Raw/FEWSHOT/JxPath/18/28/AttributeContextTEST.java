package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

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
    public void testAttributeContextConstruction() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        assertEquals(attributeContext.getCurrentNodePointer(), attributeContext.currentNodePointer);
    }

    @Test
    public void testReset() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        attributeContext.reset();

        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testSetPosition() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        attributeContext.setPosition(0);

        assertTrue(attributeContext.iterator.setPosition(0));
        assertEquals(attributeContext.getCurrentNodePointer(), attributeContext.iterator.getNodePointer());
    }

    @Test
    public void testSetPositionNegative() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testSetPositionGreater() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        attributeContext.setPosition(5);

        assertFalse(attributeContext.setPosition(5));
    }

    @Test
    public void testNextNode() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object()));
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        NodePointer nodePointer = new NodePointer(new Object());
        nodeIterator.addNodePointer(nodePointer);
        nodeIterator.setPosition(0);

        attributeContext.nextNode();

        assertEquals(attributeContext.getCurrentNodePointer(), nodePointer);
    }

    @Test
    public void testNextNodeNullIterator() {
        attributeContext.nextNode();

        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNodeEndOfIterator() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object())) {
            @Override
            public boolean setPosition(int position) {
                if (position == getSize()) {
                    return false;
                }
                return super.setPosition(position);
            }
        };
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        assertFalse(attributeContext.nextNode());

        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNodeSameNode() {
        NodeIterator nodeIterator = new NodeIterator(new NodePointer(new Object())) {
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(new Object());
            }
        };
        attributeContext.iterator = nodeIterator;
        attributeContext.currentNodePointer = new NodePointer(new Object());

        assertFalse(attributeContext.nextNode());

        assertEquals(attributeContext.iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }
}