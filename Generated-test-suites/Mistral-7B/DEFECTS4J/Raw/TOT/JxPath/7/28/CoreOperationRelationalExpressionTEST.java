package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg(10),
                new CoreOperationRelationalExpressionarg(5)
        );

        Integer x = 15;
        Integer y = 10;

        JXPathContext context = new JXPathContext(new Object[] {x, y});
        assertTrue(expr.evaluateToBoolean(context));

        x = 5;
        assertFalse(expr.evaluateToBoolean(context));

        x = 10;
        assertTrue(expr.evaluateToBoolean(context));
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg(10),
                new CoreOperationRelationalExpressionarg(5)
        );

        Integer x = 15;
        Integer y = 10;

        JXPathContext context = new JXPathContext(new Object[] {x, y});
        assertTrue(expr.evaluateToBoolean(context));

        x = 10;
        assertTrue(expr.evaluateToBoolean(context));

        x = 5;
        assertTrue(expr.evaluateToBoolean(context));
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg(5),
                new CoreOperationRelationalExpressionarg(10)
        );

        Integer x = 5;
        Integer y = 10;

        JXPathContext context = new JXPathContext(new Object[] {x, y});
        assertFalse(expr.evaluateToBoolean(context));

        x = 10;
        assertFalse(expr.evaluateToBoolean(context));

        x = 15;
        assertFalse(expr.evaluateToBoolean(context));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg(5),
                new CoreOperationRelationalExpressionarg(10)
        );

        Integer x = 5;
        Integer y = 10;

        JXPathContext context = new JXPathContext(new Object[] {x, y});
        assertFalse(expr.evaluateToBoolean(context));

        x = 10;
        assertTrue(expr.evaluateToBoolean(context));

        x = 15;
        assertTrue(expr.evaluateToBoolean(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs() {
        new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg("Hello"),
                new CoreOperationRelationalExpressionarg(5)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs2() {
        new CoreOperationRelationalExpression(
                new CoreOperationRelationalExpressionarg(new Object()),
                new CoreOperationRelationalExpressionarg(5)
        );
    }

}