package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext.getNodeTest());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer expectedNodePointer = nodePointer;
        NodePointer actuallyNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actuallyNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setStarted(true);
        attributeContext.setIterator(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNodePointer() {
                return new NodePointer(null, 0, 0);
            }
        });
        attributeContext.setCurrentNodePointer(new NodePointer(null, 0, 0));
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getIterator());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(null, 0, 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        AttributeContext expectedAttributeContext = new AttributeContext(parentContext, nodeTest);
        AttributeContext actuallyAttributeContext = new AttributeContext(parentContext, nodeTest);
        actuallyAttributeContext.setPosition(0);
        Assert.assertSame(expectedAttributeContext, actuallyAttributeContext);

        actuallyAttributeContext.setPosition(1);
        Assert.assertNotSame(expectedAttributeContext, actuallyAttributeContext);
        Assert.assertFalse(expectedAttributeContext.isSetStarted());
        Assert.assertNull(expectedAttributeContext.getIterator());
        Assert.assertNull(expectedAttributeContext.getCurrentNodePointer());

        actuallyAttributeContext.setPosition(0);
        Assert.assertSame(expectedAttributeContext, actuallyAttributeContext);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(null, 0, 0);
        NodeIterator nodeIterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNodePointer() {
                return nodePointer;
            }
        };
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.setIterator(nodeIterator);
        attributeContext.setStarted(true);
        NodePointer expectedNodePointer = nodePointer;
        NodePointer actuallyNodePointer = attributeContext.nextNode().getNodePointer();
        Assert.assertEquals(expectedNodePointer, actuallyNodePointer);

        attributeContext.nextNode();
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWithoutStarted() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:attribute"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(null, 0, 0);
        NodeIterator nodeIterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer nextNodePointer() {
                return nodePointer;
            }
        };
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.setIterator(nodeIterator);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
    }
}