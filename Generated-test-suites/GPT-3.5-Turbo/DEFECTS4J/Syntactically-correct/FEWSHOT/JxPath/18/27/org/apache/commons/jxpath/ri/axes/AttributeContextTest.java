package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
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
    private NodeIterator nodeIterator;
    private NodePointer nodePointer;
    
    @Before
    public void setup() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeTest.class);
        nodeIterator = mock(NodeIterator.class);
        nodePointer = mock(NodePointer.class);
        
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentPosition(0);
        
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(nodeTest instanceof NodeNameTest).thenReturn(true);
        when(((NodeNameTest) nodeTest).getNodeName()).thenReturn(new QName("attribute"));
        when(parentContext.getCurrentNodePointer().attributeIterator(new QName("attribute"))).thenReturn(nodeIterator);
    }
    
    @Test
    public void testGetCurrentNodePointer() {
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertEquals(nodePointer, result);
    }
    
    @Test
    public void testReset() {
        attributeContext.reset();
        
        assertFalse(attributeContext.isSetStarted());
        assertEquals(null, attributeContext.getIterator());
        
        assertTrue(attributeContext.isReset());
    }
    
    @Test
    public void testSetPosition() {
        int position = 1;
        
        when(attributeContext.getCurrentPosition()).thenReturn(position - 1);
        
        boolean result = attributeContext.setPosition(position);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPosition_Reset() {
        int position = 0;
        
        when(attributeContext.getCurrentPosition()).thenReturn(position - 1);
        
        boolean result = attributeContext.setPosition(position);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetPosition_False() {
        int position = 2;
        
        when(attributeContext.getCurrentPosition()).thenReturn(position - 1);
        
        boolean result = attributeContext.setPosition(position);
        
        assertFalse(result);
    }
    
    @Test
    public void testNextNode() {
        attributeContext.setPosition(0);
        attributeContext.setStarted(false);
        attributeContext.setIterator(nodeIterator);
        
        when(nodeIterator.setPosition(1)).thenReturn(true);
        when(nodeIterator.getNodePointer()).thenReturn(nodePointer);
        
        boolean result = attributeContext.nextNode();
        
        assertTrue(result);
        assertEquals(1, attributeContext.getCurrentPosition());
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testNextNode_False_1() {
        attributeContext.setPosition(0);
        attributeContext.setStarted(false);
        attributeContext.setIterator(null);
        
        boolean result = attributeContext.nextNode();
        
        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }
    
    @Test
    public void testNextNode_False_2() {
        attributeContext.setPosition(0);
        attributeContext.setStarted(false);
        attributeContext.setIterator(nodeIterator);
        
        when(nodeIterator.setPosition(1)).thenReturn(false);
        
        boolean result = attributeContext.nextNode();
        
        assertFalse(result);
        assertEquals(0, attributeContext.getCurrentPosition());
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

}