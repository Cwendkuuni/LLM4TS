package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.SimpleNode;
import org.apache.commons.jxpath.ri.test.TestUtils;

public class AttributeContextTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
        assertNotNull(attributeContext.nodeTest);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }

    /**
     * Test getCurrentNodePointer method.
     */
    @org.junit.Test
    public void testGetCurrentNodePointer() {
        SimpleNode root = new SimpleNode("root");
        SimpleNode child1 = new SimpleNode("child1");
        root.addChild(child1);
        SimpleNode child2 = new SimpleNode("child2");
        root.addChild(child2);
        SimpleNode attr1 = new SimpleNode("attr1");
        attr1.setValue("value1");
        child1.addAttribute(new QName("test"), attr1);

        EvalContext parentContext = new EvalContext(root, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer expected = new NodePointer(child1, attr1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertNotNull(actual);
        assertTrue(actual.equals(expected));

        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test reset method.
     */
    @org.junit.Test
    public void testReset() {
        SimpleNode root = new SimpleNode("root");
        SimpleNode child1 = new SimpleNode("child1");
        root.addChild(child1);
        SimpleNode child2 = new SimpleNode("child2");
        root.addChild(child2);
        SimpleNode attr1 = new SimpleNode("attr1");
        attr1.setValue("value1");
        child1.addAttribute(new QName("test"), attr1);

        EvalContext parentContext = new EvalContext(root, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer expected = new NodePointer(child1, attr1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertTrue(actual.equals(expected));

        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test setPosition method.
     */
    @org.junit.Test
    public void testSetPosition() {
        SimpleNode root = new SimpleNode("root");
        SimpleNode child1 = new SimpleNode("child1");
        root.addChild(child1);
        SimpleNode child2 = new SimpleNode("child2");
        root.addChild(child2);
        SimpleNode attr1 = new SimpleNode("attr1");
        attr1.setValue("value1");
        child1.addAttribute(new QName("test"), attr1);

        EvalContext parentContext = new EvalContext(root, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Test position < current
        attributeContext.setPosition(-1);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());

        // Test position = current
        attributeContext.nextNode();
        attributeContext.setPosition(0);
        assertTrue(attributeContext.nextNode());
        NodePointer expected = new NodePointer(child1, attr1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertTrue(actual.equals(expected));

        // Test position > current
        attributeContext.setPosition(1);
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(3); // position out of bounds
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test nextNode method.
     */
    @org.junit.Test
    public void testNextNode() {
        SimpleNode root = new SimpleNode("root");
        SimpleNode child1 = new SimpleNode("child1");
        root.addChild(child1);
        SimpleNode child2 = new SimpleNode("child2");
        root.addChild(child2);
        SimpleNode attr1 = new SimpleNode("attr1");
        attr1.setValue("value1");
        SimpleNode attr2 = new SimpleNode("attr2");
        attr2.setValue("value2");
        child1.addAttribute(new QName("test1"), attr1);
        child1.addAttribute(new QName("test2"), attr2);

        EvalContext parentContext = new EvalContext(root, null);
        NodeTest nodeTest = new NodeNameTest("test1");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        // Test initial state
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());

        // Test first attribute
        attributeContext.nextNode();
        NodePointer expected = new NodePointer(child1, attr1);
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertTrue(actual.equals(expected));

        // Test next attribute
        attributeContext.nextNode();
        expected = new NodePointer(child1, attr2);
        actual = attributeContext.getCurrentNodePointer();
        assertTrue(actual.equals(expected));

        // Test end of list
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());

        // Test next after end of list
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }
}