import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new TestExpression();
        expression.prepare();
    }
    
    @Test
    public void testGetExternalFunctions() {
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructorWithExpressionString() {
        String input = "2 + 3 * 4";
        expression = new TestExpression(input);
        assertEquals(input, expression.expression);
        assertNotNull(expression.stack);
        assertNotNull(expression.externalFunctions);
    }
    
    @Test
    public void testDoEvaluationStack() {
        expression.stack.push("2");
        expression.stack.push("+");
        expression.stack.push("3");
        expression.stack.push("*");
        expression.stack.push("4");
        
        expression.doEvaluationStack();
        
        assertEquals("2", expression.stack.get(0));
        assertEquals("3", expression.stack.get(1));
        assertEquals("4", expression.stack.get(2));
        assertEquals("*", expression.stack.get(3));
        assertEquals("+", expression.stack.get(4));
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("and"));
        assertNotNull(Expression.getReservedFunctions("ln"));
        assertNull(Expression.getReservedFunctions("random"));
    }
    
    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("random"));
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
        assertFalse(expression.isDateFunction("contains"));
    }
    
    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("toLowerCase"));
        assertFalse(expression.isStringFunction("dateDiff"));
    }
    
    @Test
    public void testGetExternalFunction() {
        assertNotNull(expression.getExternalFunction("customFunction"));
        assertNull(expression.getExternalFunction("random"));
    }
    
    @Test
    public void testIsExternalFunction() {
        assertTrue(expression.isExternalFunction("customFunction"));
        assertFalse(expression.isExternalFunction("randomFunction"));
    }
    
    @Test
    public void testGetFunction() {
        assertNotNull(expression.getFunction("and"));
        assertNull(expression.getFunction("randomFunction"));
    }
    
    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("randomFunction"));
    }
    
    @Test
    public void testConstructorWithoutExpression() {
        expression = new TestExpression();
        assertNull(expression.expression);
        assertNotNull(expression.stack);
        assertNotNull(expression.externalFunctions);
    }
    
    @Test
    public void testStack() {
        Stack<String> stack = new Stack<String>();
        expression.stack("2", stack);
        assertEquals("2", stack.pop());
        
        stack.push("3");
        expression.stack("4", stack);
        assertEquals("34", stack.pop());
        
        stack.push("5");
        expression.stack("+", stack);
        assertEquals("5+", stack.pop());
    }
    
    @Test
    public void testPrepare() {
        expression.expression = "2 + 3 * 4";
        expression.prepare();
        
        assertNotNull(expression.stack);
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        expression.stack.push("2");
        expression.stack.push("+");
        expression.stack.push("3");
        
        ExpressionStack copy = expression.getStackCopy();
        
        assertEquals(expression.stack.size(), copy.size());
        assertEquals(expression.stack.get(0), copy.get(0));
        assertEquals(expression.stack.get(1), copy.get(1));
        assertEquals(expression.stack.get(2), copy.get(2));
    }
    
    @Test(expected = Exception.class)
    public void testEvaluateThrowsException() throws Exception {
        expression.evaluate();
    }
    
    @Test
    public void testTokenize() {
        expression.expression = "2+3*(4-5)";
        expression.tokenize();
        
        assertEquals("2", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("3", expression.stack.get(2));
        assertEquals("*", expression.stack.get(3));
        assertEquals("(", expression.stack.get(4));
        assertEquals("4", expression.stack.get(5));
        assertEquals("-", expression.stack.get(6));
        assertEquals("5", expression.stack.get(7));
        assertEquals(")", expression.stack.get(8));
    }

    private class TestExpression extends Expression {
        public TestExpression() {
            super();
        }
        
        public TestExpression(String s) {
            super(s);
        }
        
        public List getExternalFunctions() {
            return null;
        }
        
        public Object evaluate() throws Exception {
            return null;
        }
    }
}