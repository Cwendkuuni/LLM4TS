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
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertTrue(attributeContext.getCurrentPosition() == 0);
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(0));
        assertTrue(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(2));
    }

    @Test
    public void testNextNode() {
        attributeContext.nextNode();
        assertTrue(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
        
        attributeContext.nextNode();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.currentNodePointer);
    }
}