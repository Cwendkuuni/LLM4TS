import static org.junit.Assert.*;
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
        boolean isContextDependent = expression.isContextDependent();
        assertFalse(isContextDependent);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);
        assertNull(value);
    }

    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        assertNull(result);
    }

    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersWithNullResult() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersWithEvalContextResult() {
        EvalContext evalContext = new EvalContext();
        Iterator result = expression.iteratePointers(evalContext);
        assertNotNull(result);
        assertFalse(result.hasNext());
    }

    @Test
    public void testPointerIteratorWithNullResult() {
        Iterator iterator = new Expression.PointerIterator(Collections.EMPTY_LIST.iterator(), null, null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIteratorWithNonNullResult() {
        Iterator iterator = new Expression.PointerIterator(Collections.singleton(new Pointer()), new QName(null, "name"), Locale.US);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIteratorWithNullResult() {
        Iterator iterator = new Expression.ValueIterator(Collections.EMPTY_LIST.iterator());
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueIteratorWithNonNullResult() {
        Iterator iterator = new Expression.ValueIterator(Collections.singleton(new Pointer()).iterator());
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

}