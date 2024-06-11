package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }
    
    @Test
    public void testConstructorWithArguments() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        
        Expression[] result = expression.getArgs();
        assertArrayEquals(args, result);
    }
    
    @Test
    public void testConstructorWithoutArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        
        Expression[] result = expression.getArgs();
        assertNull(result);
    }
}