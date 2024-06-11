import static org.junit.Assert.*;
import org.junit.Test;
import org.javex.Expression;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression(""){};
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression(""){};
        assertEquals("", expression.expression);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.prepare();
        assertNotNull(expression.stack);
        assertTrue(expression.prepared);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.evaluate());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isReservedFunction(""));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isLogicalFunction(""));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isMathFunction(""));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isDateFunction(""));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isStringFunction(""));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isExternalFunction(""));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression(""){};
        assertNull(expression.getExternalFunction(""));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression(""){};
        assertNull(expression.getFunction(""));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression(""){};
        assertFalse(expression.isFunction(""));
    }

}