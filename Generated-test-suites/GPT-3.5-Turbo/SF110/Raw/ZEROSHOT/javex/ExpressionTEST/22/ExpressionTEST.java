import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("") {};

        assertEquals(null, expression.getExternalFunctions());
    }

    @Test
    public void testConstructorWithExpressionParameter() {
        Expression expression = new Expression("") {};

        assertEquals(false, expression.prepared);
        assertEquals("", expression.expression);
        assertNotNull(expression.stack);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("") {};

        expression.stack.push("test1");
        expression.stack.push("test2");
        expression.stack.push("test3");

        expression.doEvaluationStack();

        assertEquals("test1", expression.stack.pop());
        assertEquals("test2", expression.stack.pop());
        assertEquals("test3", expression.stack.pop());
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression functionExpression = Expression.getReservedFunctions("test");

        assertNull(functionExpression);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isReservedFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isLogicalFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isMathFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isDateFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isStringFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("") {};

        FunctionExpression functionExpression = expression.getExternalFunction("test");

        assertNull(functionExpression);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isExternalFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("") {};

        FunctionExpression functionExpression = expression.getFunction("test");

        assertNull(functionExpression);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("") {};

        boolean result = expression.isFunction("test");

        assertEquals(false, result);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };

        assertEquals(false, expression.prepared);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expression.prepare();

        assertEquals(true, expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("") {};

        ExpressionStack stack = expression.getStackCopy();

        assertNotNull(stack);
        assertEquals(0, stack.size());
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Object result = expression.evaluate();

        assertNull(result);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression("") {};

        Stack<String> stack = new Stack<>();
        stack.push("test");

        expression.stack("t", stack);

        assertEquals(1, stack.size());
        assertEquals("testt", stack.pop());
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("") {};

        expression.stack.push("test1");
        expression.stack.push("test2");
        expression.stack.push("test3");

        expression.tokenize();

        assertEquals("test1", expression.stack.pop());
        assertEquals("test2", expression.stack.pop());
        assertEquals("test3", expression.stack.pop());
    }

}