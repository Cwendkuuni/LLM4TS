package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        // Test 1: l < r
        arg1.setValue(2.0);
        arg2.setValue(3.0);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test 2: l >= r
        arg2.setValue(2.0);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", operation.getSymbol());
    }
}