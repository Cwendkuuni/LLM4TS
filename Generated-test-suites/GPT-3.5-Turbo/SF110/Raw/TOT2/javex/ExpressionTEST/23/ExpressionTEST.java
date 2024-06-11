import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression reservedFunc = Expression.getReservedFunctions("and");
        assertNotNull(reservedFunc);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isReservedFunction("and");
        assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isLogicalFunction("and");
        assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isMathFunction("divide");
        assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isDateFunction("dateAdd");
        assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isStringFunction("length");
        assertTrue(result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isExternalFunction("func1");
        assertFalse(result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        boolean result = expression.isFunction("startsWith");
        assertTrue(result);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        assertTrue(expression.isPrepared());
    }

    @Test
    public void testEvaluate() {
        Expression expression = new Expression("2+3");
        try {
            Object result = expression.evaluate();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.setExpression("2+3");
        expression.tokenize();
        assertNotNull(expression.getStack());
    }

    // Additional edge cases and error scenarios tests can be added here

}