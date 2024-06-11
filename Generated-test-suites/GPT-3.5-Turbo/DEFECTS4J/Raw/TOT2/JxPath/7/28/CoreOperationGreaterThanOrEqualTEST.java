
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_withGreaterThanOrEqual_shouldReturnTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(5), new Constant(3));

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_withGreaterThan_shouldReturnFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(3), new Constant(5));

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_shouldReturnGreaterThanOrEqualSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(10), new Constant(5));

        assertEquals(">=", operation.getSymbol());
    }

    @Test(expected = ArithmeticException.class)
    public void testComputeValue_withNonNumericInputs_shouldThrowException() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Variable("hello"), new Variable("world"));

        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }
}
