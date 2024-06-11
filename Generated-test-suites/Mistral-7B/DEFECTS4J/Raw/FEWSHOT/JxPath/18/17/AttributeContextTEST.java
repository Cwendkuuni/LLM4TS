package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithStartPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertEquals(NodeType.ELEMENT_NODE, currentNodePointer.getNodeType());
        assertNull(currentNodePointer.getAttributeValue("test"));
    }

    @Test
    public void testNextNodeWithAttributeValue() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        NodePointer elementNodePointer = new NodePointer(new Object(), NodeType.ELEMENT_NODE, null, null, 0, null, null);
        parentContext.setCurrentNodePointer(elementNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertEquals(NodeType.ATTRIBUTE_NODE, currentNodePointer.getNodeType());
        assertEquals("test", currentNodePointer.getNodeName().getLocalPart());
        assertEquals("value", currentNodePointer.getAttributeValue("test"));
    }

    @Test
    public void testSetPositionWithStartPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(2);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertEquals(NodeType.ELEMENT_NODE, currentNodePointer.getNodeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithNegativePosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(-1);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }
}