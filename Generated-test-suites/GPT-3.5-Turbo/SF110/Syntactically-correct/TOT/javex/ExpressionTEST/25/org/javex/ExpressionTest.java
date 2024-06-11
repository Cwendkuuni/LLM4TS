import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        assertNotNull(expression.getExternalFunctions());
        assertEquals(0, expression.getExternalFunctions().size());
    }

    @Test
    public void testExpressionConstructor() {
        expression = new Expression("1 + 2");
        assertNotNull(expression.getExternalFunctions());
        assertEquals(0, expression.getExternalFunctions().size());
        assertEquals("1 + 2", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
        assertEquals("1", expression.stack.get(0));
        assertEquals("2", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
    }

    @Test
    public void testPutFunction() {
        Expression.putFunction(new PlusFunction(), Expression.reservedFunctions);
        assertEquals(1, Expression.reservedFunctions.size());
    }

    @Test
    public void testGetReservedFunctions() {
        Expression.putFunction(new PlusFunction(), Expression.reservedFunctions);
        assertNotNull(Expression.getReservedFunctions("+"));
        assertNull(Expression.getReservedFunctions("-"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("-"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("-"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("dateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("equals"));
        assertFalse(expression.isStringFunction("endsWith"));
    }

    @Test
    public void testGetExternalFunction() {
        ExternalFunction externalFunction1 = new ExternalFunction();
        ExternalFunction externalFunction2 = new ExternalFunction();
        expression.externalFunctions.add(externalFunction1);
        expression.externalFunctions.add(externalFunction2);
        assertEquals(externalFunction1, expression.getExternalFunction("function1"));
        assertNull(expression.getExternalFunction("function3"));
    }

    @Test
    public void testIsExternalFunction() {
        ExternalFunction externalFunction = new ExternalFunction();
        expression.externalFunctions.add(externalFunction);
        assertTrue(expression.isExternalFunction("function"));
        assertFalse(expression.isExternalFunction("otherFunction"));
    }

    @Test
    public void testGetFunction() {
        assertEquals(new PlusFunction(), expression.getFunction("+"));
        assertNull(expression.getFunction("-"));
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("+"));
        assertFalse(expression.isFunction("-"));
    }

    @Test
    public void testExpressionConstructor2() {
        assertNotNull(expression.stack);
        assertNotNull(expression.externalFunctions);
    }

    @Test
    public void testStack() {
        Stack stack = new Stack();
        expression.stack("+", stack);
        assertEquals(1, stack.size());
        assertEquals("+", stack.peek());
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        ExpressionStack stack = expression.getStackCopy();
        assertEquals(expression.stack.size(), stack.size());
        assertEquals(expression.stack.get(0), stack.get(0));
    }

    @Test
    public void testEvaluate() throws Exception {
        assertNull(expression.evaluate());
    }

    @Test
    public void testTokenize() {
        expression.expression = "1 +2";
        expression.tokenize();
        assertEquals(3, expression.stack.size());
        assertEquals("1", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("2", expression.stack.get(2));
    }
}