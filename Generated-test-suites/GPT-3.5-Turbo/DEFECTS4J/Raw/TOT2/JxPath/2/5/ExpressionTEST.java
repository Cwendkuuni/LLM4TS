import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new SampleExpression();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new SampleExpression();
        assertNotNull(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testCompute() {
        Expression expression = new SampleExpression();
        assertNotNull(expression.compute(new EvalContext()));
    }

    @Test
    public void testIterate() {
        Expression expression = new SampleExpression();
        assertNotNull(expression.iterate(new EvalContext()));
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new SampleExpression();
        assertNotNull(expression.iteratePointers(new EvalContext()));
    }
}

class SampleExpression extends Expression {

    @Override
    public boolean computeContextDependent() {
        return true;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return "test";
    }

    @Override
    public Object compute(EvalContext context) {
        return "test";
    }
}