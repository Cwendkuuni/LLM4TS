
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    @Test
    public void testIsContextDependent_Literal() {
        Expression expression = new Literal(new Double(1));
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue_NullContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = null;
        try {
            expression.computeValue(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testComputeValue_EmptyContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = new EvalContext();
        try {
            expression.computeValue(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testComputeValue_Exception() {
        Expression expression = new Literal(new Throwable("Test Exception"));
        EvalContext context = new EvalContext();
        try {
            expression.computeValue(context);
            fail("Expected EvaluationException");
        } catch (EvaluationException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testIterate_NullContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = null;
        try {
            expression.iterate(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIterate_EmptyContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_Exception() {
        Expression expression = new Literal(new Throwable("Test Exception"));
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCompute_NullContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = null;
        try {
            expression.compute(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCompute_EmptyContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        assertNotNull(result);
    }

    @Test
    public void testCompute_Exception() {
        Expression expression = new Literal(new Throwable("Test Exception"));
        EvalContext context = new EvalContext();
        try {
            expression.compute(context);
            fail("Expected EvaluationException");
        } catch (EvaluationException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testIteratePointers_NullContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = null;
        try {
            expression.iteratePointers(context);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testIteratePointers_EmptyContext() {
        Expression expression = new Literal(new Double(1));
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_Exception() {
        Expression expression = new Literal(new Throwable("Test Exception"));
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }
}