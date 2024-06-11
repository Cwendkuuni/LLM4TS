package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

    private static final QName ATTRIBUTE_NAME = new QName("attributeName");
    private static final QName ANOTHER_ATTRIBUTE_NAME = new QName("anotherAttributeName");

    private MockNodePointer parentPointer;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentPointer = new MockNodePointer();
        attributeContext = new AttributeContext(parentPointer, new NodeNameTest(ATTRIBUTE_NAME));
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        assertTrue(currentNodePointer instanceof MockNodePointer);
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }
    
    @Test
    public void testSetPosition() {
        attributeContext.setPosition(0);
        assertEquals(0, attributeContext.getCurrentPosition());
        
        attributeContext.setPosition(1);
        assertEquals(1, attributeContext.getCurrentPosition());
        
        attributeContext.setPosition(0);
        assertEquals(0, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testSetPosition_outOfRange() {
        attributeContext.setPosition(10);
        assertEquals(0, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testNextNode() {
        attributeContext.nextNode();
        assertTrue(attributeContext.setStarted);
        assertNotNull(attributeContext.iterator);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.currentNodePointer);
    }
    
    @Test
    public void testNextNode_noIterator() {
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
    }
}