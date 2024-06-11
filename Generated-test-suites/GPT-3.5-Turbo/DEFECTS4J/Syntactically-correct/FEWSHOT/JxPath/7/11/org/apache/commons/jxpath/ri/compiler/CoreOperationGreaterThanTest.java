package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression("5");
        Expression arg2 = new ConstantExpression("3");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new ConstantExpression("5");
        Expression arg2 = new ConstantExpression("3");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }
}