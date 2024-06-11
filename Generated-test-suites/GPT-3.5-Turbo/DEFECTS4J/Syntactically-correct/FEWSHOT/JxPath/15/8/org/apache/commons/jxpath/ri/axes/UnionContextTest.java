package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        EvalContext[] evalContexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, evalContexts);
        
        assertEquals(1, unionContext.getDocumentOrder());
    }
    
    @Test
    public void testSetPosition() {
        EvalContext[] evalContexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, evalContexts);
        
        assertFalse(unionContext.setPosition(0));
    }
}