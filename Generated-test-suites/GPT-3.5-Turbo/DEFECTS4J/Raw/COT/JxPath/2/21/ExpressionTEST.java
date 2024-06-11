import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

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
                return "value";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals("value", result);
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
                return "result";
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        Assert.assertEquals("result", result);
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
                return new Object[]{1, 2, 3};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object[] expected = {1, 2, 3};
        int index = 0;
        Iterator iterator = expression.iterate(context);
        while (iterator.hasNext()) {
            Object result = iterator.next();
            Assert.assertEquals(expected[index], result);
            index++;
        }
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return new Object[]{1, 2, 3};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();
        Object[] expected = {1, 2, 3};
        int index = 0;
        Iterator iterator = expression.iteratePointers(context);
        while (iterator.hasNext()) {
            Object result = iterator.next();
            Assert.assertEquals(expected[index], result);
            index++;
        }
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(
                Collections.singletonList("value").iterator(),
                new QName(null, "value"),
                Locale.ENGLISH);

        Assert.assertTrue(iterator.hasNext());
        Object result = iterator.next();
        Assert.assertEquals("value", result);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(
                Collections.singletonList("value").iterator());

        Assert.assertTrue(iterator.hasNext());
        Object result = iterator.next();
        Assert.assertEquals("value", result);
        Assert.assertFalse(iterator.hasNext());
    }
}