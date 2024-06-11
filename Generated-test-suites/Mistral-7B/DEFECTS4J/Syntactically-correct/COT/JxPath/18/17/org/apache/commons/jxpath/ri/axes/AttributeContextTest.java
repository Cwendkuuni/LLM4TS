package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test file for AttributeContext class
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(1, 1);
        attributeContext.nextNode();
        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        Assert.assertFalse(attributeContext.getCurrentNodePointer() != null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionInvalidPosition() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(-1);
    }

    @Test
    public void testSetPositionValidPosition() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(2);
        Assert.assertTrue(attributeContext.getCurrentNodePointer() != null);
    }

    @Test
    public void testNextNode() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(1, 1);
        attributeContext.nextNode();
        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testNextNodeEmptyIterator() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator(null, null);
        parentContext.setCurrentNodePointer(new NodePointer(1, 1));
        attributeContext.iterator = iterator;
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeNullIterator() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        parentContext.setCurrentNodePointer(new NodePointer(1, 1));
        attributeContext.iterator = null;
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeNotAttributeIterator() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator(null, null) {
            @Override
            public NodeIterator attributeIterator(QName name) {
                throw new UnsupportedOperationException();
            }
        };
        parentContext.setCurrentNodePointer(new NodePointer(1, 1));
        attributeContext.iterator = iterator;
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWithAttributeIterator() {
        NodeTest nodeTest = new NodeNameTest("test");
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator(null, null) {
            @Override
            public NodeIterator attributeIterator(QName name) {
                NodePointer nodePointer = new NodePointer(2, 1);
                NodeIterator nodeIterator = new NodeIterator(nodePointer, null);
                return nodeIterator;
            }
        };
        parentContext.setCurrentNodePointer(new NodePointer(1, 1));
        attributeContext.iterator = iterator;
        NodePointer expectedNodePointer = new NodePointer(2, 1);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }
}