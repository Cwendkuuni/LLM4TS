package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for AttributeContext class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("attrName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        attributeContext.nextNode();
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.setPosition(2));
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionNegativeIndex() {
        attributeContext.setPosition(-1);
    }

    @Test
    public void testNextNode() {
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeEmptyIterator() {
        parentContext.getCurrentNodePointer().setNode(new Object());
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeNullIterator() {
        parentContext.getCurrentNodePointer().setNode(null);
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeNoMatchingAttribute() {
        parentContext.getCurrentNodePointer().setNode(new Object());
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithMatchingAttribute() {
        Object node = new Object();
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer() {
                    private Object node = node;

                    @Override
                    public Object getNode() {
                        return node;
                    }
                };
            }

            @Override
            public boolean hasNext() {
                return position < 2;
            }

            @Override
            public NodePointer nextNode() {
                position++;
                return getNodePointer();
            }
        };
        parentContext.getCurrentNodePointer().setNode(node);
        parentContext.getCurrentNodePointer().setNodeIterator(iterator);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
        Assert.assertEquals(node, attributeContext.getCurrentNodePointer().getNode());
    }
}