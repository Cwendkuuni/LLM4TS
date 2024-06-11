package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext evalContext = new EvalContext();
        greaterThanOrEqual.computeValue(evalContext);
        // Add more test cases to cover different scenarios
        
        // Test case 1: l < r
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(2), new Expression(5));
        evalContext = new EvalContext();
        assertEquals(false, greaterThanOrEqual.computeValue(evalContext));

        // Test case 2: l = r
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(5));
        evalContext = new EvalContext();
        assertEquals(true, greaterThanOrEqual.computeValue(evalContext));

        // Test case 3: l > r
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(10), new Expression(5));
        evalContext = new EvalContext();
        assertEquals(true, greaterThanOrEqual.computeValue(evalContext));

        // Add more test cases to cover different scenarios
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", greaterThanOrEqual.getSymbol());
    }
}