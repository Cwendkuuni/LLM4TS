package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private QName nodeName;
    private NodeTest nodeTest;
    private NodeIterator iterator;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeName = new QName("attribute");
        nodeTest = new NodeNameTest(nodeName);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        iterator = new NodeIterator();
        nodePointer = new NodePointer();
    }

    @Test
    public void testGetCurrentNodePointer() {
        attributeContext.currentNodePointer = nodePointer;
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setStarted = true;
        attributeContext.iterator = iterator;
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertEquals(null, attributeContext.iterator);
        assertEquals(0, attributeContext.getPosition());
    }

    @Test
    public void testSetPosition_beforeCurrentPosition() {
        attributeContext.reset();
        attributeContext.setPosition(1);
        attributeContext.setPosition(0);
        assertFalse(attributeContext.setStarted);
        assertEquals(null, attributeContext.iterator);
    }

    @Test
    public void testSetPosition_afterCurrentPosition() {
        attributeContext.reset();
        attributeContext.setPosition(1);
        attributeContext.iterator = iterator;
        attributeContext.setPosition(2);
        assertTrue(attributeContext.setStarted);
        assertEquals(null, attributeContext.iterator);
    }

    @Test
    public void testNextNode_noNodeTest() {
        attributeContext.reset();
        attributeContext.nodeTest = null;
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_noIterator() {
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_invalidIteratorPosition() {
        attributeContext.reset();
        attributeContext.iterator = iterator;
        iterator.setPosition(2);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_validIteratorPosition() {
        attributeContext.reset();
        attributeContext.setStarted = true;
        attributeContext.nodeTest = new NodeNameTest(new QName("test"));
        attributeContext.iterator = iterator;
        iterator.setPosition(1);
        assertTrue(attributeContext.nextNode());
        assertEquals(iterator.getNodePointer(), attributeContext.currentNodePointer);
    }
}