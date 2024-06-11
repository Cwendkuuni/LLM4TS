package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    // Your test cases for CoreOperationRelationalExpression class should be added here.

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(5));
        assertEquals(3, expression.getPrecedence());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSymmetricThrowsException() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(5));
        assertFalse(expression.isSymmetric());
        expression.isSymmetric();
    }
}