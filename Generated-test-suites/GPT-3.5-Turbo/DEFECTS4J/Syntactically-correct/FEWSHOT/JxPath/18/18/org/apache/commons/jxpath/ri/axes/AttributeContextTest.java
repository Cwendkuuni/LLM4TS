package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("attributeName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(5);
        attributeContext.reset();
        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(2);
        assertEquals(2, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.nextNode());
        assertEquals(3, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionWhenPositionLessThanCurrentPosition() {
        attributeContext.setPosition(2);
        attributeContext.setPosition(1);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionWhenPositionIsZero() {
        attributeContext.setPosition(2);
        attributeContext.setPosition(0);
        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenSetStartedIsFalse() {
        attributeContext.reset();
        attributeContext.nextNode();
        assertEquals(0, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.isSetStarted());
    }

    @Test
    public void testNextNodeWhenNodeTestIsNotNodeNameTest() {
        NodeTest nodeTest = new NodeTest() {
            @Override
            public boolean isWildcard() {
                return false;
            }

            @Override
            public boolean test(EvalContext context) {
                return false;
            }
        };
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
        assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testNextNodeWhenIteratorIsNull() {
        attributeContext.setStarted = true; //set setStarted to true to bypass the first check for setStarted in nextNode()
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenIteratorPositionIsNotValid() {
        MockNodeIterator iterator = new MockNodeIterator();
        iterator.setPosition(0);
        attributeContext.setStarted = true; //set setStarted to true to bypass the first check for setStarted in nextNode()
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenIteratorPositionIsValid() {
        MockNodeIterator iterator = new MockNodeIterator();
        iterator.setPosition(1);
        attributeContext.setStarted = true; //set setStarted to true to bypass the first check for setStarted in nextNode()
        attributeContext.iterator = iterator;
        assertTrue(attributeContext.nextNode());
        assertEquals(2, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    private class MockNodeIterator implements NodeIterator {

        private int position;

        @Override
        public NodePointer getNodePointer() {
            return null;
        }

        @Override
        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public int getPosition() {
            return position;
        }
    }
}