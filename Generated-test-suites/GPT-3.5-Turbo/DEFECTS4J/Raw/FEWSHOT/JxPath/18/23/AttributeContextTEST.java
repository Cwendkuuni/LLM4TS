package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class AttributeContextTest {
    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext();
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.nodeTest);
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(new EvalContext(), new NodeNameTest(new QName("test")));
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator() {};

        attributeContext.reset();

        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        AttributeContext attributeContext = new AttributeContext(new EvalContext(), new NodeNameTest(new QName("test")));
        attributeContext.currentPosition = 2;
        attributeContext.iterator = new NodeIterator() {};

        assertTrue(attributeContext.setPosition(3));

        assertEquals(3, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);

        assertFalse(attributeContext.setPosition(1));

        assertEquals(1, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext();
        parentContext.setCurrentNodePointer(new NodePointer());
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("test")));

        assertFalse(attributeContext.nextNode());

        parentContext.getCurrentNodePointer().setAttributeValue("test", "value");
        assertTrue(attributeContext.nextNode());

        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setStarted);

        parentContext.getCurrentNodePointer().setAttributeValue("test", "value2");
        assertTrue(attributeContext.nextNode());

        assertEquals(2, attributeContext.getCurrentPosition());

        assertFalse(attributeContext.nextNode());
    }
}