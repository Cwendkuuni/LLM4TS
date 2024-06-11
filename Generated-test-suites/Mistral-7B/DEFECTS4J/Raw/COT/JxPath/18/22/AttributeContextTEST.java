package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test cases for AttributeContext class
 */
public class AttributeContextTest extends AbstractJxPathTest {

    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() throws Exception {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("attrName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetterCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        attributeContext.nextNode();
        attributeContext.setPosition(0);
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());

        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(2);
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(2, attributeContext.getCurrentPosition());

        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(5); // out of bounds
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(5, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);

        attributeContext.nextNode();
        assertTrue(attributeContext.setStarted);
        assertNotNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        assertNotNull(attributeContext.iterator);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeAfterEndOfIterator() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWithEmptyIterator() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }
}