import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression("1 + 2") {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertEquals("1 + 2", expression.expression);
        assertFalse(expression.prepared);
        assertNull(expression.stack);
    }
    
    @Test
    public void testConstructorWithoutParams() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2") {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.prepare();
        
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("1 + 2") {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertNull(expression.evaluate());
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        ExpressionStack copy = expression.getStackCopy();
        
        assertEquals("1", copy.get(0));
        assertEquals("+", copy.get(1));
        assertEquals("2", copy.get(2));
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("foo"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("foo"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("foo"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("foo"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isStringFunction("starts with"));
        assertFalse(expression.isStringFunction("foo"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertNull(expression.getExternalFunction("foo"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertFalse(expression.isExternalFunction("foo"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertNull(expression.getFunction("foo"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertFalse(expression.isFunction("foo"));
    }
}