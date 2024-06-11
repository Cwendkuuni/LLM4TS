package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        context.createVariable("", "arg1", new Object());
        context.createVariable("", "arg2", new Object());
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", expression.getSymbol());
    }
}