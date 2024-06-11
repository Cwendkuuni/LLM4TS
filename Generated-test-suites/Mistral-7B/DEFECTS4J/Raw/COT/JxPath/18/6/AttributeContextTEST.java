package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        QName attrName = new QName("testAttribute");
        nodeTest = new NodeNameTest(attrName);
        parentContext = new EvalContext(null);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    // Test method for constructor
    @Test
    public void testAttributeContextConstructor() {
        Assert.assertNotNull(attributeContext);
    }

    // Test method for getCurrentNodePointer()
    @Test
    public void testGetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        attribute.nextNode();
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertNotNull(nodePointer);
    }

    // Test method for reset()
    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    // Test method for setPosition(int position)
    @Test
    public void testSetPosition() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(1);
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        Assert.assertEquals(attributeContext.getCurrentPosition(), 1);
        Assert.assertNotNull(nodePointer);
    }

    // Test method for nextNode() with empty attribute iterator
    @Test(expected = IllegalStateException.class)
    public void testNextNodeEmptyIterator() {
        attributeContext.nextNode();
        attributeContext.reset();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        attributeContext.nextNode(); // This should throw an exception
    }

    // Test method for nextNode() with null iterator
    @Test(expected = IllegalStateException.class)
    public void testNextNodeNullIterator() {
        parentContext.getCurrentNodePointer().setCurrentNode(null);
        attributeContext.nextNode();
        attributeContext.reset();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        attributeContext.nextNode(); // This should throw an exception
    }

    // Test method for nextNode() with no more attributes
    @Test
    public void testNextNodeNoMoreAttributes() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    // Test method for nextNode() with different attribute name
    @Test
    public void testNextNodeDifferentAttributeName() {
        QName differentAttrName = new QName("differentAttribute");
        AttributeContext differentAttributeContext = new AttributeContext(parentContext, new NodeNameTest(differentAttrName));
        differentAttributeContext.nextNode();
        differentAttributeContext.nextNode();
        Assert.assertFalse(differentAttributeContext.nextNode());
    }

    // Test method for nextNode() with the same attribute multiple times
    @Test
    public void testNextNodeSameAttributeMultipleTimes() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertEquals(attributeContext.getCurrentPosition(), 3);
    }
}