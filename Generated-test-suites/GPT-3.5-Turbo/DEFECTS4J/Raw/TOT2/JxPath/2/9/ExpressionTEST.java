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
    public void testComputeContextDependent() {
        assertFalse(expression.computeContextDependent());
    }

    @Test
    public void testComputeValue() {
        // Add test case here
    }

    @Test
    public void testCompute() {
        // Add test case here
    }

}

class TestExpression extends Expression {
    @Override
    public boolean computeContextDependent() {
        return false;
    }

    @Override
    public Object computeValue(EvalContext context) {
        // Implement test case logic here
        return null;
    }

    @Override
    public Object compute(EvalContext context) {
        // Implement test case logic here
        return null;
    }
}