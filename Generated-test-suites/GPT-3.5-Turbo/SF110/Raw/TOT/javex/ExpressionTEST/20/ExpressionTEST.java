
package org.javex.test;

import org.javex.Expression;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.utils.JavexUtils;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class ExpressionTest {
    
    private Expression expression;
    
    @Before
    public void setup() {
        expression = new ExpressionStub();
    }
    
    // Test cases for getExternalFunctions()
    @Test
    public void testGetExternalFunctions_ReturnsEmptyList_WhenNoExternalFunctionsAdded() {
        List<FunctionExpression> expected = new ArrayList<>();
        
        List<FunctionExpression> result = expression.getExternalFunctions();
        
        assertEquals(expected, result);
    }
    
    // Test cases for Expression(String s)
    @Test
    public void testExpression_Constructor_InitializesExpressionStack() {
        String input = "1 + 2";
        
        Expression result = new ExpressionStub(input);
        
        assertNotNull(result.stack);
    }
    
    // Test cases for doEvaluationStack()
    @Test
    public void testDoEvaluationStack_StackIsEmpty_AfterCallingMethod() {
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        expression.doEvaluationStack();
        
        assertTrue(expression.stack.isEmpty());
    }
    
    // Test cases for getReservedFunctions(String symbol)
    @Test
    public void testGetReservedFunctions_ReturnsNull_WhenReservedFunctionsNotInitialized() {
        Expression.reservedFunctions = null;
        String symbol = "+";
        
        FunctionExpression result = expression.getReservedFunctions(symbol);
        
        assertNull(result);
    }
    
    // Test cases for isReservedFunction(String symbol)
    @Test
    public void testIsReservedFunction_ReturnsTrue_WhenSymbolIsReservedFunction() {
        String symbol = "+";
        
        boolean result = expression.isReservedFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for isLogicalFunction(String symbol)
    @Test
    public void testIsLogicalFunction_ReturnsTrue_WhenSymbolIsLogicalFunction() {
        String symbol = "and";
        
        boolean result = expression.isLogicalFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for isMathFunction(String symbol)
    @Test
    public void testIsMathFunction_ReturnsTrue_WhenSymbolIsMathFunction() {
        String symbol = "+";
        
        boolean result = expression.isMathFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for isDateFunction(String symbol)
    @Test
    public void testIsDateFunction_ReturnsTrue_WhenSymbolIsDateFunction() {
        String symbol = "dateAdd";
        
        boolean result = expression.isDateFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for isStringFunction(String symbol)
    @Test
    public void testIsStringFunction_ReturnsTrue_WhenSymbolIsStringFunction() {
        String symbol = "startsWith";
        
        boolean result = expression.isStringFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for getExternalFunction(String symbol)
    @Test
    public void testGetExternalFunction_ReturnsNull_WhenExternalFunctionNotAdded() {
        expression.externalFunctions = null;
        String symbol = "externalFunction";
        
        FunctionExpression result = expression.getExternalFunction(symbol);
        
        assertNull(result);
    }
    
    // Test cases for isExternalFunction(String symbol)
    @Test
    public void testIsExternalFunction_ReturnsTrue_WhenSymbolIsExternalFunction() {
        FunctionExpression externalFunction = new ExternalFunction();
        expression.externalFunctions.add(externalFunction);
        String symbol = "externalFunction";
        
        boolean result = expression.isExternalFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for getFunction(String symbol)
    @Test
    public void testGetFunction_ReturnsNull_WhenFunctionNotFound() {
        String symbol = "unknownFunction";
        
        FunctionExpression result = expression.getFunction(symbol);
        
        assertNull(result);
    }
    
    // Test cases for isFunction(String symbol)
    @Test
    public void testIsFunction_ReturnsTrue_WhenSymbolIsFunction() {
        String symbol = "+";
        
        boolean result = expression.isFunction(symbol);
        
        assertTrue(result);
    }
    
    // Test cases for prepare()
    @Test
    public void testPrepare_SetsPreparedToTrue_AfterCallingMethod() {
        expression.prepare();
        
        assertTrue(expression.prepared);
    }
    
    // Test cases for getStackCopy()
    @Test
    public void testGetStackCopy_ReturnsCopyOfStack() {
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        ExpressionStack result = expression.getStackCopy();
        
        assertNotSame(expression.stack, result);
        assertEquals(expression.stack, result);
    }
    
    // Test cases for evaluate()
    @Test(expected = Exception.class)
    public void testEvaluate_ThrowsException_WhenExceptionOccurs() throws Exception {
        expression = new ExpressionStub() {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception();
            }
        };
        
        expression.evaluate();
    }
    
    // Test cases for tokenize()
    @Test
    public void testTokenize_AddsValidTokensToStack() {
        expression.expression = "1 + 2";
        
        expression.tokenize();
        
        assertEquals("1", expression.stack.get(0));
        assertEquals("+", expression.stack.get(1));
        assertEquals("2", expression.stack.get(2));
    }
    
    // Test cases for stack(String c, Stack stack)
    @Test
    public void testStack_StackIsNotEmpty_AfterCallingMethod() {
        Stack<String> stack = new Stack<>();
        
        expression.stack("1", stack);
        
        assertFalse(stack.isEmpty());
    }
    
    @Test
    public void testStack_StackPeekIsUpdated_AfterCallingMethod() {
        Stack<String> stack = new Stack<>();
        stack.push("1");
        
        expression.stack("2", stack);
        
        assertEquals("12", stack.peek());
    }
    
    // Test cases for ExpressionStub class
    private class ExpressionStub extends Expression {
        private String expression;
        
        public ExpressionStub() {
            super();
        }
        
        public ExpressionStub(String expression) {
            super(expression);
        }
        
        @Override
        public Object evaluate() throws Exception {
            return null;
        }
    }
    
    // Test cases for ExternalFunction class
    private class ExternalFunction extends FunctionExpression {
        @Override
        public Object evaluate(List<Object> args) {
            return null;
        }
        
        @Override
        public String getSimbol() {
            return "externalFunction";
        }
        
        @Override
        public int getType() {
            return FunctionExpression.GENERIC;
        }
    }
}
