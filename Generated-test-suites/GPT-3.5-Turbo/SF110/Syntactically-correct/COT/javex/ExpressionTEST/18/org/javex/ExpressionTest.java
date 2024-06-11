
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("") {};
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("") {};
        assertNotNull(expression.stack);
        assertEquals("", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("") {};
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.doEvaluationStack();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("+");
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testPutFunction() {
        Expression expression = new Expression("") {};
        Map map = new TreeMap();
        expression.putFunction(new AndFunction(), map);
        assertTrue(map.containsKey("and"));
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression functionExpression = Expression.getReservedFunctions("and");
        assertEquals("and", functionExpression.getSimbol());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("") {};
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("xyz"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("") {};
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("xyz"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("xyz"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("xyz"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("xyz"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList();
        FunctionExpression functionExpression = new GenericFunctionExpression("func");
        expression.externalFunctions.add(functionExpression);
        FunctionExpression result = expression.getExternalFunction("func");
        assertEquals(functionExpression, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList();
        FunctionExpression functionExpression = new GenericFunctionExpression("func");
        expression.externalFunctions.add(functionExpression);
        assertTrue(expression.isExternalFunction("func"));
        assertFalse(expression.isExternalFunction("xyz"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("") {};
        FunctionExpression functionExpression = new GenericFunctionExpression("func");
        expression.externalFunctions = new ArrayList();
        expression.getExternalFunctions().add(functionExpression);
        FunctionExpression result = expression.getFunction("func");
        assertEquals(functionExpression, result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("") {};
        FunctionExpression functionExpression = new GenericFunctionExpression("func");
        expression.externalFunctions = new ArrayList();
        expression.externalFunctions.add(functionExpression);
        assertTrue(expression.isFunction("func"));
        assertFalse(expression.isFunction("xyz"));
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("") {};
        assertNull(expression.stack);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression("") {};
        Stack stack = new Stack();
        expression.stack("1", stack);
        assertEquals("1", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.prepare();
        assertTrue(expression.prepared);
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("+");
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("") {};
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        ExpressionStack stackCopy = expression.getStackCopy();
        assertEquals(expression.stack, stackCopy);
        assertNotSame(expression.stack, stackCopy);
    }

    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception();
            }
        };
        expression.evaluate();
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("") {};
        expression.expression = "2 + 3 - 4";
        expression.tokenize();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        expectedStack.add("-");
        expectedStack.add("4");
        assertEquals(expectedStack, expression.stack);
    }
}

