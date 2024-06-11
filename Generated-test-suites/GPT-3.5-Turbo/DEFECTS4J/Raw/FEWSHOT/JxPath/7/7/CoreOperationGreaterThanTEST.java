package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Create test cases for different inputs
        EvalContext context = new EvalContext();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));

        // Test case 1: arg1 > arg2
        double arg1 = 5.0;
        double arg2 = 2.5;
        Object expectedResult = Boolean.TRUE;
        Object result = operation.computeValue(context);
        assertEquals(expectedResult, result);

        // Test case 2: arg1 < arg2
        arg1 = 2.5;
        arg2 = 5.0;
        expectedResult = Boolean.FALSE;
        result = operation.computeValue(context);
        assertEquals(expectedResult, result);

        // Test case 3: arg1 = arg2
        arg1 = 5.0;
        arg2 = 5.0;
        expectedResult = Boolean.FALSE;
        result = operation.computeValue(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));
        String expectedSymbol = ">";
        String symbol = operation.getSymbol();
        assertEquals(expectedSymbol, symbol);
    }
}