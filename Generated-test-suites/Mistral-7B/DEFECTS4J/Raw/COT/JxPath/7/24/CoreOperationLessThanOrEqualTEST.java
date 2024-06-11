package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
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

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        boolean result = coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean && ((Boolean) coreOperationLessThanOrEqual.computeValue(new EvalContext())).booleanValue() == false;
        assertTrue(result);

        arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };

        arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean && ((Boolean) coreOperationLessThanOrEqual.computeValue(new EvalContext())).booleanValue() == true;
        assertTrue(result);

        arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.NaN);
            }
        };

        arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.NaN);
            }
        };

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean && ((Boolean) coreOperationLessThanOrEqual.computeValue(new EvalContext())).booleanValue() == false;
        assertTrue(result);

        arg1 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10.0;
            }
        });

        arg2 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        });

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean && ((Boolean) coreOperationLessThanOrEqual.computeValue(new EvalContext())).booleanValue() == true;
        assertTrue(result);

        arg1 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.POSITIVE_INFINITY);
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.NEGATIVE_INFINITY);
            }
        });

        arg2 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.NEGATIVE_INFINITY);
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.POSITIVE_INFINITY);
            }
        });

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext()) instanceof Boolean && ((Boolean) coreOperationLessThanOrEqual.computeValue(new EvalContext())).booleanValue() == false;
        assertTrue(result);

        arg1 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(Double.NaN);
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(5.0);
            }
        });

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(new EvalContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreOperationNumber(new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(5.0);
            }
        }, new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new RuntimeException();
            }
        });

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(new EvalContext());
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(5.0);
            }
        };

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualNullArg2() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(5.0);
            }
        };

        Expression arg2 = null;

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }
}