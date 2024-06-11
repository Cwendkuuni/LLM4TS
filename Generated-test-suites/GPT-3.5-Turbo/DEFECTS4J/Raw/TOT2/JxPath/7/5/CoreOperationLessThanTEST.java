
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));
    }

    @Test
    public void testComputeValue_NotLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(10), new Constant(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(1), new Constant(1));
        assertEquals("<", lessThan.getSymbol());
    }

    // Edge case: Testing for NaN values
    @Test
    public void testComputeValue_NaNValues() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(Double.NaN), new Constant(0));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    // Edge case: Testing for negative infinity
    @Test
    public void testComputeValue_NegativeInfinity() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(Double.NEGATIVE_INFINITY), new Constant(0));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));
    }

    // Exception handling: Testing for null values
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(null), new Constant(10));
        EvalContext context = new EvalContext();
        lessThan.computeValue(context);
    }
}
