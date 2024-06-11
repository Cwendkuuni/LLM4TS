import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new TestExpression();
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertNull(result);
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
        assertFalse(expression.iterate(context).hasNext());
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        assertFalse(expression.iteratePointers(context).hasNext());
    }

    private static class TestExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public boolean computeValue(EvalContext context) {
            return null;
        }

        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }
}