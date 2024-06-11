package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
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
        nodeTest = new NodeNameTest("test");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(10);
        assertTrue(attributeContext.isSetStarted());
        assertTrue(attributeContext.nextNode());
        assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWhenAttributeIteratorIsNull() {
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenPositionIsGreaterThanNumberOfAttributes() {
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int position) {
                return position <= 0;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodeIterator getNextSibling() {
                return null;
            }
        };
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenAttributeIteratorHasNoMoreNodes() {
        NodeIterator iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int position) {
                return position <= 0;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public NodeIterator getNextSibling() {
                return null;
            }
        };
        NodePointer nodePointer = new NodePointer() {
            @Override
            public int getNodeType() {
                return 1;
            }

            @Override
            public String getNodeName() {
                return "nodeName";
            }

            @Override
            public int getLineNumber() {
                return 0;
            }

            @Override
            public int getLineNumber() {
                return 0;
            }
        };
        iterator.setNodePointer(nodePointer);
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenAttributeIteratorHasNextNode() {
        NodeIterator iterator = new NodeIterator() {
            int position = 0;

            @Override
            public NodePointer getNodePointer() {
                return new NodePointer() {
                    @Override
                    public int getNodeType() {
                        return 1;
                    }

                    @Override
                    public String getNodeName() {
                        return "nodeName" + position;
                    }

                    @Override
                    public int getLineNumber() {
                        return 0;
                    }

                    @Override
                    public int getLineNumber() {
                        return 0;
                    }
                };
            }

            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }

            @Override
            public boolean hasNext() {
                return position < 3;
            }

            @Override
            public NodeIterator getNextSibling() {
                return null;
            }
        };
        attributeContext.iterator = iterator;
        NodePointer nodePointer = null;
        for (int i = 0; i < 3; i++) {
            nodePointer = attributeContext.nextNode().getNodePointer();
            assertEquals("nodeName" + i, nodePointer.getNodeName());
        }
        assertNull(attributeContext.nextNode().getNodePointer());
    }
}