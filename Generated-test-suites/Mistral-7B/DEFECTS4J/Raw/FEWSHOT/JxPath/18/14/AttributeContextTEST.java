package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testEmptyAttributeContext() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeTest() {});
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testInitialPositionWithEmptyAttributeContext() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeTest() {});
        assertTrue(attributeContext.getCurrentPosition() == 0);
    }

    @Test
    public void testNextNodeWithEmptyAttributeContext() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeTest() {});
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithAttributeContextAndAttributeExists() {
        String xml = "<root attr='value'><child attr='otherValue'></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(parentContext.getCurrentNodePointer(), 0);
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
        assertEquals("value", attributeContext.getCurrentNodePointer().getNodeValue());
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithAttributeContextAndAttributeDoesNotExist() {
        String xml = "<root attr='value'><child></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr2");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionWithAttributeContextAndPositionBeforeFirstNode() {
        String xml = "<root attr='value'><child attr='otherValue'></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.setPosition(-1));
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithAttributeContextAndPositionEqualsCurrentNode() {
        String xml = "<root attr='value'><child attr='otherValue'></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(0));
        assertSame(attributeContext.getCurrentNodePointer(), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithAttributeContextAndPositionAfterCurrentNode() {
        String xml = "<root attr='value'><child attr='otherValue'></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(1));
        NodePointer expectedNodePointer = new NodePointer(attributeContext.getCurrentNodePointer().getParentNode(), 1);
        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testResetWithAttributeContext() {
        String xml = "<root attr='value'><child attr='otherValue'></child></root>";
        EvalContext parentContext = new EvalContext(new NodeIterator(xml) {}.getRootNode());
        NodeTest nodeTest = new NodeNameTest("attr");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
        assertTrue(attributeContext.getCurrentPosition() == 0);
    }
}