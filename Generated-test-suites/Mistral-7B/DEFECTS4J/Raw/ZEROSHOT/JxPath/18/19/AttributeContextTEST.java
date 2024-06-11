package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    @org.junit.Before
    public void setUp() {
        // Setup mocks
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("test"));
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertSame(parentContext, attributeContext.getParentContext());
        assertSame(nodeTest, attributeContext.getNodeTest());
    }

    /**
     * Test getCurrentNodePointer method when not set.
     */
    @org.junit.Test
    public void testGetCurrentNodePointerBeforeSet() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test reset method.
     */
    @org.junit.Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    /**
     * Test setPosition method when position is smaller than current.
     */
    @org.junit.Test
    public void testSetPositionBeforeCurrent() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.setPosition(0);
        assertFalse(attributeContext.isNextNode());
    }

    /**
     * Test nextNode method when not started.
     */
    @org.junit.Test
    public void testNextNodeBeforeStarted() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.isNextNode());
    }

    /**
     * Test nextNode method when end of attributes reached.
     */
    @org.junit.Test
    public void testNextNodeEndOfAttributes() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        when(parentContext.getCurrentNodePointer().getAttributeIterator("test"))
                .thenReturn(new NodeIterator() {
                    private int position = 0;

                    @Override
                    public boolean setPosition(int newPosition) {
                        position = newPosition;
                        return true;
                    }

                    @Override
                    public boolean hasNext() {
                        return position < 2;
                    }

                    @Override
                    public NodePointer nextNodePointer() {
                        return new NodePointer(new Object());
                    }

                    @Override
                    public NodeIterator getIterator() {
                        return this;
                    }
                });
        assertFalse(attributeContext.isNextNode());
    }

    /**
     * Test nextNode method when next node exists.
     */
    @org.junit.Test
    public void testNextNodeNextNodeExists() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        when(parentContext.getCurrentNodePointer().getAttributeIterator("test"))
                .thenReturn(new NodeIterator() {
                    private int position = 0;

                    @Override
                    public boolean setPosition(int newPosition) {
                        position = newPosition;
                        return true;
                    }

                    @Override
                    public boolean hasNext() {
                        return position < 2;
                    }

                    @Override
                    public NodePointer nextNodePointer() {
                        return new NodePointer(new Object());
                    }

                    @Override
                    public NodeIterator getIterator() {
                        return this;
                    }
                });
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(new Object());
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }
}