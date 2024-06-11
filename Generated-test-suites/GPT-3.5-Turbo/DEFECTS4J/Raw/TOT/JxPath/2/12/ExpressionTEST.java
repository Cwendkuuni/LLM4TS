import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent_WhenContextDependent_ThenReturnTrue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean result = expression.isContextDependent();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsContextDependent_WhenNotContextDependent_ThenReturnFalse() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        boolean result = expression.isContextDependent();

        Assert.assertFalse(result);
    }

    @Test
    public void testComputeValue_WhenNoResult_ThenReturnNull() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Object result = expression.computeValue(null);

        Assert.assertNull(result);
    }

    @Test
    public void testCompute_WhenNoResult_ThenReturnNull() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Object result = expression.compute(null);

        Assert.assertNull(result);
    }

    @Test
    public void testIterate_WhenResultIsEvalContext_ThenReturnIteratorOfEvalContext() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new EvalContext();
            }

            @Override
            public Object compute(EvalContext context) {
                return new EvalContext();
            }
        };

        Iterator result = expression.iterate(null);

        Assert.assertTrue(result instanceof Expression.ValueIterator);
    }

    @Test
    public void testIterate_WhenResultIsNull_ThenReturnEmptyIterator() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Iterator result = expression.iterate(null);

        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers_WhenResultIsEvalContext_ThenReturnIteratorOfEvalContext() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new EvalContext();
            }

            @Override
            public Object compute(EvalContext context) {
                return new EvalContext();
            }
        };

        Iterator result = expression.iteratePointers(null);

        Assert.assertTrue(result instanceof Expression.PointerIterator);
    }

    @Test
    public void testIteratePointers_WhenResultIsNull_ThenReturnEmptyIterator() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Iterator result = expression.iteratePointers(null);

        Assert.assertFalse(result.hasNext());
    }

}