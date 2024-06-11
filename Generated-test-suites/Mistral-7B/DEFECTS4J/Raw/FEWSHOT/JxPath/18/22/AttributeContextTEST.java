package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
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
        nodeTest = new NodeNameTest(new QName("testAttribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertTrue(attributeContext.isReset());
    }

    @Test
    public void testSetPositionAndNextNode() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        attributeContext.setPosition(3);

        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNoAttribute() {
        NodeIterator attributeIterator = new NodeIterator(new NodePointer(new Object[]{1, 1}, null, null, null));
        parentContext.setCurrentNodePointer(new NodePointer(new Object[]{1, 0}, null, null, null));
        parentContext.setAttributeIterator(attributeIterator);

        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithAttribute() {
        NodePointer attributeNodePointer = new NodePointer(new Object[]{1, 1}, null, new String[]{"testAttribute", "testValue"}, null);
        NodeIterator attributeIterator = new NodeIterator(attributeNodePointer);
        parentContext.setCurrentNodePointer(new NodePointer(new Object[]{1, 0}, null, null, null));
        parentContext.setAttributeIterator(attributeIterator);

        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertEquals(attributeNodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeBeforeReset() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        attributeContext.nextNode();
    }
}