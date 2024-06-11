package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("foo"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer();
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator();
        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertTrue(attributeContext.getCurrentPosition() == 0);
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(0));

        attributeContext.setPosition(1);
        assertTrue(attributeContext.setCurrentPosition(1));

        assertTrue(attributeContext.setPosition(2));
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());

        attributeContext.setStarted = true;
        attributeContext.nodeTest = new NodeNameTest(new QName("bar"));
        assertFalse(attributeContext.nextNode());

        attributeContext.setStarted = true;
        attributeContext.nodeTest = new NodeNameTest(new QName("foo"));
        assertFalse(attributeContext.nextNode());

        NodeIterator iterator = new NodeIterator();
        iterator.setPosition(1);
        attributeContext.iterator = iterator;
        assertFalse(attributeContext.nextNode());

        iterator.setPosition(2);
        attributeContext.iterator = iterator;
        assertTrue(attributeContext.nextNode());
        assertEquals(iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }
}