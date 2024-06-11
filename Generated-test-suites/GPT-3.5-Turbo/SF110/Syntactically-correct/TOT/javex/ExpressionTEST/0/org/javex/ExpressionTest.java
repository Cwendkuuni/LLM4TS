
package org.javex.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.javex.Expression;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            
            public Object evaluate() {
                return null;
            }
        };
        
        assertNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("2+3") {
            
            public Object evaluate() {
                return null;
            }
        };
        
        assertNotNull(expression.getStackCopy());
        assertEquals("2+3", expression.getStackCopy().get(0));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            
            public Object evaluate() {
                return null;
            }
        };
        
        assertFalse(expression.isMathFunction("and"));
        assertTrue(expression.isMathFunction("+"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            
            public Object evaluate() {
                return null;
            }
        };
        
        assertNull(expression.getExternalFunction("sum"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            
            public Object evaluate() {
                return null;
            }
        };
        
        assertFalse(expression.isExternalFunction("sum"));
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression("2+3") {
            
            public Object evaluate() throws Exception {
                return 5;
            }
        };
        
        try {
            assertEquals(5, expression.evaluate());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("2+3") {
            
            public Object evaluate() {
                return null;
            }
        };
        
        expression.prepare();
        assertEquals("+", expression.stack.get(1));
    }
}

