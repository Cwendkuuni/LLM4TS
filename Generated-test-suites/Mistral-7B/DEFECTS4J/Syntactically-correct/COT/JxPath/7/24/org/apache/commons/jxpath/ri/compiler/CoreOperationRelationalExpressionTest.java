package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse("Constructor should not be called with empty args", coreOperationRelationalExpression instanceof CoreOperationRelationalExpression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationGreaterThan(new Expression[0]);
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertTrue("Precedence should be 3", precedence == 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationGreaterThan(new Expression[0]);
        boolean symmetric = coreOperationRelationalExpression.isSymmetric();
        assertFalse("IsSymmetric method should throw UnsupportedOperationException", symmetric);
    }

    @Test
    public void testGreaterThan() {
        Expression leftOperand = new LiteralExpression(3);
        Expression rightOperand = new LiteralExpression(4);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationGreaterThan(new Expression[]{leftOperand, rightOperand});
        boolean result = coreOperationRelationalExpression.evaluate(new Context());
        assertTrue("3 < 4 should be true", result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression leftOperand = new LiteralExpression(3);
        Expression rightOperand = new LiteralExpression(4);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationGreaterThanOrEqual(new Expression[]{leftOperand, rightOperand});
        boolean result = coreOperationRelationalExpression.evaluate(new Context());
        assertTrue("3 <= 4 should be false", result);

        leftOperand = new LiteralExpression(4);
        result = coreOperationRelationalExpression.evaluate(new Context());
        assertTrue("3 <= 4 should be true", result);
    }

    @Test
    public void testLessThan() {
        Expression leftOperand = new LiteralExpression(3);
        Expression rightOperand = new LiteralExpression(4);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationLessThan(new Expression[]{leftOperand, rightOperand});
        boolean result = coreOperationRelationalExpression.evaluate(new Context());
        assertFalse("3 > 4 should be false", result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression leftOperand = new LiteralExpression(3);
        Expression rightOperand = new LiteralExpression(4);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationLessThanOrEqual(new Expression[]{leftOperand, rightOperand});
        boolean result = coreOperationRelationalExpression.evaluate(new Context());
        assertFalse("3 <= 4 should be false", result);

        leftOperand = new LiteralExpression(4);
        result = coreOperationRelationalExpression.evaluate(new Context());
        assertTrue("3 <= 4 should be true", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgsNumber() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[3]);
    }
}