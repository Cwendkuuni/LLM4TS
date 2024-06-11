package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateResult;
import org.junit.Test;
import org.mockito.Mockito;

import static org.apache.commons.jxpath.ri.util.TestUtil.createIterateResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), left);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(3.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), left);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(4.0));

        result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testLessThan() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(5), left);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(7.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(4.0));

        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(5), left);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(4.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(4.0));

        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCompareDoubles() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5.5), left) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(3.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.5));

        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCompareCollections() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(createIterateResult(new Integer[] {1, 2, 3}));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(createIterateResult(new Integer[] {1, 2, 3, 4}));

        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(createIterateResult(new Integer[] {1, 2, 3, 4}));

        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(left, right);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenThrow(new RuntimeException());
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));

        expression.computeValue(new EvalContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison2() {
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(left, right);

        Mockito.when(left.compute(Mockito.any(EvalContext.class))).thenReturn(new Double(5.0));
        Mockito.when(right.compute(Mockito.any(EvalContext.class))).thenThrow(new RuntimeException());

        expression.computeValue(new EvalContext());
    }
}