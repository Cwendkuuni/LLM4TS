package org.javex;

import static org.junit.Assert.*;

import org.javex.functions.date.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.string.*;
import org.javex.utils.FunctionExpression;
import org.javex.utils.GenericFunctionExpression;
import org.javex.utils.JavexUtils;
import org.javex.utils.ReservedFunctions;
import java.util.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("") {};
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression("") {};
        assertFalse(expression.prepared);
        assertNotNull(expression.stack);
        assertNotNull(expression.expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("") {};
        expression.stack = new ExpressionStack();
        expression.stack.add("+");
        expression.stack.add("5");
        expression.stack.add("3");
        expression.doEvaluationStack();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("5");
        expectedStack.add("3");
        expectedStack.add("+");
        assertEquals(expectedStack, expression.stack);
    }
    
    @Test
    public void testGetReservedFunctions() {
        ReservedFunctions reservedFunctions = new ReservedFunctions();
        FunctionExpression function = reservedFunctions.getReservedFunctions("+");
        assertNull(function);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("") {};
        boolean isReserved = expression.isReservedFunction("+");
        assertFalse(isReserved);
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("") {};
        boolean isLogical = expression.isLogicalFunction("and");
        assertFalse(isLogical);
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("") {};
        boolean isMath = expression.isMathFunction("+");
        assertFalse(isMath);
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("") {};
        boolean isDate = expression.isDateFunction("dateFunction");
        assertFalse(isDate);
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("") {};
        boolean isString = expression.isStringFunction("stringFunction");
        assertFalse(isString);
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        expression.externalFunctions.add(new GenericFunctionExpression("func"));
        FunctionExpression function = expression.getExternalFunction("func");
        assertNotNull(function);
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("") {};
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        expression.externalFunctions.add(new GenericFunctionExpression("externalFunc"));
        boolean isExternal = expression.isExternalFunction("externalFunc");
        assertTrue(isExternal);
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression("") {};
        FunctionExpression function = expression.getFunction("func");
        assertNull(function);
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression("") {};
        boolean isFunction = expression.isFunction("func");
        assertFalse(isFunction);
    }
    
    @Test
    public void testConstructor2() {
        Expression expression = new Expression() {};
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression("") {};
        Stack<String> stack = new Stack<>();
        expression.stack("c", stack);
        assertEquals("c", stack.peek());
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.stack = new ExpressionStack();
        expression.stack.add("5");
        ExpressionStack copy = expression.getStackCopy();
        assertEquals(expression.stack, copy);
    }
    
    @Test(expected=Exception.class)
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("") {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception();
            }
        };
        expression.evaluate();
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression("") {};
        expression.expression = "2+2";
        expression.tokenize();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("2");
        assertEquals(expectedStack, expression.stack);
    }
}