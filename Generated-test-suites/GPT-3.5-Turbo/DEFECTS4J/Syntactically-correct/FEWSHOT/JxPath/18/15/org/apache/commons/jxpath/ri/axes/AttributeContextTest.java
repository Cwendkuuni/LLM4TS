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
    
    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;
    
    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }
    
    @Test
    public void testGetSetCurrentNodePointer() {
        NodePointer pointer = new NodePointer();
        attributeContext.setCurrentNodePointer(pointer);
        assertEquals(pointer, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }
    
    @Test
    public void testSetPosition() {
        attributeContext.setCurrentPosition(2);
        
        assertTrue(attributeContext.setPosition(4));
        assertEquals(4, attributeContext.getCurrentPosition());
        
        assertTrue(attributeContext.setPosition(2));
        assertEquals(2, attributeContext.getCurrentPosition());
        
        assertTrue(attributeContext.setPosition(1));
        assertEquals(1, attributeContext.getCurrentPosition());
        
        assertFalse(attributeContext.setPosition(0));
    }
    
    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
        
        NodePointer pointer = new NodePointer();
        parentContext.setCurrentNodePointer(pointer);
        
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
        
        assertFalse(attributeContext.nextNode());
        
        attributeContext.setStarted = true;
        assertFalse(attributeContext.nextNode());
        
        assertTrue(attributeContext.setPosition(1));
        assertTrue(attributeContext.nextNode());
        
        attributeContext.iterator = new NodeIterator() {
            private int position = 0;
            
            @Override
            public NodePointer getNodePointer() {
                return new NodePointer();
            }
            
            @Override
            public boolean setPosition(int position) {
                this.position = position;
                return true;
            }
            
            @Override
            public int getPosition() {
                return position;
            }
        };
        
        assertTrue(attributeContext.nextNode());
    }

}