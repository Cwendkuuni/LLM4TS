import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.TRUE;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.FALSE;
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualWithEqualValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.FALSE;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.FALSE;
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.FALSE;
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithNullArg2() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.FALSE;
            }
        };

        Expression arg2 = null;

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqualWithDifferentTypes() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.TRUE;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "String";
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithSameTypeAndDifferentValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(2);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithSameTypeAndSameValues() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualWithZero() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(0);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(0);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualWithZeroAndNonZero() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(0);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithNullAndNonNull() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualWithNullAndNull() {
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

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testCoreOperationNotEqualWithEmptyStringAndEmptyString() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "";
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "";
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualWithEmptyStringAndNonEmptyString() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "";
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "String";
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithNullAndString() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "String";
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualWithDoubleAndDouble() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.valueOf(1.1);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.valueOf(1.2);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithDoubleAndFloat() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.valueOf(1.1);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Float.valueOf(1.2f);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test
    public void testCoreOperationNotEqualWithFloatAndDouble() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Float.valueOf(1.1f);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Double.valueOf(1.2);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertSame(result, Boolean.FALSE);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationNotEqualWithIncompatibleTypes() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Boolean.TRUE;
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Integer.valueOf(1);
            }
        };

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }
}