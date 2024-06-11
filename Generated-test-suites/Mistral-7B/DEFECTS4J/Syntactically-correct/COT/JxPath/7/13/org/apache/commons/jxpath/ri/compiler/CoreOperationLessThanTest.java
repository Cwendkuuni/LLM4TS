package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Create a CoreOperationLessThan instance with valid arguments
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Check that the instance was created correctly
        assertEquals("<", coreOperationLessThan.getSymbol());
    }

    /**
     * Test computeValue method with valid arguments.
     */
    @Test
    public void testComputeValueValidArguments() {
        // Create a CoreOperationLessThan instance with valid arguments
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10.0;
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context = new EvalContext() {};
        // Test method
        Object result = coreOperationLessThan.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with arguments in the same order but with the second argument being smaller.
     */
    @Test
    public void testComputeValueSecondArgumentSmaller() {
        // Create a CoreOperationLessThan instance with valid arguments
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3.0;
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context = new EvalContext() {};
        // Test method
        Object result = coreOperationLessThan.computeValue(context);
        // Verify the result
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with arguments in the same order but with the same value.
     */
    @Test
    public void testComputeValueSameValue() {
        // Create a CoreOperationLessThan instance with valid arguments
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context = new EvalContext() {};
        // Test method
        Object result = coreOperationLessThan.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with non-numeric arguments.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueNonNumericArguments() {
        // Create a CoreOperationLessThan instance with valid arguments
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "5";
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "10";
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context = new EvalContext() {};
        // Test method
        coreOperationLessThan.computeValue(context);
    }

    /**
     * Test computeValue method with NaN as first or second argument.
     */
    @Test
    public void testComputeValueNaNArguments() {
        // Create a CoreOperationLessThan instance with valid arguments, first argument as NaN
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.NaN;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10.0;
            }
        };
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context = new EvalContext() {};
        // Test method
        Object result = coreOperationLessThan.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);

        // Create a CoreOperationLessThan instance with valid arguments, second argument as NaN
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.NaN;
            }
        };
        CoreOperationLessThan coreOperationLessThan2 = new CoreOperationLessThan(arg1, arg2);
        // Create a mock EvalContext
        EvalContext context2 = new EvalContext() {};
        // Test method
        Object result2 = coreOperationLessThan2.computeValue(context2);
        // Verify the result
        assertEquals(Boolean.FALSE, result2);
    }
}