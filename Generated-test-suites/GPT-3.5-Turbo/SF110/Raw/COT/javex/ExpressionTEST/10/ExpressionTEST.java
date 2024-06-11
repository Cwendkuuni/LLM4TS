import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new DummyExpression("");
        
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testExpression() {
        Expression expression = new DummyExpression("1 + 2");
        
        assertNotNull(expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        DummyExpression expression = new DummyExpression("1 + 2");
        expression.prepare();
        expression.doEvaluationStack();
        
        assertEquals(3, expression.stack.size());
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("+"));
    }
    
    @Test
    public void testIsReservedFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("/"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("+"));
    }
    
    @Test
    public void testIsMathFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isDateFunction("date"));
        assertFalse(expression.isDateFunction("+"));
    }
    
    @Test
    public void testIsStringFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isStringFunction("string"));
        assertFalse(expression.isStringFunction("+"));
    }
    
    @Test
    public void testGetExternalFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertNull(expression.getExternalFunction("not_exists"));
        assertNotNull(expression.getExternalFunction("function1"));
    }
    
    @Test
    public void testIsExternalFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertFalse(expression.isExternalFunction("not_exists"));
        assertTrue(expression.isExternalFunction("function1"));
    }
    
    @Test
    public void testGetFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertNotNull(expression.getFunction("and"));
        assertNotNull(expression.getFunction("+"));
        assertNotNull(expression.getFunction("function1"));
        assertNull(expression.getFunction("not_exists"));
    }
    
    @Test
    public void testIsFunction() {
        DummyExpression expression = new DummyExpression("");
        
        assertTrue(expression.isFunction("and"));
        assertTrue(expression.isFunction("+"));
        assertTrue(expression.isFunction("function1"));
        assertFalse(expression.isFunction("not_exists"));
    }
    
    @Test
    public void testExpression2() {
        Expression expression = new DummyExpression();
        
        assertNotNull(expression);
    }
    
    @Test
    public void testPrepare() {
        DummyExpression expression = new DummyExpression("1 + 2");
        expression.prepare();
        
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        DummyExpression expression = new DummyExpression("1 + 2");
        expression.prepare();
        ExpressionStack stackCopy = expression.getStackCopy();
        
        assertNotNull(stackCopy);
        assertEquals(expression.stack.size(), stackCopy.size());
    }
    
    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        DummyExpression expression = new DummyExpression("1 + 2");
        
        expression.evaluate();
    }
    
    @Test
    public void testTokenize() {
        DummyExpression expression = new DummyExpression("1 + 2");
        expression.tokenize();
        
        assertEquals(3, expression.stack.size());
    }
    
    private class DummyExpression extends Expression {
        public DummyExpression() {
            super();
        }
        
        public DummyExpression(String s) {
            super(s);
        }
        
        public DummyExpression getExternalFunction(String simbol) {
            return new DummyExpression();
        }
        
        public boolean isExternalFunction(String simbol) {
            return simbol.equals("function1");
        }
        
        public FunctionExpression getFunction(String simbol) {
            if (simbol.equals("and")) {
                return new AndFunction();
            } else if (simbol.equals("+")) {
                return new PlusFunction();
            } else if (simbol.equals("function1")) {
                return new DummyFunction();
            }
            return null;
        }
        
        public Object evaluate() throws Exception {
            throw new Exception("Not implemented");
        }
        
        private class DummyFunction extends FunctionExpression {
            public DummyFunction() {
                super("function1");
            }
        }
    }
}