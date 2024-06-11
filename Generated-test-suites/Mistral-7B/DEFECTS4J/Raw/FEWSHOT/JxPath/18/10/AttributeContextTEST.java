package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for AttributeContext class
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test:attr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstruction() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(0);
        NodePointer expectedNodePointer = nodePointer;
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionNegative() {
        attributeContext.setPosition(-1);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionGreaterThanCurrentPosition() {
        attributeContext.setPosition(1);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer = new NodePointer(new Object());
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(null);
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());

        NodeIterator iterator = new NodeIterator() {
            private NodePointer nodePointer = new NodePointer(new Object());

            @Override
            public boolean setPosition(int position) {
                if (position == 0) {
                    this.nodePointer = nodePointer;
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public NodePointer getNodePointer() {
                return this.nodePointer;
            }
        };
        parentContext.setCurrentNodePointer(nodePointer);
        attributeContext.nextNode();
        NodePointer actualNodePointer = iterator.getNodePointer();
        NodePointer expectedNodePointer = new NodePointer(new Object());
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testNextNodeEmptyIterator() {
        NodeIterator iterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodePointer nextNode() {
                return null;
            }
        };
        parentContext.setCurrentNodePointer(new NodePointer(new Object()));
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }
}