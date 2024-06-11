package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testgetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(1, 1);
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertSame(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertNull(attributeContext.getCurrentPosition());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = new NodePointer(1, 1);
        int position = 2;
        attributeContext.setCurrentNodePointer(currentNodePointer);
        Assert.assertTrue(attributeContext.setPosition(position));
        NodePointer actualCurrentNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNotNull(actualCurrentNodePointer);
        Assert.assertEquals(position, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeFalse() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int position) {
                return false;
            }
        };
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeTrue() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            private int position = 0;

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer(1, position++);
            }

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }
        };
        attributeContext.iterator = iterator;
        NodePointer expectedCurrentNodePointer = new NodePointer(1, 1);
        attributeContext.nextNode();
        NodePointer actualCurrentNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertSame(expectedCurrentNodePointer, actualCurrentNodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeBeforeFirstNode() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }
        };
        attributeContext.nextNode();
    }

    @Test
    public void testSetStarted() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.isSetStarted());
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.isSetStarted());
    }
}