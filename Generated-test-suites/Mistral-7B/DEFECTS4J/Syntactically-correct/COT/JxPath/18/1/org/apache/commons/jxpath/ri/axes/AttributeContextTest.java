package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AttributeContext} class.
 *
 * @author Dmitri Plotnikov
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        // Check that constructor is called
        Assert.assertNotNull(attributeContext);
        Assert.assertNotNull(attributeContext.getNodeTest());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testGetCurrentNodePointer() {
        // Basic functionality test
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer result = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(nodePointer, result);

        // Edge case: null node pointer
        attributeContext.setCurrentNodePointer(null);
        result = attributeContext.getCurrentNodePointer();
        Assert.assertNull(result);
    }

    @Test
    public void testReset() {
        // Basic functionality test
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.iterator);

        // Edge case: already reset
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isStarted());
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        // Basic functionality test
        attributeContext.setPosition(0);
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
        Assert.assertFalse(attributeContext.nextNode());

        attributeContext.setPosition(1);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());

        // Edge case: position out of bounds
        attributeContext.setPosition(-1);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(Integer.MAX_VALUE);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        // Basic functionality test
        NodeIterator iterator = parentContext.getCurrentNodePointer().attributeIterator(new NodeNameTest(new QName("test")));
        attributeContext.iterator = iterator;
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());

        // Edge case: no more nodes to iterate
        iterator.setPosition(iterator.getPosition() + 1);
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());

        // Edge case: null iterator
        attributeContext.iterator = null;
        Assert.assertFalse(attributeContext.nextNode());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }
}