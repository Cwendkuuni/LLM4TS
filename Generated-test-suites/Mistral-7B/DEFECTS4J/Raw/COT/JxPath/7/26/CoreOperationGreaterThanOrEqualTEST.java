package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 5.0;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 5.0;
                    }
                });

        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDifferentValues() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 5.0;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 4.0;
                    }
                });

        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNonNumericValues() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return "5";
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return "4";
                    }
                });
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 0.0;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return -1.0;
                    }
                });

        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).equals(Boolean.FALSE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return -5.0;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return -6.0;
                    }
                });

        assertTrue(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithWrongNumberOfArguments() {
        new CoreOperationGreaterThanOrEqual(new Expression[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArguments() {
        new CoreOperationGreaterThanOrEqual(new Expression[] { null, null });
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNullExpressionResult() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return null;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 5.0;
                    }
                });

        assertNull(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNullRightExpressionResult() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return 5.0;
                    }
                },
                new Expression() {
                    @Override
                    public Object computeValue(EvalContext arg) {
                        return null;
                    }
                });

        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }
}