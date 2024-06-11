package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
    private Expression expression;
    
    @Before
    public void setUp() {
        expression = new Expression("1+2+3");
        expression.prepare();
    }
    
    @Test
    public void testGetExternalFunctions() {
        List expectedExternalFunctions = new ArrayList<>();
        assertEquals(expectedExternalFunctions, expression.getExternalFunctions());
    }
    
    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction("foo"));
    }
    
    @Test
    public void testIsReservedFunction() {
        assertFalse(expression.isReservedFunction("foo"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction("foo"));
    }
    
    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction("foo"));
    }
    
    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction("foo"));
    }
    
    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction("foo"));
    }
    
    @Test
    public void testGetFunction() {
        FunctionExpression expectedFunction = new PlusFunction();
        assertEquals(expectedFunction, expression.getFunction("+"));
    }
    
    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("+"));
    }
    
    @Test
    public void testEvaluate() {
        Object expected = 6;
        try {
            assertEquals(expected, expression.evaluate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testTokenize() {
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        
        expression.tokenize();
        
        assertEquals(expectedStack, expression.getStackCopy());
    }
}