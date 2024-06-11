package org.javex;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {};
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("test") {};
        assertNotNull(expression);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("example"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("example"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("example"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("example"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isStringFunction("contains"));
        assertFalse(expression.isStringFunction("example"));
    }
    
     @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {};
        FunctionExpression function = new ExampleFunction();
        List externalFunctions = new ArrayList();
        externalFunctions.add(function);
        expression.externalFunctions = externalFunctions;
        
        assertEquals(function, expression.getExternalFunction("exampleFunction"));
        assertNull(expression.getExternalFunction("notExistingFunction"));
    }
    
     @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {};
        FunctionExpression function = new ExampleFunction();
        List externalFunctions = new ArrayList();
        externalFunctions.add(function);
        expression.externalFunctions = externalFunctions;
        
        assertTrue(expression.isExternalFunction("exampleFunction"));
        assertFalse(expression.isExternalFunction("notExistingFunction"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {};
        FunctionExpression function = new ExampleFunction();
        expression.reservedFunctions = new TreeMap();
        expression.reservedFunctions.put(function, function);
        
        assertEquals(function, expression.getFunction("exampleFunction"));
        assertNull(expression.getFunction("notExistingFunction"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {};
        FunctionExpression function = new ExampleFunction();
        expression.reservedFunctions = new TreeMap();
        expression.reservedFunctions.put(function, function);
        
        assertTrue(expression.isFunction("exampleFunction"));
        assertFalse(expression.isFunction("notExistingFunction"));
    }
    
    @Test
    public void testExpressionConstructorEmpty() {
        Expression expression = new Expression() {};
        assertNotNull(expression);
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression() {};
        expression.expression = "test";
        expression.prepare();
        
        assertEquals("t", expression.stack.get(0));
        assertEquals("e", expression.stack.get(1));
        assertEquals("s", expression.stack.get(2));
        assertEquals("t", expression.stack.get(3));
        assertEquals("END", expression.stack.get(4));
    }
    
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {};
        expression.stack = new ExpressionStack();
        expression.stack.add("test");
        
        ExpressionStack copyStack = expression.getStackCopy();
        assertEquals("test", copyStack.get(0));
    }
    
    @Test(expected=Exception.class)
    public void testEvaluate() throws Exception{
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                throw new Exception("Evaluation error");
            }
        };
        
        expression.evaluate();
    }
    
    @Test
    public void testStack() {
        Expression expression = new Expression() {};
        Stack<String> stack = new Stack<String>();
        expression.stack("a", stack);
        
        assertEquals("a", stack.pop());
    }

    class ExampleFunction implements FunctionExpression {
        public String getSimbol() {
            return "exampleFunction";
        }
        
        public int getType() {
            return FunctionExpression.LOGICAL;
        }
    }
}