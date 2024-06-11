import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
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
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "computed value";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Object result = expression.computeValue(null);
        Assert.assertEquals("computed value", result);
    }

    @Test
    public void testCompute() {
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
                return "computed result";
            }
        };

        Object result = expression.compute(null);
        Assert.assertEquals("computed result", result);
    }

    @Test
    public void testIterate() {
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
                return new Object[]{1, 2, 3};
            }
        };

        Object[] expected = new Object[]{1, 2, 3};
        int count = 0;
        for (Object value : expression.iterate(null)) {
            Assert.assertEquals(expected[count], value);
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testIteratePointers_nullResult() {
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

        Assert.assertFalse(expression.iteratePointers(null).hasNext());
    }

    @Test
    public void testIteratePointers_nonNullResult() {
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
                return new Object[]{1, 2, 3};
            }
        };

        Object[] expected = new Object[]{1, 2, 3};
        int count = 0;
        for (Object value : expression.iteratePointers(null)) {
            Assert.assertEquals(expected[count], value);
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIteratorRemove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        iterator.remove();
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Object[]{1, 2, 3});
        Object[] expected = new Object[]{1, 2, 3};
        int count = 0;
        for (Object value : iterator) {
            Assert.assertEquals(expected[count], value);
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIteratorRemove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        iterator.remove();
    }
}