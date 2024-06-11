import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("and"));
        assertNull(Expression.getReservedFunctions("test"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("test"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("test"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("test"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("test"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isExternalFunction("and"));
        assertFalse(expression.isExternalFunction("test"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isFunction("test"));
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testEvaluate() {
        Expression expression = new Expression("test");
        assertThrows(Exception.class, () -> expression.evaluate());
    }

}