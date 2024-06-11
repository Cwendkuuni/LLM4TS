package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    private static final QName ATTR_NAME = new QName("attribute");

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(ATTR_NAME);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
        attributeContext.setPosition(3);
        assertEquals(3, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_reset() {
        attributeContext.setPosition(3);
        assertTrue(attributeContext.setPosition(2));
        assertEquals(2, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition_outOfBounds() {
        assertFalse(attributeContext.setPosition(5));
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode_firstInvocation() {
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.setStarted);
        assertEquals(parentContext.getCurrentNodePointer().attributeIterator(ATTR_NAME), attributeContext.iterator);
        assertEquals(attributeContext.iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_successiveInvocations() {
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.setStarted);
        assertEquals(parentContext.getCurrentNodePointer().attributeIterator(ATTR_NAME), attributeContext.iterator);
        assertEquals(attributeContext.iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_noIterator() {
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_iteratorNoNext() {
        attributeContext.iterator.setPosition(1);
        assertFalse(attributeContext.nextNode());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.getCurrentNodePointer());
    }
}