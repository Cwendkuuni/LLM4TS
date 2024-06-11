package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setUp() {
        NodePointer parent = new TestNodePointer();
        domAttributeIterator = new DOMAttributeIterator(parent, new QName("test"));
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, domAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_validPosition() {
        assertTrue(domAttributeIterator.setPosition(1));
        assertEquals(1, domAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_invalidPosition() {
        assertFalse(domAttributeIterator.setPosition(0));
        assertEquals(0, domAttributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
    }

    private static class TestNodePointer implements NodePointer {

        @Override
        public Object getImmediateNode() {
            return null;
        }

        @Override
        public Node getNode() {
            return null;
        }

        @Override
        public Node getImmediateNodePointer() {
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Object getBaseValue() {
            return null;
        }

        @Override
        public Object getImmediateValue() {
            return null;
        }
    }

}