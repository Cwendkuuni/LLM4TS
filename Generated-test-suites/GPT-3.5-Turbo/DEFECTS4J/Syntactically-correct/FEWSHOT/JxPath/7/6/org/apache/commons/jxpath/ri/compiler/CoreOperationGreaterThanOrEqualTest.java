
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
  
    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext(null);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }
}
