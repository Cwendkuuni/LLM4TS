package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertSame(nodePointer, currentNodePointer);
    }

    @Test
    public void testReset() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionSuccess() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.setPosition(1);
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer);
    }

    @Test
    public void testSetPositionFailure() {
        NodePointer nodePointer1 = new NodePointer(new Object(), 1);
        NodePointer nodePointer2 = new NodePointer(new Object(), 2);
        attributeContext.setCurrentNodePointer(nodePointer1);
        assertFalse(attributeContext.setPosition(2));
        assertSame(nodePointer1, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeSuccess() {
        NodePointer nodePointer1 = new NodePointer(new Object(), 1);
        NodePointer nodePointer2 = new NodePointer(new Object(), 2);
        attributeContext.setCurrentNodePointer(nodePointer1);
        attributeContext.nextNode();
        assertTrue(attributeContext.getCurrentNodePointer() == nodePointer2);
    }

    @Test
    public void testNextNodeFailure() {
        NodePointer nodePointer = new NodePointer(new Object(), 1);
        attributeContext.setCurrentNodePointer(nodePointer);
        assertFalse(attributeContext.nextNode());
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());
    }
}