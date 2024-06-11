package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for AttributeContext class.
 */
public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertTrue(attributeContext.getCurrentNodePointer() == null);
        assertTrue(attributeContext.iterator == null);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.currentNodePointer = nodePointer;
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                return nodePointer;
            }
        };
        attributeContext.iterator = iterator;

        NodePointer expectedNodePointer = nodePointer;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.currentNodePointer = nodePointer;
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                return nodePointer;
            }
        };
        attributeContext.iterator = iterator;

        attributeContext.reset();
        assertTrue(attributeContext.setStarted == false);
        assertTrue(attributeContext.iterator == null);
        assertTrue(attributeContext.currentNodePointer == null);
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer1 = new NodePointer(new Object(), 1);
        NodePointer nodePointer2 = new NodePointer(new Object(), 2);
        attributeContext.currentNodePointer = nodePointer1;
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                if (getCurrentPosition() == 0) {
                    return nodePointer1;
                } else {
                    return nodePointer2;
                }
            }
        };

        attributeContext.iterator = iterator;
        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer1);
        assertTrue(attributeContext.setPosition(1));
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer2);

        attributeContext.reset();
        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.getCurrentNodePointer() == null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWithoutSetStarted() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                return null;
            }
        };
        attributeContext.iterator = iterator;

        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWithIterator() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer1 = new NodePointer(new Object(), 1);
        NodePointer nodePointer2 = new NodePointer(new Object(), 2);
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                if (getCurrentPosition() == 0) {
                    return nodePointer1;
                } else {
                    return nodePointer2;
                }
            }

            public boolean setPosition(int position) {
                return position == getCurrentPosition();
            }
        };

        attributeContext.iterator = iterator;
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer1);
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer2);
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == null);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeAfterEndOfIterator() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer1 = new NodePointer(new Object(), 1);
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                return nodePointer1;
            }
        };

        attributeContext.iterator = iterator;
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.nextNode()); // will throw an exception
    }

    @Test
    public void testSetPositionBeforeStart() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object(), 1);

        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.getCurrentNodePointer() == null);
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer);
    }

    @Test
    public void testNextNodeWithNullIterator() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == null);
    }

    @Test
    public void testNextNodeWithEmptyIterator() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = new NodeIterator() {
            public NodePointer getNodePointer() {
                return null;
            }
        };

        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
        assertTrue(attributeContext.getCurrentNodePointer() == null);
    }
}