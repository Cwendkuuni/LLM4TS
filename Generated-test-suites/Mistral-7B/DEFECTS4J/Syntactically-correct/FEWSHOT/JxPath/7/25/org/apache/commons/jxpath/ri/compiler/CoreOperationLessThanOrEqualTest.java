package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_1() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_2() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_3() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(2.0), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_4() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public double doubleValue(int index) {
                return Double.NaN;
            }
        });
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_5() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_6() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(Double.NaN), new LiteralExpression(2.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_7() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_8() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public double doubleValue(int index) {
                return 2.0;
            }
        });
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_9() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new VariableExpression("x"));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public double doubleValue(int index) {
                return 2.0;
            }
        });
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_10() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new VariableExpression("y"));
        InfoSet is = new InfoSet() {
            @Override
            public double doubleValue(int index) {
                switch (index) {
                    case 0:
                        return 2.0;
                    case 1:
                        return 1.0;
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        };
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_11() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new VariableExpression("y"));
        InfoSet is = new InfoSet() {
            @Override
            public double doubleValue(int index) {
                switch (index) {
                    case 0:
                        return 1.0;
                    case 1:
                        return 2.0;
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        };
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_12() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new VariableExpression("x"));
        InfoSet is = new InfoSet() {
            @Override
            public double doubleValue(int index) {
                return 2.0;
            }
        };
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_13() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new LiteralExpression(1.0));
        InfoSet is = new InfoSet() {
            @Override
            public double doubleValue(int index) {
                return 2.0;
            }
        };
        EvalContext context = new EvalContext(is);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_14() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(null), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_15() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(null));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_16() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(null), new LiteralExpression(null));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_17() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(Double.MAX_VALUE), new LiteralExpression(1.0));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_18() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(1.0), new LiteralExpression(Double.MAX_VALUE));
        EvalContext context = new EvalContext(new InfoSet() {});
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}