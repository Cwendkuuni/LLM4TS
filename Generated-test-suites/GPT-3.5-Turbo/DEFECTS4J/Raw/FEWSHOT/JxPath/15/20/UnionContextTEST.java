package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnionContextTest {
    
    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(null, contexts);
        
        int result = unionContext.getDocumentOrder();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testSetPosition() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        
        UnionContext unionContext = new UnionContext(null, contexts);
        
        boolean result = unionContext.setPosition(1);
        
        assertTrue(result);
    }
}