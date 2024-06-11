package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        assertNotNull(new CoreOperationRelationalExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionNullArgs() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new DefaultGreaterThanExpression(new Expression[0]);
        assertTrue(expression.getPrecedence() == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new DefaultGreaterThanExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }
}

@Test
public class DefaultGreaterThanExpressionTest extends CoreOperationRelationalExpression {

    public DefaultGreaterThanExpressionTest(Expression[] args) {
        super(args);
    }

    @Test
    public void testDefaultGreaterThanExpression() {
        assertNotNull(new DefaultGreaterThanExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultGreaterThanExpressionNullArgs() {
        new DefaultGreaterThanExpression(null);
    }

    @Test
    public void testEvaluate() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(6);

        DefaultGreaterThanExpression expression = new DefaultGreaterThanExpression(new Expression[] { left, right });
        assertTrue(expression.evaluate(context));
    }

    @Test
    public void testEvaluateFalse() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(5);

        DefaultGreaterThanExpression expression = new DefaultGreaterThanExpression(new Expression[] { left, right });
        assertFalse(expression.evaluate(context));
    }

    @Test
    public void testEvaluateNullLeft() {
        Expression right = new ConstantExpression(5);

        DefaultGreaterThanExpression expression = new DefaultGreaterThanExpression(new Expression[] { null, right });
        assertNull(expression.evaluate(context));
    }

    @Test
    public void testEvaluateNullRight() {
        Expression left = new ConstantExpression(5);

        DefaultGreaterThanExpression expression = new DefaultGreaterThanExpression(new Expression[] { left, null });
        assertNull(expression.evaluate(context));
    }
}

@Test
public class DefaultGreaterThanOrEqualExpressionTest extends CoreOperationRelationalExpression {

    public DefaultGreaterThanOrEqualExpressionTest(Expression[] args) {
        super(args);
    }

    @Test
    public void testDefaultGreaterThanOrEqualExpression() {
        assertNotNull(new DefaultGreaterThanOrEqualExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultGreaterThanOrEqualExpressionNullArgs() {
        new DefaultGreaterThanOrEqualExpression(null);
    }

    @Test
    public void testEvaluate() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(5);

        DefaultGreaterThanOrEqualExpression expression = new DefaultGreaterThanOrEqualExpression(new Expression[] { left, right });
        assertTrue(expression.evaluate(context));
    }

    @Test
    public void testEvaluateFalse() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(6);

        DefaultGreaterThanOrEqualExpression expression = new DefaultGreaterThanOrEqualExpression(new Expression[] { left, right });
        assertFalse(expression.evaluate(context));
    }

    @Test
    public void testEvaluateTrueWhenLeftNullAndRightIsZero() {
        Expression right = new ConstantExpression(0);

        DefaultGreaterThanOrEqualExpression expression = new DefaultGreaterThanOrEqualExpression(new Expression[] { null, right });
        assertTrue(expression.evaluate(context));
    }

    @Test
    public void testEvaluateTrueWhenLeftIsNullAndRightIsNotNull() {
        Expression right = new ConstantExpression(5);

        DefaultGreaterThanOrEqualExpression expression = new DefaultGreaterThanOrEqualExpression(new Expression[] { null, right });
        assertTrue(expression.evaluate(context));
    }
}

@Test
public class DefaultLessThanExpressionTest extends CoreOperationRelationalExpression {

    public DefaultLessThanExpressionTest(Expression[] args) {
        super(args);
    }

    @Test
    public void testDefaultLessThanExpression() {
        assertNotNull(new DefaultLessThanExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultLessThanExpressionNullArgs() {
        new DefaultLessThanExpression(null);
    }

    @Test
    public void testEvaluate() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(6);

        DefaultLessThanExpression expression = new DefaultLessThanExpression(new Expression[] { left, right });
        assertFalse(expression.evaluate(context));
    }

    @Test
    public void testEvaluateTrue() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(5);

        DefaultLessThanExpression expression = new DefaultLessThanExpression(new Expression[] { left, right });
        assertFalse(expression.evaluate(context));
    }

    @Test
    public void testEvaluateNullLeft() {
        Expression right = new ConstantExpression(5);

        DefaultLessThanExpression expression = new DefaultLessThanExpression(new Expression[] { null, right });
        assertNull(expression.evaluate(context));
    }

    @Test
    public void testEvaluateNullRight() {
        Expression left = new ConstantExpression(5);

        DefaultLessThanExpression expression = new DefaultLessThanExpression(new Expression[] { left, null });
        assertNull(expression.evaluate(context));
    }
}

@Test
public class DefaultLessThanOrEqualExpressionTest extends CoreOperationRelationalExpression {

    public DefaultLessThanOrEqualExpressionTest(Expression[] args) {
        super(args);
    }

    @Test
    public void testDefaultLessThanOrEqualExpression() {
        assertNotNull(new DefaultLessThanOrEqualExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultLessThanOrEqualExpressionNullArgs() {
        new DefaultLessThanOrEqualExpression(null);
    }

    @Test
    public void testEvaluate() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(5);

        DefaultLessThanOrEqualExpression expression = new DefaultLessThanOrEqualExpression(new Expression[] { left, right });
        assertTrue(expression.evaluate(context));
    }

    @Test
    public void testEvaluateFalse() {
        Expression left = new ConstantExpression(5);
        Expression right = new ConstantExpression(6);

        DefaultLessThanOrEqualExpression expression = new DefaultLessThanOrEqualExpression(new Expression[] { left, right });
        assertFalse(expression.evaluate(context));
    }

    @Test
    public void testEvaluateTrueWhenLeftIsNullAndRightIsZero() {
        Expression right = new ConstantExpression(0);

        DefaultLessThanOrEqualExpression expression = new DefaultLessThanOrEqualExpression(new Expression[] { null, right });
        assertTrue(expression.evaluate(context));
    }

    @Test
    public void testEvaluateTrueWhenLeftIsNullAndRightIsNotNull() {
        Expression right = new ConstantExpression(5);

        DefaultLessThanOrEqualExpression expression = new DefaultLessThanOrEqualExpression(new Expression[] { null, right });
        assertTrue(expression.evaluate(context));
    }
}