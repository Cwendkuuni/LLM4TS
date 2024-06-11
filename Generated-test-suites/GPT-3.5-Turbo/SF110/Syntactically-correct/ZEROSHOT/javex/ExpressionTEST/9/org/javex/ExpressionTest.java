package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression(""){};
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression(""){};
        assertNotNull(expression.stack);
        assertNotNull(expression.expression);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression(""){};
        expression.stack.add("test");
        expression.stack.add("test2");
        expression.stack.add("(");
        expression.stack.add("+");
        expression.stack.add(")");
        expression.stack.add("*");
        expression.stack.add("test3");
        expression.doEvaluationStack();
        assertEquals("test test2 + test3 *", expression.stack.toString());
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = Expression.getReservedFunctions("and");
        assertTrue(function instanceof AndFunction);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isReservedFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isLogicalFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isMathFunction("abs");
        assertTrue(result);
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isDateFunction("dateAdd");
        assertTrue(result);
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isStringFunction("contains");
        assertTrue(result);
    }
    
    @Test
    public void testGetExternalFunction() {
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new ContainsFunction());
        Expression expression = new Expression(""){};
        expression.externalFunctions = externalFunctions;
        FunctionExpression function = expression.getExternalFunction("contains");
        assertNotNull(function);
    }
    
    @Test
    public void testIsExternalFunction() {
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new ContainsFunction());
        Expression expression = new Expression(""){};
        expression.externalFunctions = externalFunctions;
        boolean result = expression.isExternalFunction("contains");
        assertTrue(result);
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression function = expression.getFunction("abs");
        assertTrue(function instanceof AbsFunction);
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression(""){};
        boolean result = expression.isFunction("abs");
        assertTrue(result);
    }
    
    @Test
    public void testConstructor2() {
        Expression expression = new Expression(""){};
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression(""){};
        Stack<String> stack = new Stack<>();
        stack.push("test");
        expression.stack("a", stack);
        assertEquals("testa", stack.peek());
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression(""){};
        expression.stack.add("test");
        expression.stack.add("test2");
        expression.stack.add("test3");
        expression.prepare();
        assertEquals("test test2 test3", expression.stack.toString());
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression(""){};
        expression.stack.add("test");
        ExpressionStack copy = expression.getStackCopy();
        assertNotSame(expression.stack, copy);
        assertEquals(expression.stack, copy);
    }
    
    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression(""){};
        Object result = expression.evaluate();
        assertNull(result);
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression(""){};
        expression.stack.add("test");
        expression.stack.add("+");
        expression.stack.add("(");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.stack.add(")");
        expression.stack.add("/");
        expression.stack.add("4");
        expression.tokenize();
        assertEquals("test + ( 2 * 3 ) / 4", expression.stack.toString());
    }
}