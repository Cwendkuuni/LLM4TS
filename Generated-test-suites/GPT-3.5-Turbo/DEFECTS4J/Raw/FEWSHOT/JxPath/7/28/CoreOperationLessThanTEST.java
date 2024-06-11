package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test less than
        context.setContextNodeSet(new NodeSet());
        NodeSet arg1 = new NodeSet();
        arg1.add(new Node());
        context.setCurrentNode(arg1);
        NodeSet arg2 = new NodeSet();
        arg2.add(new Node());
        context.setCurrentNode(arg2);
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        // Test not less than
        context.setContextNodeSet(new NodeSet());
        arg1 = new NodeSet();
        arg1.add(new Node());
        context.setCurrentNode(arg1);
        arg2 = new NodeSet();
        arg2.add(new Node());
        context.setCurrentNode(arg2);
        result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", lessThan.getSymbol());
    }
}