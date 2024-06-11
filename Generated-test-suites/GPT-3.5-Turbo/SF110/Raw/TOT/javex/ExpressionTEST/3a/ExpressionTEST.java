package org.javex;

import org.junit.Test;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("(3+2)*5");
        List expected = null;
        List result = expression.getExternalFunctions();
        assert(expected == result);
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("(3+2)*5");
        String expected = "(3+2)*5";
        String result = expression.expression;
        assert(expected.equals(result));
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("(3+2)*5");
        Stack<String> operands = new Stack<String>();
        ExpressionStack values = new ExpressionStack();
        operands.push("3");
        operands.push("+");
        operands.push("2");
        operands.push("5");
        
        expression.doEvaluationStack();
        
        Stack<String> expectedOperands = new Stack<String>();
        expectedOperands.push("+");
        expectedOperands.push("*");
        Stack<String> expectedValues = new Stack<String>();
        expectedValues.push("3");
        expectedValues.push("2");
        expectedValues.push("5");
        
        assert(expectedOperands.equals(operands));
        assert(expectedValues.equals(values));
    }
    
    @Test
    public void testPutFunction() {
        Expression expression = new Expression("(3+2)*5");
        FunctionExpression f = new AndFunction();
        TreeMap map = new TreeMap();
        
        expression.putFunction(f, map);
        
        FunctionExpression expected = f;
        FunctionExpression result = map.get(f);
        
        assert(expected == result);
    }
    
    @Test
    public void testGetReservedFunctions() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "and";
        
        FunctionExpression expected = null;
        FunctionExpression result = expression.getReservedFunctions(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "and";
        
        boolean expected = false;
        boolean result = expression.isReservedFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "and";
        
        boolean expected = false;
        boolean result = expression.isLogicalFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "+";
        
        boolean expected = true;
        boolean result = expression.isMathFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "date";
        
        boolean expected = false;
        boolean result = expression.isDateFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "starts";
        
        boolean expected = false;
        boolean result = expression.isStringFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "startsWith";
        
        FunctionExpression expected = null;
        FunctionExpression result = expression.getExternalFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "startsWith";
        
        boolean expected = false;
        boolean result = expression.isExternalFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "startsWith";
        
        FunctionExpression expected = null;
        FunctionExpression result = expression.getFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression("(3+2)*5");
        String simbol = "startsWith";
        
        boolean expected = false;
        boolean result = expression.isFunction(simbol);
        
        assert(expected == result);
    }
    
    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression("(3+2)*5");
        
        boolean expected = false;
        boolean result = expression.prepared;
        
        assert(expected == result);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression("(3+2)*5");
        String c = "3";
        Stack stack = new Stack();
        stack.push("2");
        
        expression.stack(c, stack);
        
        Stack<String> expected = new Stack<String>();
        expected.push("23");
        Stack<String> result = stack;
        
        assert(expected.equals(result));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("(3+2)*5");
        
        expression.prepare();
        
        boolean expected = true;
        boolean result = expression.prepared;
        
        assert(expected == result);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("(3+2)*5");
        expression.stack = new ExpressionStack();
        expression.stack.add("3");
        expression.stack.add("+");
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("3");
        expected.add("+");
        ExpressionStack result = expression.getStackCopy();
        
        assert(expected.equals(result));
    }
    
    // additional test cases for evaluate() and tokenize() methods can be added
    
}