package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCreateGreaterThan() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression greaterThan = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(arg1), arg2);
        assertNotNull(greaterThan);
        assertEquals("(1) > (2)", greaterThan.toString());
        assertFalse(greaterThan.isSymmetric());
        assertFalse(greaterThan.evaluate(new Object[]{new Object[]{arg1, arg2}})[0]);
        assertNull(greaterThan.getLeftOperand());
        assertNull(greaterThan.getRightOperand());
    }

    @Test
    public void testCreateGreaterThanOrEqual() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(greaterThanOrEqual);
        assertEquals("(1) >= (2)", greaterThanOrEqual.toString());
        assertFalse(greaterThanOrEqual.isSymmetric());
        assertTrue(greaterThanOrEqual.evaluate(new Object[]{new Object[]{arg1, arg2}})[0]);
        assertNull(greaterThanOrEqual.getLeftOperand());
        assertNull(greaterThanOrEqual.getRightOperand());
    }

    @Test
    public void testCreateLessThan() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression lessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(lessThan);
        assertEquals("(1) < (2)", lessThan.toString());
        assertFalse(lessThan.isSymmetric());
        assertFalse(lessThan.evaluate(new Object[]{new Object[]{arg1, arg2}})[0]);
        assertNull(lessThan.getLeftOperand());
        assertNull(lessThan.getRightOperand());
    }

    @Test
    public void testCreateLessThanOrEqual() {
        Expression arg1 = new NumberValue(1);
        Expression arg2 = new NumberValue(2);
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(lessThanOrEqual);
        assertEquals("(1) <= (2)", lessThanOrEqual.toString());
        assertFalse(lessThanOrEqual.isSymmetric());
        assertTrue(lessThanOrEqual.evaluate(new Object[]{new Object[]{arg1, arg2}})[0]);
        assertNull(lessThanOrEqual.getLeftOperand());
        assertNull(lessThanOrEqual.getRightOperand());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression testExpression = new CoreOperationGreaterThanOrEqual(new NumberValue(1), new NumberValue(2));
        assertEquals(3, testExpression.getPrecedence());
    }

}