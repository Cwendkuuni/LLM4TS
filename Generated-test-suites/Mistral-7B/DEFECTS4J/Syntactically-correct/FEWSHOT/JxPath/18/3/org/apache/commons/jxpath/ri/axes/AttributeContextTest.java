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
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextCreation() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertTrue(attributeContext instanceof AttributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer expectedNodePointer = nodePointer;
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();
        assertTrue(attributeContext.isStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testSetPosition_ValidPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        parentContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        attributeContext.setPosition(0);
        NodePointer expectedNodePointer = parentContext.getCurrentNodePointer();
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_InvalidPreviousPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        parentContext.setCurrentNodePointer(new NodePointer(new Object(), 1));
        attributeContext.setPosition(0);
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_ValidPreviousPosition() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer parentNodePointer = new NodePointer(new Object(), 0);
        NodePointer currentNodePointer = new NodePointer(new Object(), 1);
        parentContext.setCurrentNodePointer(parentNodePointer);
        attributeContext.setCurrentNodePointer(currentNodePointer);
        attributeContext.setPosition(1);

        NodePointer expectedNodePointer = currentNodePointer;
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_NoAttributeFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        parentContext.setCurrentNodePointer(new NodePointer(new Object(), 0));
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_AttributeFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer parentNodePointer = new NodePointer(new Object(), 0);
        NodePointer attributeNodePointer = new NodePointer(new Object(), 1);
        NodeIterator attributeIterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer next() {
                return attributeNodePointer;
            }

            @Override
            public NodeIterator getIterator() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(parentNodePointer);
        parentContext.setAttributeIterator(attributeIterator);

        attributeContext.nextNode();
        NodePointer expectedNodePointer = attributeNodePointer;
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_MultipleAttributesFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer parentNodePointer = new NodePointer(new Object(), 0);
        NodePointer firstAttributeNodePointer = new NodePointer(new Object(), 1);
        NodePointer secondAttributeNodePointer = new NodePointer(new Object(), 2);
        NodeIterator attributeIterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer next() {
                if (getCurrentPosition() == 0) {
                    return firstAttributeNodePointer;
                } else {
                    return secondAttributeNodePointer;
                }
            }

            @Override
            public NodeIterator getIterator() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(parentNodePointer);
        parentContext.setAttributeIterator(attributeIterator);

        attributeContext.nextNode();
        NodePointer expectedFirstNodePointer = firstAttributeNodePointer;
        assertSame(expectedFirstNodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        NodePointer expectedSecondNodePointer = secondAttributeNodePointer;
        assertSame(expectedSecondNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_NoMoreAttributesFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer parentNodePointer = new NodePointer(new Object(), 0);
        NodePointer firstAttributeNodePointer = new NodePointer(new Object(), 1);
        NodeIterator attributeIterator = new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public NodePointer next() {
                if (getCurrentPosition() == 0) {
                    return firstAttributeNodePointer;
                } else {
                    return null;
                }
            }

            @Override
            public NodeIterator getIterator() {
                return this;
            }
        };
        parentContext.setCurrentNodePointer(parentNodePointer);
        parentContext.setAttributeIterator(attributeIterator);

        attributeContext.nextNode();
        NodePointer expectedNodePointer = firstAttributeNodePointer;
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }
}