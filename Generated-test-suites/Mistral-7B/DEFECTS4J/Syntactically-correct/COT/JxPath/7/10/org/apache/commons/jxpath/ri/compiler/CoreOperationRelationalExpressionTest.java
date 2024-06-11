package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertNotNull(coreOperationRelationalExpression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionWithInvalidArgsNumber() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    @Test
    public void testCoreOperationRelationalExpressionGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]);
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertTrue(precedence == 3);
    }

    @Test
    public void testCoreOperationRelationalExpressionIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]);
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testBasicGreaterThan() {
        Expression leftOperand = new ConstantExpression(1);
        Expression rightOperand = new ConstantExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == false);

        leftOperand = new ConstantExpression(2);
        rightOperand = new ConstantExpression(1);
        coreOperationRelationalExpression = new GreaterThanExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);
    }

    @Test
    public void testBasicGreaterThanOrEqual() {
        Expression leftOperand = new ConstantExpression(1);
        Expression rightOperand = new ConstantExpression(2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == false);

        leftOperand = new ConstantExpression(2);
        rightOperand = new ConstantExpression(1);
        coreOperationRelationalExpression = new GreaterThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);

        leftOperand = new ConstantExpression(2);
        rightOperand = new ConstantExpression(2);
        coreOperationRelationalExpression = new GreaterThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);
    }

    @Test
    public void testBasicLessThan() {
        Expression leftOperand = new ConstantExpression(2);
        Expression rightOperand = new ConstantExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new LessThanExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);

        leftOperand = new ConstantExpression(1);
        rightOperand = new ConstantExpression(2);
        coreOperationRelationalExpression = new LessThanExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == false);
    }

    @Test
    public void testBasicLessThanOrEqual() {
        Expression leftOperand = new ConstantExpression(2);
        Expression rightOperand = new ConstantExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new LessThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == false);

        leftOperand = new ConstantExpression(1);
        rightOperand = new ConstantExpression(2);
        coreOperationRelationalExpression = new LessThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);

        leftOperand = new ConstantExpression(2);
        rightOperand = new ConstantExpression(2);
        coreOperationRelationalExpression = new LessThanOrEqualExpression(leftOperand, rightOperand);
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionConstruction() {
        new CoreOperationRelationalExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithNullLeftArg() {
        Expression[] args = new Expression[2];
        args[0] = null;
        new GreaterThanExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithNullRightArg() {
        Expression[] args = new Expression[2];
        args[1] = null;
        new GreaterThanOrEqualExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithInvalidLeftArgType() {
        Expression[] args = new Expression[2];
        args[0] = new ConstantExpression("String");
        new GreaterThanExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithInvalidRightArgType() {
        Expression[] args = new Expression[2];
        args[1] = new ConstantExpression("String");
        new GreaterThanExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithMismatchedTypes() {
        Expression leftOperand = new ConstantExpression(1);
        Expression rightOperand = new ConstantExpression("String");
        new GreaterThanExpression(new Expression[]{leftOperand, rightOperand});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTestCoreOperationRelationalExpressionWithMismatchedTypes2() {
        Expression leftOperand = new ConstantExpression("String");
        Expression rightOperand = new ConstantExpression(1);
        new GreaterThanExpression(new Expression[]{leftOperand, rightOperand});
    }

    @Test
    public void testNullLeftAndRightOperands() {
        Expression leftOperand = new ConstantExpression(null);
        Expression rightOperand = new ConstantExpression(null);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanOrEqualExpression(new Expression[]{leftOperand, rightOperand});
        assertNull(coreOperationRelationalExpression.evaluate(new TestContext()));
    }

    @Test
    public void testNullLeftOperand() {
        Expression leftOperand = new ConstantExpression(null);
        Expression rightOperand = new ConstantExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanOrEqualExpression(new Expression[]{leftOperand, rightOperand});
        assertNull(coreOperationRelationalExpression.evaluate(new TestContext()));
    }

    @Test
    public void testNullRightOperand() {
        Expression leftOperand = new ConstantExpression(1);
        Expression rightOperand = new ConstantExpression(null);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanOrEqualExpression(new Expression[]{leftOperand, rightOperand});
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == false);
    }

    @Test
    public void testSameValues() {
        Expression leftOperand = new ConstantExpression(1);
        Expression rightOperand = new ConstantExpression(1);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new GreaterThanOrEqualExpression(new Expression[]{leftOperand, rightOperand});
        assertTrue(coreOperationRelationalExpression.evaluate(new TestContext()) == true);
    }
}