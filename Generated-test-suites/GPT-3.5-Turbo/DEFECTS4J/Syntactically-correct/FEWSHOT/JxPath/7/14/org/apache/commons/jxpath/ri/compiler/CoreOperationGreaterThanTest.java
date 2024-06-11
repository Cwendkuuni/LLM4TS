package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1: l is greater than r
        context.setDoubleValue(5.0);
        double l = (Double) operation.args[0].computeValue(context);
        context.setDoubleValue(3.0);
        double r = (Double) operation.args[1].computeValue(context);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test case 2: l is equal to r
        context.setDoubleValue(5.0);
        l = (Double) operation.args[0].computeValue(context);
        context.setDoubleValue(5.0);
        r = (Double) operation.args[1].computeValue(context);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test case 3: r is greater than l
        context.setDoubleValue(3.0);
        l = (Double) operation.args[0].computeValue(context);
        context.setDoubleValue(5.0);
        r = (Double) operation.args[1].computeValue(context);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}