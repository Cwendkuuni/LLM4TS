package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("2 + 3");
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("2 + 3");
        assertNotNull(expression);
        assertEquals(expression.expression, "2 + 3");
        assertNotNull(expression.stack);
        assertEquals(expression.prepared, false);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("2 + 3");
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        
        expression.doEvaluationStack();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("+");
        
        assertEquals(expression.stack, expectedStack);
    }
    
    @Test
    public void testPutFunction() {
        Expression expression = new Expression();
        
        Map<String, FunctionExpression> map = new HashMap<>();
        FunctionExpression f1 = new AndFunction();
        map.put(f1, f1);
        
        expression.putFunction(f1, map);
        
        assertTrue(map.containsKey(f1));
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression f = Expression.getReservedFunctions("and");
        assertNotNull(f);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        boolean result = expression.isReservedFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        boolean result = expression.isLogicalFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        boolean result = expression.isMathFunction("sqrt");
        assertTrue(result);
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        boolean result = expression.isDateFunction("dateAdd");
        assertTrue(result);
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        boolean result = expression.isStringFunction("contains");
        assertTrue(result);
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f = new AndFunction();
        expression.externalFunctions.add(f);
        
        FunctionExpression result = expression.getExternalFunction("and");
        assertEquals(result, f);
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f = new AndFunction();
        expression.externalFunctions.add(f);
        
        boolean result = expression.isExternalFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f = new AndFunction();
        expression.externalFunctions.add(f);
        
        FunctionExpression result = expression.getFunction("and");
        assertEquals(result, f);
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression f = new AndFunction();
        expression.externalFunctions.add(f);
        
        boolean result = expression.isFunction("and");
        assertTrue(result);
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression();
        assertNotNull(expression);
        assertEquals(expression.prepared, false);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression();
        String c = "+";
        Stack<String> stack = new Stack<>();
        
        expression.stack(c, stack);
        
        assertFalse(stack.isEmpty());
        assertEquals(stack.peek(), "+");
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("2 + 3");
        expression.prepare();
        
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("2 + 3");
        expression.stack.add("2");
        expression.stack.add("+");
        
        ExpressionStack copyStack = expression.getStackCopy();
        
        assertEquals(copyStack.size(), expression.stack.size());
        assertNotSame(copyStack, expression.stack);
    }
    
    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        Object result = expression.evaluate();
        assertNull(result);
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.expression = "2 + 3";
        
        expression.tokenize();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        
        assertEquals(expression.stack, expectedStack);
    }
}