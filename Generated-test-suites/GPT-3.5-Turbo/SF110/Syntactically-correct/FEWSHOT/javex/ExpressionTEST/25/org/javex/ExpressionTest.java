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
        Expression expression = new Expression(""){};
        assertEquals(Collections.emptyList(), expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression(""){};
        assertEquals("", expression.expression);
        assertEquals(false, expression.prepared);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression(""){};
        Stack<String> stack = new Stack<>();
        expression.stack("a", stack);
        assertEquals("a", stack.peek());
        expression.stack("b", stack);
        assertEquals("ab", stack.peek());
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression(""){};
        assertEquals(true, expression.isReservedFunction("and"));
        assertEquals(false, expression.isReservedFunction("add"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression(""){};
        assertEquals(true, expression.isLogicalFunction("and"));
        assertEquals(false, expression.isLogicalFunction("+"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression(""){};
        assertEquals(true, expression.isMathFunction("abs"));
        assertEquals(false, expression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression(""){};
        assertEquals(true, expression.isDateFunction("dateadd"));
        assertEquals(false, expression.isDateFunction("abs"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression(""){};
        assertEquals(true, expression.isStringFunction("contains"));
        assertEquals(false, expression.isStringFunction("dateadd"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression externalFunction = new FunctionExpression("externalFunction");
        expression.externalFunctions = Collections.singletonList(externalFunction);
        assertEquals(externalFunction, expression.getExternalFunction("externalFunction"));
        assertNull(expression.getExternalFunction("randomFunction"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression externalFunction = new FunctionExpression("externalFunction");
        expression.externalFunctions = Collections.singletonList(externalFunction);
        assertEquals(true, expression.isExternalFunction("externalFunction"));
        assertEquals(false, expression.isExternalFunction("randomFunction"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression reservedFunction = new FunctionExpression("and");
        FunctionExpression externalFunction = new FunctionExpression("externalFunction");
        expression.externalFunctions = Collections.singletonList(externalFunction);
        assertEquals(reservedFunction, expression.getFunction("and"));
        assertEquals(externalFunction, expression.getFunction("externalFunction"));
        assertNull(expression.getFunction("randomFunction"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression(""){};
        FunctionExpression reservedFunction = new FunctionExpression("and");
        FunctionExpression externalFunction = new FunctionExpression("externalFunction");
        expression.externalFunctions = Collections.singletonList(externalFunction);
        assertEquals(true, expression.isFunction("and"));
        assertEquals(true, expression.isFunction("externalFunction"));
        assertEquals(false, expression.isFunction("randomFunction"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression(""){};
        expression.prepare();
        assertEquals(true, expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression(""){};
        expression.stack = new ExpressionStack(Arrays.asList("a", "b", "c"));
        ExpressionStack copy = expression.getStackCopy();
        assertEquals(copy, expression.stack);
        assertNotSame(copy, expression.stack);
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression(""){};
        assertThrows(Exception.class, expression::evaluate);
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression(""){};
        expression.expression = "1 + 2";
        expression.tokenize();
        ExpressionStack expectedStack = new ExpressionStack(Arrays.asList("1", "+", "2"));
        assertEquals(expectedStack, expression.stack);
    }

}