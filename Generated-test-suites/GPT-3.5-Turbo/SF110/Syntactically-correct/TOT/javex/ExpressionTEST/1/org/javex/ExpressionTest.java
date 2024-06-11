import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("3 + 4");
    }

    @Test
    public void testGetExternalFunctions() {
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        assertNotNull(expression.stack);
        assertFalse(expression.prepared);
        assertEquals("3 + 4", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack.add("3");
        expression.stack.add("+");
        expression.stack.add("4");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
        assertEquals("3", expression.stack.get(0));
        assertEquals("4", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
    }

    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("+"));
        assertNull(Expression.getReservedFunctions("nonexistent"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("nonexistent"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("+"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("toLowerCase"));
        assertFalse(expression.isStringFunction("+"));
    }

    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction("nonexistent"));
    }

    @Test
    public void testIsExternalFunction() {
        assertFalse(expression.isExternalFunction("nonexistent"));
    }

    @Test
    public void testGetFunction() {
        assertNotNull(expression.getFunction("and"));
        assertNull(expression.getFunction("nonexistent"));
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("+"));
    }

    @Test
    public void testExpressionDefaultConstructor() {
        Expression expressionDefault = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertFalse(expressionDefault.prepared);
    }

    @Test
    public void testStack() {
        expression.stack("(abc", expression.stack);
        assertEquals(1, expression.stack.size());
        assertEquals("(abc", expression.stack.get(0));

        expression.stack(")", expression.stack);
        assertEquals(1, expression.stack.size());
        assertEquals("(abc)", expression.stack.get(0));

        expression.stack("-5", expression.stack);
        assertEquals(2, expression.stack.size());
        assertEquals("-5abc)", expression.stack.get(0));
        assertEquals("(abc)", expression.stack.get(1));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression.ExpressionStack stackCopy = expression.getStackCopy();
        assertNotSame(expression.stack, stackCopy);
        assertEquals(expression.stack.size(), stackCopy.size());
        for (int i = 0; i < expression.stack.size(); i++) {
            assertNotSame(expression.stack.get(i), stackCopy.get(i));
            assertEquals(expression.stack.get(i), stackCopy.get(i));
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        expression.evaluate();
        // Testing for exception, actual return value doesn't matter as the function is abstract
    }

    @Test
    public void testTokenize() {
        expression.tokenize();
        assertEquals(3, expression.stack.size());
        assertEquals("3", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("4", expression.stack.get(2));
    }

}