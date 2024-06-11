import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setup() {
        expression = new Expression() {
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
    }

    @Test
    public void testIsContextDependent() {
        boolean result = expression.isContextDependent();
        assertFalse(result);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = createDummyEvalContext();
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCompute() {
        EvalContext context = createDummyEvalContext();
        Object result = expression.compute(context);
        assertNull(result);
    }

    @Test
    public void testIterate() {
        EvalContext context = createDummyEvalContext();
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = createDummyEvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Iterator iterator = new Expression.PointerIterator(null, null, null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Iterator iterator = new Expression.ValueIterator(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    private EvalContext createDummyEvalContext() {
        return new EvalContext();
    }
}