import org.junit.Test;
import static org.junit.Assert.*;

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

        boolean isContextDependent = expression.isContextDependent();
        assertTrue(isContextDependent);
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

        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);
        assertEquals("value", result);
    }

    @Test
    public void testCompute() {
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
                return "result";
            }
        };

        EvalContext context = new EvalContext(null);
        Object result = expression.compute(context);
        assertEquals("result", result);
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

        EvalContext context = new EvalContext(null);
        int count = 0;
        Iterator iterator = expression.iterate(context);

        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(3, count);
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
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return new Object[]{1, 2, 3};
            }
        };

        EvalContext context = new EvalContext(null);
        int count = 0;
        Iterator iterator = expression.iteratePointers(context);

        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testPointerIterator() {
        Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression.ValueIterator iterator = new Expression.ValueIterator(null);
        assertFalse(iterator.hasNext());
    }

}