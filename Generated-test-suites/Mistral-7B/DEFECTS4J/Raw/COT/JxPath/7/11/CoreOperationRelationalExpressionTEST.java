package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstruct() {
        Expression[] args = new Expression[2];
        CoreOperation op1 = new CoreOperationMock();
        CoreOperation op2 = new CoreOperationMock();
        args[0] = op1;
        args[1] = op2;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals("CoreOperationRelationalExpression", expression.getClass().getName());
        assertSame(args, expression.getArgs());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test methods with valid arguments.
     */
    @Test
    public void testMethods() {
        Expression op1 = new CoreOperationMock();
        Expression op2 = new CoreOperationMock();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { op1, op2 });
        Object result = expression.evaluate(new Object[] { op1.getValue(), op2.getValue() });
        // Test greater than (>)
        assertTrue(expression.isGreaterThan());
        assertNull(result);
        op1.setValue(5);
        op2.setValue(3);
        result = expression.evaluate(new Object[] { op1.getValue(), op2.getValue() });
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        // Test greater than or equal to (>=)
        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] { op1, op2 });
        op1.setValue(5);
        op2.setValue(3);
        result = expression.evaluate(new Object[] { op1.getValue(), op2.getValue() });
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        op2.setValue(5);
        result = expression.evaluate(new Object[] { op1.getValue(), op2.getValue() });
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test methods with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMethodsWithInvalidArguments() {
        Expression op1 = new CoreOperationMock();
        Expression op2 = new CoreOperationMock();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { op2, op1 });
    }

    /**
     * Test methods with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testMethodsWithNullArguments() {
        Expression op1 = null;
        Expression op2 = new CoreOperationMock();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { op1, op2 });
    }

    /**
     * Test methods with different types of arguments.
     */
    @Test(expected = ClassCastException.class)
    public void testMethodsWithDifferentTypesOfArguments() {
        Expression op1 = new CoreOperationString("5");
        Expression op2 = new CoreOperationNumber(5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] { op1, op2 });
    }

}