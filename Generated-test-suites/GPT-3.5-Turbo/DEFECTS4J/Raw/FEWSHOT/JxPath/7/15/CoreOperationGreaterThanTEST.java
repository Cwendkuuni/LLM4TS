package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(3);
        
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {

        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(3);
        
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        
        assertEquals(">", expression.getSymbol());
    }
}