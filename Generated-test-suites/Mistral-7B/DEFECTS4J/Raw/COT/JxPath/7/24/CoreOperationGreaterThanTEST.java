package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;

/**
 * Test cases for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan_TwoNumbers_ReturnTrue_WhenFirstNumberIsGreaterThanSecond() {
        // Given
        CoreExpression arg1 = new SimpleNode(5.0);
        CoreExpression arg2 = new SimpleNode(3.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThan_TwoNumbers_ReturnFalse_WhenFirstNumberIsLessThanOrEqualSecond() {
        // Given
        CoreExpression arg1 = new SimpleNode(3.0);
        CoreExpression arg2 = new SimpleNode(5.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_InvalidArgument_ThrowsIllegalArgumentException() {
        // Given
        CoreExpression arg1 = null;
        CoreExpression arg2 = new SimpleNode(5.0);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);

        // When & Then
        expression.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_InvalidArgument_ThrowsIllegalArgumentException_WhenSecondArgumentIsNull() {
        // Given
        CoreExpression arg1 = new SimpleNode(5.0);
        CoreExpression arg2 = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);

        // When & Then
        expression.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testCoreOperationGreaterThan_TwoStrings_ReturnFalse() {
        // Given
        CoreExpression arg1 = new SimpleNode("5");
        CoreExpression arg2 = new SimpleNode("3");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        // When
        Object result = expression.computeValue(context);

        // Then
        assertFalse(result instanceof Boolean);
    }
}