package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();

        // Test case 1
        double l1 = 3.5;
        double r1 = 2.5;
        context.setDoubleValue(arg1, l1);
        context.setDoubleValue(arg2, r1);
        Object result1 = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result1);

        // Test case 2
        double l2 = 2.5;
        double r2 = 3.5;
        context.setDoubleValue(arg1, l2);
        context.setDoubleValue(arg2, r2);
        Object result2 = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result2);

        // Test case 3
        double l3 = 2.5;
        double r3 = 2.5;
        context.setDoubleValue(arg1, l3);
        context.setDoubleValue(arg2, r3);
        Object result3 = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}