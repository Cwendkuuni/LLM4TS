package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals("Expected CoreOperationRelationalExpression instance", CoreOperationRelationalExpression.class, expression.getClass());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals("Expected precedence to be 3", 3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        boolean symmetric = expression.isSymmetric();
        assertEquals("Expected symmetric to be false", false, symmetric);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testCoreOperationRelationalExpressionWithArgs() {
        Expression arg1 = new CoreOperation(new Expression[0]);
        Expression arg2 = new CoreOperation(new Expression[0]);
        Expression[] args = new Expression[2];
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals("Expected CoreOperationRelationalExpression instance", CoreOperationRelationalExpression.class, expression.getClass());
        assertEquals("Expected arg1 to be set correctly", arg1, expression.getArg(0));
        assertEquals("Expected arg2 to be set correctly", arg2, expression.getArg(1));
    }
}