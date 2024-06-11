import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("5 + 3");
        assertEquals(null, expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression("5 + 3");
        assertEquals("5 + 3", expression.expression);
        assertNotNull(expression.stack);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("5 + 3");
        expression.stack.push("5");
        expression.stack.push("+");
        expression.stack.push("3");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
        assertEquals("5", expression.stack.get(0));
        assertEquals("3", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression reservedFunction = Expression.getReservedFunctions("and");
        assertNotNull(reservedFunction);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("random"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("random"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isMathFunction("divide"));
        assertFalse(expression.isMathFunction("random"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("random"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isStringFunction("tolower"));
        assertFalse(expression.isStringFunction("random"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("5 + 3");
        assertNull(expression.getExternalFunction("random"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("5 + 3");
        assertFalse(expression.isExternalFunction("random"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression("5 + 3");
        assertNotNull(expression.getFunction("and"));
        assertNull(expression.getFunction("random"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression("5 + 3");
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("random"));
    }
    
    @Test
    public void testConstructor2() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("5 + 3");
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("5 + 3");
        expression.stack.push("5");
        expression.stack.push("+");
        expression.stack.push("3");
        ExpressionStack copy = expression.getStackCopy();
        assertEquals(expression.stack.size(), copy.size());
        for (int i = 0; i < expression.stack.size(); i++) {
            assertEquals(expression.stack.get(i), copy.get(i));
        }
    }
    
    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("5 + 3");
        expression.evaluate();
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression("5 + 3");
        expression.tokenize();
        assertEquals(3, expression.stack.size());
        assertEquals("5", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("3", expression.stack.get(2));
    }
}