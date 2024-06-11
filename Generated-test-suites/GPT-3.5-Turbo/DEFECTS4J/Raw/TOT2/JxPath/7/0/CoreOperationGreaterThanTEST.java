package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);  // typical use case
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context)); // 5 > 3

        arg1 = new Constant(3);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context)); // 3 not greater than 5

        // Edge cases
        arg1 = new Constant(Double.MAX_VALUE); // largest positive finite value
        arg2 = new Constant(Double.MIN_VALUE); // smallest positive non-zero value
        operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context)); // positive infinity > positive non-zero

        arg1 = new Constant(Double.NaN); // Not-a-Number
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context)); // NaN

        // Error scenario - If the arguments are not numeric
        arg1 = new Constant("abc"); // non-numeric
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        try {
            operation.computeValue(context); // should throw ClassCastException
            fail("Should have thrown exception for non-numeric arguments");
        } catch (ClassCastException ex) {
            // Expected exception
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operation.getSymbol());
    }
}