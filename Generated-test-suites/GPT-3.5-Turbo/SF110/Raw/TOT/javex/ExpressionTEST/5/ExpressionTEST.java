package org.javex;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression();
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("1 + 2");
        assertNotNull(expression.stack);
        assertNotNull(expression.expression);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2");
        expression.stack = new ExpressionStack();
        expression.stack.push("1");
        expression.stack.push("+");
        expression.stack.push("2");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
        assertEquals("+", expression.stack.get(0));
        assertEquals("1", expression.stack.get(1));
        assertEquals("2", expression.stack.get(2));
    }

    @Test
    public void testPutFunction() {
        Expression expression = new Expression();
        assertNull(expression.reservedFunctions);
        FunctionExpression f = new AndFunction();
        Map map = new TreeMap();
        expression.putFunction(f, map);
        assertNotNull(map.get(f));
        assertEquals(f, map.get(f));
    }

    @Test
    public void testGetReservedFunctions() {
        Expression expression = new Expression();
        assertNotNull(expression.getReservedFunctions("and"));
        assertNotEquals(expression.getReservedFunctions("and").getClass(), GenericFunctionExpression.class);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("sum"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isStringFunction("startsWith"));
        assertFalse(expression.isStringFunction("and"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        assertNotNull(expression.getExternalFunction("and"));
        assertNull(expression.getExternalFunction("sum"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        assertTrue(expression.isExternalFunction("and"));
        assertFalse(expression.isExternalFunction("sum"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        assertNotNull(expression.getFunction("and"));
        assertNull(expression.getFunction("sum"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("sum"));
    }

    @Test
    public void testConstructor2() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression();
        Stack stack = new Stack();
        expression.stack("(", stack);
        assertEquals(1, stack.size());
        assertEquals("(", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression();
        expression.expression = "1 + 2";
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression();
        expression.stack = new ExpressionStack();
        expression.stack.push("1");
        expression.stack.push("+");
        expression.stack.push("2");
        ExpressionStack copy = expression.getStackCopy();

        assertEquals(3, copy.size());
        assertEquals("+", copy.get(0));
        assertEquals("1", copy.get(1));
        assertEquals("2", copy.get(2));
    }

    @Test
    public void testEvaluate() {
        // Test cases for each type of function
        // and for various types of expressions
        
        // 1. Math functions
        Expression expression1 = new Expression("3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3");
        try {
            assertEquals(3.0001220703125, expression1.evaluate());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        // 2. Logical functions
        Expression expression2 = new Expression("and(1 > 2, 3 < 4, or(5 > 6, 7 < 8))");
        try {
            assertEquals(false, expression2.evaluate());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        // 3. Date functions
        Expression expression3 = new Expression("dateAdd('2021-01-01', 1, 'day')");
        try {
            assertEquals("2021-01-02", expression3.evaluate());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        // 4. String functions
        Expression expression4 = new Expression("startsWith('Hello world', 'Hello')");
        try {
            assertEquals(true, expression4.evaluate());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        // 5. Error scenarios
        Expression expression5 = new Expression("1 + 2 *");
        try {
            expression5.evaluate();
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("Invalid expression", e.getMessage());
        }

    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.expression = "( 1 + 2 ) * 3";
        expression.tokenize();
        assertEquals(7, expression.stack.size());
        assertEquals("(", expression.stack.get(0));
        assertEquals("1", expression.stack.get(1));
        assertEquals("+", expression.stack.get(2));
        assertEquals("2", expression.stack.get(3));
        assertEquals(")", expression.stack.get(4));
        assertEquals("*", expression.stack.get(5));
        assertEquals("3", expression.stack.get(6));
    }
}