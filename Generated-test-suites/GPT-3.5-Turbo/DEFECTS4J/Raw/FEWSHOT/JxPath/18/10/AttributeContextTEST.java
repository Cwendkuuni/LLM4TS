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
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("test"));
    }

    @Test
    public void testConstructor() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(true);
        attributeContext.setIterator(new NodeIterator());
        attributeContext.setCurrentNodePointer(new NodePointer());

        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        attributeContext = new AttributeContext(parentContext, nodeTest);

        // Test when set is not started
        assertFalse(attributeContext.isSetStarted());
        assertTrue(attributeContext.nextNode());
        assertTrue(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());

        // Test when iterator is null
        attributeContext.setStarted = true;
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());

        // Test when iterator position cannot be set
        attributeContext.iterator = new NodeIterator();
        attributeContext.iterator.setPosition(10);
        assertFalse(attributeContext.nextNode());

        // Test when iterator position can be set
        attributeContext.iterator.setPosition(0);
        assertTrue(attributeContext.nextNode());
        assertEquals(attributeContext.iterator.getNodePointer(), attributeContext.getCurrentNodePointer());
    }
}