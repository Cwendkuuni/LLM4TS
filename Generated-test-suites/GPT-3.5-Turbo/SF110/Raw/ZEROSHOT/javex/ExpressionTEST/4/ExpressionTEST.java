package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("2 + 3");
        
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
        assertEquals("2 + 3", expression.expression);
        assertNotNull(expression.stack);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        expression.stack.add(")");
        
        expression.doEvaluationStack();
        
        assertEquals(new ExpressionStack(Arrays.asList("2", "3", "+")), expression.stack);
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = Expression.getReservedFunctions("+");
        assertTrue(function instanceof PlusFunction);
        
        function = Expression.getReservedFunctions("and");
        assertTrue(function instanceof AndFunction);
        
        function = Expression.getReservedFunctions("invalid");
        assertNull(function);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("invalid"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("abs"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isStringFunction("length"));
        assertFalse(expression.isStringFunction("abs"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        List externalFunctions = new ArrayList();
        externalFunctions.add(new LengthFunction());
        externalFunctions.add(new BetweenFunction());
        expression.externalFunctions = externalFunctions;
        
        assertNotNull(expression.getExternalFunction("length"));
        assertNull(expression.getExternalFunction("abs"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        List externalFunctions = new ArrayList();
        externalFunctions.add(new LengthFunction());
        externalFunctions.add(new BetweenFunction());
        expression.externalFunctions = externalFunctions;
        
        assertTrue(expression.isExternalFunction("length"));
        assertFalse(expression.isExternalFunction("abs"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertNotNull(expression.getFunction("abs"));
        assertNull(expression.getFunction("invalid"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertTrue(expression.isFunction("abs"));
        assertFalse(expression.isFunction("invalid"));
    }
    
    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        Stack<String> stack = new Stack<String>();
        expression.stack("(", stack);
        
        assertEquals("(", stack.pop());
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.expression = "2 + 3";
        
        expression.prepare();
        
        assertTrue(expression.prepared);
        assertEquals(new ExpressionStack(Arrays.asList("2", "3", "+")), expression.stack);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.stack = new ExpressionStack(Arrays.asList("2", "3", "+"));
        
        ExpressionStack copy = expression.getStackCopy();
        assertEquals(expression.stack, copy);
        assertNotSame(expression.stack, copy);
    }
    
    @Test(expected=Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception("Error evaluating expression");
            }
        };
        
        expression.evaluate();
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        expression.expression = "(2 + 3)";
        
        expression.tokenize();
        
        assertEquals(new ExpressionStack(Arrays.asList("(", "2", "+", "3", ")")), expression.stack);
    }
}