package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqualConstructor() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4.0;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationGreaterThanOrEqual);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationGreaterThanOrEqual.getClass());
        assertEquals(2, coreOperationGreaterThanOrEqual.getArguments().length);
    }

    /**
     * Test computeValue method with positive values.
     */
    @Test
    public void testComputeValueWithPositiveValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4.0;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue method with negative values.
     */
    @Test
    public void testComputeValueWithNegativeValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return -3.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return -2.0;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test computeValue method with zero value and another value.
     */
    @Test
    public void testComputeValueWithZeroValueAndAnotherValue() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 0.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4.0;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test computeValue method with two equal values.
     */
    @Test
    public void testComputeValueWithTwoEqualValues() {
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
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue method with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    /**
     * Test computeValue method with non-numeric values.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "five";
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "four";
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression[0], new Expression[0]);
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);
    }
}