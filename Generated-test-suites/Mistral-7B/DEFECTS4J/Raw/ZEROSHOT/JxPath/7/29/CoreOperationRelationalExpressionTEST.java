package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiled.RelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationAdd(arg1, arg2));

        assertSame(CoreOperationRelationalExpression.class, expr.getClass());
        assertSame(arg1, expr.getArg(0));
        assertSame(arg2, expr.getArg(1));
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationAdd(new CoreNumber(1), new CoreNumber(2)));

        int precedence = expr.getPrecedence();
        assertTrue(precedence == 3);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperationAdd(new CoreNumber(1), new CoreNumber(2)));

        assertFalse(expr.isSymmetric());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        Expression arg3 = new CoreNumber(3);

        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(new CoreOperationAdd(arg1, arg2));
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(new CoreOperationAdd(arg1, arg2));
        CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThan(new CoreOperationAdd(arg1, arg3));
        CoreOperationRelationalExpression expr4 = new CoreOperationGreaterThanOrEqual(new CoreOperationAdd(arg1, arg2));

        assertTrue(expr1.equals(expr2));
        assertFalse(expr1.equals(expr3));
        assertFalse(expr1.equals(expr4));
    }

    /**
     * Test evaluate method.
     */
    @Test
    public void testEvaluate() {
        JXPathContext context = new JXPathContext();

        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        Expression arg3 = new CoreNumber(3);

        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(new CoreOperationAdd(arg1, arg2));
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThanOrEqual(new CoreOperationAdd(arg1, arg2));

        Object value1 = expr1.evaluate(context);
        Object value2 = expr2.evaluate(context);

        assertFalse(value1 instanceof Boolean);
        assertFalse(value2 instanceof Boolean);

        context.setValues(new Object[]{arg1, arg2}, new Object[]{new Integer(2), new Integer(1)});

        value1 = expr1.evaluate(context);
        value2 = expr2.evaluate(context);

        assertFalse(value1 instanceof Boolean);
        assertFalse(value2 instanceof Boolean);

        assertTrue(value1 == Boolean.FALSE);
        assertTrue(value2 == Boolean.TRUE);

        context.setValues(new Object[]{arg1, arg2}, new Object[]{new Integer(3), new Integer(2)});

        value1 = expr1.evaluate(context);
        value2 = expr2.evaluate(context);

        assertFalse(value1 instanceof Boolean);
        assertFalse(value2 instanceof Boolean);

        assertTrue(value1 == Boolean.TRUE);
        assertTrue(value2 == Boolean.TRUE);

        context.setValues(new Object[]{arg1, arg3}, new Object[]{new Integer(3), new Integer(3)});

        value1 = expr1.evaluate(context);
        value2 = expr2.evaluate(context);

        assertFalse(value1 instanceof Boolean);
        assertFalse(value2 instanceof Boolean);

        assertTrue(value1 == Boolean.TRUE);
        assertTrue(value2 == Boolean.TRUE);
    }
}