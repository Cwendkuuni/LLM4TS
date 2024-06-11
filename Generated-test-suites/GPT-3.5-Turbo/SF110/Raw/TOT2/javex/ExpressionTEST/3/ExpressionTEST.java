import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("test");
        assertEquals("test", expression.expression);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("test");
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.doEvaluationStack();
        // Add assertion here
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getFunction("and"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isDateFunction("dateAdd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isStringFunction("toUpperCase"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isExternalFunction("test"));
    }

    @Test
    public void testEvaluate() {
        // Test evaluate method with proper input
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("test");
        ExpressionStack stackCopy = expression.getStackCopy();
        // Add assertion here
    }

    // Add more test cases for other methods and edge cases

}