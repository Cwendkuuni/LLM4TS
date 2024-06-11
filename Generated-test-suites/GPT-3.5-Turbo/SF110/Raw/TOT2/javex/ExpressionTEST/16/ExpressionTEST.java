import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new CustomExpression("test");
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new CustomExpression("1 + 2 * 3");
        expression.prepare();
        // Add assertions based on expected behavior after evaluation stack
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new CustomExpression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("xyz"));
    }

    // Add more individual test cases for other methods in the Expression class

}