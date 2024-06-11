package org.javex;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
    
    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        String s = "Test Expression";
        Expression exp = new Expression(s) {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNotNull(exp);
        assertEquals(s, exp.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.stack.add("/");
        expression.stack.add("4");
        
        expression.doEvaluationStack();
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("1");
        expected.add("2");
        expected.add("+");
        expected.add("3");
        expected.add("4");
        expected.add("/");
        expected.add("*");
        
        assertEquals(expected, expression.stack);
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "and";
        FunctionExpression result = Expression.getReservedFunctions(simbol);
        assertNotNull(result);
    }

    @Test
    public void testIsReservedFunction() {
        String simbol = "and";
        boolean result = expression.isReservedFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        String simbol = "and";
        boolean result = expression.isLogicalFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        String simbol = "/";
        boolean result = expression.isMathFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        String simbol = "dateAdd";
        boolean result = expression.isDateFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        String simbol = "startsWith";
        boolean result = expression.isStringFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testGetExternalFunction() {
        String simbol = "startsWith";
        FunctionExpression result = expression.getExternalFunction(simbol);
        assertNotNull(result);
    }

    @Test
    public void testIsExternalFunction() {
        String simbol = "startsWith";
        boolean result = expression.isExternalFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testGetFunction() {
        String simbol = "startsWith";
        FunctionExpression result = expression.getFunction(simbol);
        assertNotNull(result);
    }

    @Test
    public void testIsFunction() {
        String simbol = "startsWith";
        boolean result = expression.isFunction(simbol);
        assertTrue(result);
    }

    @Test
    public void testExpression1() {
        Expression exp = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNotNull(exp);
    }

    @Test
    public void testStack() {
        String c = "c";
        expression.stack = new ExpressionStack();
        expression.stack.push("a");
        expression.stack.push("b");
        
        expression.stack(c, expression.stack);
        
        ExpressionStack expected = new ExpressionStack();
        expected.push("a");
        expected.push("bc");
        
        assertEquals(expected, expression.stack);
    }

    @Test
    public void testPrepare() {
        expression.expression = "(1+2)*3/4";
        
        expression.prepare();
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("1");
        expected.add("2");
        expected.add("+");
        expected.add("3");
        expected.add("4");
        expected.add("/");
        expected.add("*");
        
        assertEquals(expected, expression.stack);
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        expression.stack.add("+");
        
        ExpressionStack result = expression.getStackCopy();
        assertNotNull(result);
        assertEquals(expression.stack, result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Object result = expression.evaluate();
        assertNull(result);
    }

    @Test
    public void testTokenize() {
        expression.expression = "1 + 2 - 3";
        
        expression.tokenize();
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("1");
        expected.add("+");
        expected.add("2");
        expected.add("-");
        expected.add("3");
        
        assertEquals(expected, expression.stack);
    }
}