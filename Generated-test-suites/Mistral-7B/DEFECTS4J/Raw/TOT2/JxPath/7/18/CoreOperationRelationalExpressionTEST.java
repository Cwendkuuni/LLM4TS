
package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(3));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(3));
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testGreaterThanWithEqualValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(5));
        boolean result = expression.evaluate(null, new Object[0]);
        assertFalse(result);
    }

    @Test
    public void testGreaterThanOrEqualWithEqualValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        boolean result = expression.evaluate(null, new Object[0]);
        assertTrue(result);
    }

    @Test
    public void testLessThanWithEqualValues() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new ConstantExpression(5), new ConstantExpression(5));
        boolean result = expression.evaluate(null, new Object[0]);
        assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualWithEqualValues() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        boolean result = expression.evaluate(null, new Object[0]);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionConstructionWithNullArguments() {
        Expression[] args = null;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionConstructionWithInvalidArguments() {
        Expression[] args = {null, new CoreOperationRelationalExpression(new Expression[0])};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    }
}
