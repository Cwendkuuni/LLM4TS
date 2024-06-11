package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        // Test case 1
        double argValue1 = 5.0;
        double argValue2 = 3.0;
        arg1.setValue(argValue1);
        arg2.setValue(argValue2);
        Object result1 = op.computeValue(null);
        assertEquals(Boolean.TRUE, result1);
        
        // Test case 2
        argValue1 = 3.0;
        argValue2 = 5.0;
        arg1.setValue(argValue1);
        arg2.setValue(argValue2);
        Object result2 = op.computeValue(null);
        assertEquals(Boolean.FALSE, result2);
        
        // Test case 3
        argValue1 = 5.0;
        argValue2 = 5.0;
        arg1.setValue(argValue1);
        arg2.setValue(argValue2);
        Object result3 = op.computeValue(null);
        assertEquals(Boolean.TRUE, result3);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = op.getSymbol();
        assertEquals(">=", symbol);
    }
}