package org.javex;

import static org.junit.Assert.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression(""){};
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression(""){};
        assertNotNull(expression.stack);
        assertEquals("", expression.expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression(""){};
        assertEquals(0, expression.stack.size());
        expression.stack.push("+");
        expression.stack.push("1");
        expression.doEvaluationStack();
        assertEquals(2, expression.stack.size());
        assertEquals("1", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
    }
    
    @Test
    public void testPutFunction() {
        Map map = new HashMap();
        FunctionExpression functionExpression = new AndFunction();
        Expression.putFunction(functionExpression, map);
        assertEquals(1, map.size());
        assertNotNull(map.get(functionExpression));
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("and"));
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("notReservedFunction"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("notLogicalFunction"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isMathFunction("plusfunction"));
        assertFalse(expression.isMathFunction("notMathFunction"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isDateFunction("dateAddFunction"));
        assertFalse(expression.isDateFunction("notDateFunction"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isStringFunction("containsFunction"));
        assertFalse(expression.isStringFunction("notStringFunction"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression(""){};
        List externalFunctions = new ArrayList();
        FunctionExpression functionExpression = new ContainsFunction();
        externalFunctions.add(functionExpression);
        expression.externalFunctions = externalFunctions;
        assertEquals(functionExpression, expression.getExternalFunction("containsFunction"));
        assertNull(expression.getExternalFunction("notExternalFunction"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression(""){};
        List externalFunctions = new ArrayList();
        FunctionExpression functionExpression = new ContainsFunction();
        externalFunctions.add(functionExpression);
        expression.externalFunctions = externalFunctions;
        assertTrue(expression.isExternalFunction("containsFunction"));
        assertFalse(expression.isExternalFunction("notExternalFunction"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression functionExpression = new ContainsFunction();
        assertEquals(functionExpression, expression.getFunction("containsFunction"));
        assertNull(expression.getFunction("notFunction"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression(""){};
        assertTrue(expression.isFunction("containsFunction"));
        assertFalse(expression.isFunction("notFunction"));
    }
    
    @Test
    public void testConstructor2() {
        Expression expression = new Expression(){};
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression(""){};
        Stack stack = new Stack();
        expression.stack(".", stack);
        assertEquals(".", stack.peek());
        expression.stack("+", stack);
        assertEquals("+.", stack.peek());
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression(""){};
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression(""){};
        expression.stack.push("1");
        expression.stack.push("2");
        ExpressionStack stackCopy = expression.getStackCopy();
        assertNotNull(stackCopy);
        assertEquals(expression.stack.size(), stackCopy.size());
        assertEquals(expression.stack.get(0), stackCopy.get(0));
        assertEquals(expression.stack.get(1), stackCopy.get(1));
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression(""){};
        try {
            expression.evaluate();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression(""){};
        expression.expression = "1 + 2";
        expression.tokenize();
        assertEquals(3, expression.stack.size());
        assertEquals("1", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("2", expression.stack.get(2));
    }
}