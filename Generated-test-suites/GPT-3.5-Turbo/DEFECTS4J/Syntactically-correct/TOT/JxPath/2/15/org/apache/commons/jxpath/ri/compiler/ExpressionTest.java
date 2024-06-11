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

        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue_NullResult() {
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

        Assert.assertNull(expression.computeValue(null));
    }

    @Test
    public void testComputeValue_NonNullResult() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "result";
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Assert.assertEquals("result", expression.computeValue(null));
    }

    @Test
    public void testCompute_NullResult() {
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

        Assert.assertNull(expression.compute(null));
    }

    @Test
    public void testCompute_NonNullResult() {
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

        Assert.assertEquals("result", expression.compute(null));
    }

    @Test
    public void testIterate_NullResult() {
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

        Assert.assertFalse(expression.iterate(null).hasNext());
    }

    @Test
    public void testIterate_NonNullResult() {
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
                return new String[]{"value1", "value2", "value3"};
            }
        };

        Assert.assertTrue(expression.iterate(null).hasNext());
    }

    @Test
    public void testIteratePointers_NullResult() {
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
    public void testIteratePointers_NonNullResult() {
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
                return new String[]{"value1", "value2", "value3"};
            }
        };

        Assert.assertTrue(expression.iteratePointers(null).hasNext());
    }

    @Test
    public void testPointerIterator_HasNext() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator_Next_Pointer() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        Assert.assertNull(iterator.next());
    }

    @Test
    public void testPointerIterator_Next_NodePointer() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, new QName(null, "value"), Locale.US);

        Assert.assertNotNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPointerIterator_Remove() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

        iterator.remove();
    }

    @Test
    public void testValueIterator_HasNext() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator_Next_NotPointer() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        Assert.assertNull(iterator.next());
    }

    @Test
    public void testValueIterator_Next_Pointer() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(new Expression.PointerIterator(null, null, null));

        Assert.assertNotNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueIterator_Remove() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);

        iterator.remove();
    }
}