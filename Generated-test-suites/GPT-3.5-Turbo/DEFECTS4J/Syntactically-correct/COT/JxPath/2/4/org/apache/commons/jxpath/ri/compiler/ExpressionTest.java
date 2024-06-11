
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

        // Assert the result is true
        assertTrue(result);
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

        // Assert the result is "value"
        assertEquals("value", result);
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

        // Assert the result is "result"
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
                return new String[]{"item1", "item2"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();

        Iterator iterator = expression.iterate(context);

        // Assert the iterator has next() method and returns "item1" and "item2"
        assertTrue(iterator.hasNext());
        assertEquals("item1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("item2", iterator.next());
        assertFalse(iterator.hasNext());
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
                return new String[]{"item1", "item2"};
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        EvalContext context = new EvalContext();

        Iterator iterator = expression.iteratePointers(context);

        // Assert the iterator has next() method and returns NodePointers with values "item1" and "item2"
        assertTrue(iterator.hasNext());
        assertEquals("item1", ((NodePointer) iterator.next()).getValue());
        assertTrue(iterator.hasNext());
        assertEquals("item2", ((NodePointer) iterator.next()).getValue());
        assertFalse(iterator.hasNext());
    }
}
