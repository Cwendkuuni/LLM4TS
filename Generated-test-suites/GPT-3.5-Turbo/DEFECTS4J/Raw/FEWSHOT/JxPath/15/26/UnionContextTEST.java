package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(null, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
        
        contexts = new EvalContext[1];
        contexts[0] = new EvalContext();
        unionContext = new UnionContext(null, contexts);
        assertEquals(0, unionContext.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = new EvalContext();
        contexts[1] = new EvalContext();
        UnionContext unionContext = new UnionContext(null, contexts);
        
        unionContext.setPosition(1);
        // Verify the result
        
        unionContext.setPosition(2);
        // Verify the result
        
        unionContext.setPosition(3);
        // Verify the result
    }
}