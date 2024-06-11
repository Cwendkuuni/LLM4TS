package org.javex;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(null, expression.getExternalFunctions());
    }
    
    @Test
    public void testExpressionWithValidString() {
        expression = new Expression("1 + 2");
        assertEquals("1 + 2", expression.expression);
    }

    @Test
    public void testExpressionWithEmptyString() {
        expression = new Expression("");
        assertEquals("", expression.expression);
    }
    
    @Test
    public void testDoEvaluationStackWithEmptyStack() {
        expression.stack.clear();
        expression.doEvaluationStack();
        assertTrue(expression.stack.isEmpty());
    }
    
    @Test
    public void testDoEvaluationStackWithNonEmptyStack() {
        expression.stack.push("5");
        expression.stack.push("+");
        expression.stack.push("3");
        expression.doEvaluationStack();
        assertEquals("5 3 +", expression.stack.toString());
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertEquals(null, expression.getReservedFunctions(""));
    }
    
    @Test
    public void testIsReservedFunction() {
        assertFalse(expression.isReservedFunction(""));
    }
    
    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction(""));
    }
    
    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction(""));
    }
    
    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction(""));
    }
    
    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction(""));
    }
    
    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction(""));
    }
    
    @Test
    public void testIsExternalFunction() {
        assertFalse(expression.isExternalFunction(""));
    }
    
    @Test
    public void testGetFunction() {
        assertNull(expression.getFunction(""));
    }
    
    @Test
    public void testIsFunction() {
        assertFalse(expression.isFunction(""));
    }
    
    @Test
    public void testEmptyConstructor() {
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testStackWithNonEmptyStack() {
        expression.stack.push("a");
        expression.stack("b", expression.stack);
        assertEquals("ab", expression.stack.peek());
    }
    
    @Test
    public void testStackWithEmptyStack() {
        expression.stack("");
        assertEquals("", expression.stack.peek());
    }
    
    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        expression.stack.push("a");
        expression.stack.push("b");
        ExpressionStack copyStack = expression.getStackCopy();
        assertFalse(copyStack.isEmpty());
        assertEquals(2, copyStack.size());
        assertEquals("a", copyStack.get(0));
        assertEquals("b", copyStack.get(1));
    }
    
    @Test
    public void testEvaluate() {
        try {
            expression.evaluate();
            fail("An exception should be thrown");
        } catch (Exception e) {
            assertEquals("Not implemented", e.getMessage());
        }
    }
    
    @Test
    public void testTokenizeWithEmptyExpression() {
        expression.tokenize();
        assertTrue(expression.stack.isEmpty());
    }
    
    @Test
    public void testTokenizeWithNonEmptyExpression() {
        expression.expression = "1 + 2";
        expression.tokenize();
        assertFalse(expression.stack.isEmpty());
        assertEquals("1", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("2", expression.stack.get(2));
    }
    
}