package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Test cases for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testAbstractClass() {
        assertInstanceOf(CoreOperationRelationalExpression.class, new CoreOperationRelationalExpression(new Expression[0]));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreGreaterThanExpression(new Expression[0]);
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreGreaterThanExpression(new Expression[0]);
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testNewCoreGreaterThanExpression() {
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreGreaterThanExpression(new Expression[]{left, right});
        assertEquals(left, expr.getLeftExpression());
        assertEquals(right, expr.getRightExpression());
    }

    @Test
    public void testNewCoreGreaterThanOrEqualExpression() {
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreGreaterThanOrEqualExpression(new Expression[]{left, right});
        assertEquals(left, expr.getLeftExpression());
        assertEquals(right, expr.getRightExpression());
    }

    @Test
    public void testNewCoreLessThanExpression() {
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreLessThanExpression(new Expression[]{left, right});
        assertEquals(left, expr.getLeftExpression());
        assertEquals(right, expr.getRightExpression());
    }

    @Test
    public void testNewCoreLessThanOrEqualExpression() {
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperationRelationalExpression expr = new CoreLessThanOrEqualExpression(new Expression[]{left, right});
        assertEquals(left, expr.getLeftExpression());
        assertEquals(right, expr.getRightExpression());
    }
}