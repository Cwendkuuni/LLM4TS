package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.DummyExpression;

import java.util.ArrayList;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @org.junit.Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Basic test case for CoreOperationGreaterThanOrEqual constructor
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationGreaterThanOrEqual);

        // Basic test case for computeValue method
        EvalContext context = new EvalContext(new InfoSet());
        double l = 5.0;
        double r = 4.0;
        Expression arg1Value = new DummyExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return l;
            }
        };
        Expression arg2Value = new DummyExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return r;
            }
        };
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqualTest = new CoreOperationGreaterThanOrEqual(arg1Value, arg2Value);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqualTest.computeValue(context);
        assertTrue(result);

        // Test edge cases and exception handling
        testEdgeCasesAndExceptionHandling(context, l, r, 0.0, Boolean.FALSE);
        testEdgeCasesAndExceptionHandling(context, l, r, Double.MAX_VALUE, Boolean.TRUE);
        testEdgeCasesAndExceptionHandling(context, l, r, Double.NaN, Boolean.FALSE);
        testEdgeCasesAndExceptionHandling(context, l, r, null, IllegalArgumentException.class);
        testEdgeCasesAndExceptionHandling(context, l, r, new DummyExpression(), IllegalArgumentException.class);
    }

    private void testEdgeCasesAndExceptionHandling(EvalContext context, double l, double r, double testValue, Boolean expectedResult) {
        Expression arg1Value = new DummyExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return l;
            }
        };
        Expression arg2Value = new DummyExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return r;
            }
        };

        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1Value, arg2Value);

        Expression arg1ValueTest = new DummyExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return testValue;
            }
        };

        try {
            Object result = coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()) {
                @Override
                public Expression getVariableValue(String name) {
                    if (CoreOperationGreaterThanOrEqualTest.class.getName().equals(name)) {
                        return arg1ValueTest;
                    }
                    return null;
                }
            });
            assertEquals(expectedResult, result);
        } catch (IllegalArgumentException e) {
            if (expectedResult == null) {
                assertTrue(e.getMessage().contains("Cannot compare 'null' with given expression"));
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @org.junit.Test
    public void testMergeTestCases() {
        // Add other test methods here if needed
    }

    @org.junit.Test
    public void testToString() {
        // Add test for toString method here if needed
    }
}