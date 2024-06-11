package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertTrue(expression.getPrecedence() == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testNewInstanceWithNullArgs() {
        try {
            new CoreOperationRelationalExpression(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithEmptyArgsArray() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertTrue(expression instanceof CoreOperationRelationalExpression);
    }

    @Test
    public void testNewInstanceWithOneArg() {
        CoreOperation arg = new CoreOperationConstant(1);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{arg});
        assertTrue(expression instanceof CoreOperationRelationalExpression);
    }

    @Test
    public void testNewInstanceWithTwoArgs() {
        CoreOperation arg1 = new CoreOperationConstant(1);
        CoreOperation arg2 = new CoreOperationConstant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{arg1, arg2});
        assertTrue(expression instanceof CoreOperationRelationalExpression);
    }
}