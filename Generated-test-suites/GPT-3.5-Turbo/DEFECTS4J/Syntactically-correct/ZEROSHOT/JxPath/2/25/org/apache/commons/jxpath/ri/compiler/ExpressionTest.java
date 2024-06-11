import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return true;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return "test";
            }
            public Object compute(EvalContext context) {
                return null;
            }
        };

        assertEquals("test", expression.computeValue(null));
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return "test";
            }
        };

        assertEquals("test", expression.compute(null));
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return new String[]{"test1", "test2", "test3"};
            }
        };

        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.hasNext());
        assertEquals("test1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            public boolean computeContextDependent() {
                return false;
            }
            public Object computeValue(EvalContext context) {
                return null;
            }
            public Object compute(EvalContext context) {
                return new String[]{"test1", "test2", "test3"};
            }
        };

        Iterator iterator = expression.iteratePointers(null);
        assertFalse(iterator.hasNext());
    }
}