package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual operation;
    private EvalContext context;
    
    @Before
    public void setUp() {
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(null);
    }
    
    @Test
    public void testComputeValue() {
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}