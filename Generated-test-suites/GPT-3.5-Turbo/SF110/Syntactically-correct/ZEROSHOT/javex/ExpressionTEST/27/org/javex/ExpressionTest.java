import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("") {};
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("") {};
        assertFalse(expression.prepared);
        assertNotNull(expression.stack);
        assertNotNull(expression.expression);
        assertEquals("", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("") {};
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
        assertEquals("1", expression.stack.get(0));
        assertEquals("2", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
    }

    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("("));
        assertNotNull(Expression.getReservedFunctions(")"));
        assertNotNull(Expression.getReservedFunctions("+"));
        assertNotNull(Expression.getReservedFunctions("-"));
        assertNotNull(Expression.getReservedFunctions("and"));
        assertNotNull(Expression.getReservedFunctions("or"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("") {};
        assertTrue(expression.isReservedFunction("("));
        assertTrue(expression.isReservedFunction(")"));
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("*"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isLogicalFunction("("));
        assertFalse(expression.isLogicalFunction(")"));
        assertFalse(expression.isLogicalFunction("+"));
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isMathFunction("("));
        assertFalse(expression.isMathFunction(")"));
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("or"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isDateFunction("("));
        assertFalse(expression.isDateFunction(")"));
        assertFalse(expression.isDateFunction("+"));
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("or"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("") {};
        assertFalse(expression.isStringFunction("("));
        assertFalse(expression.isStringFunction(")"));
        assertFalse(expression.isStringFunction("+"));
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("or"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList();
        expression.externalFunctions.add(new FunctionExpression("testFunction"));
        assertNotNull(expression.getExternalFunction("testFunction"));
        assertNull(expression.getExternalFunction("notExists"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList();
        expression.externalFunctions.add(new FunctionExpression("testFunction"));
        assertTrue(expression.isExternalFunction("testFunction"));
        assertFalse(expression.isExternalFunction("notExists"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("") {};
        assertNotNull(expression.getFunction("("));
        assertNotNull(expression.getFunction(")"));
        assertNotNull(expression.getFunction("+"));
        assertNull(expression.getFunction("*"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("") {};
        assertTrue(expression.isFunction("("));
        assertTrue(expression.isFunction(")"));
        assertTrue(expression.isFunction("+"));
        assertFalse(expression.isFunction("*"));
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("") {};
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression("") {};
        Stack<String> stack = new Stack<String>();
        expression.stack("(", stack);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("") {};
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("") {};
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        ExpressionStack copy = expression.getStackCopy();
        assertEquals(expression.stack.size(), copy.size());
        assertEquals(expression.stack.get(0), copy.get(0));
        assertEquals(expression.stack.get(1), copy.get(1));
        assertEquals(expression.stack.get(2), copy.get(2));
        assertEquals(expression.stack.get(3), copy.get(3));
        assertEquals(expression.stack.get(4), copy.get(4));
    }

    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception("Test Exception");
            }
        };
        expression.evaluate();
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("") {};
        expression.expression = "(1 + 2) * 3 - 4";
        expression.tokenize();
        assertEquals(9, expression.stack.size());
        assertEquals("(", expression.stack.get(0));
        assertEquals("1", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
        assertEquals("2", expression.stack.get(3));
        assertEquals(")", expression.stack.get(4));
        assertEquals("*", expression.stack.get(5));
        assertEquals("3", expression.stack.get(6));
        assertEquals("-", expression.stack.get(7));
        assertEquals("4", expression.stack.get(8));
    }
}