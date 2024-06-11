package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("attributeName"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer expectedNodePointer = null;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        Assert.assertFalse(attributeContext.setStarted);
        Assert.assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        int position = 0;
        boolean expected = false;
        boolean actual = attributeContext.setPosition(position);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNextNode() {
        boolean expected = false;
        boolean actual = attributeContext.nextNode();
        Assert.assertEquals(expected, actual);
    }
}