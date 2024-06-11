package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(3.0),
                new CoreNumericConstantExpression(5.0));
        assertNotNull(operation);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(3.0),
                new CoreNumericConstantExpression(5.0));
        assertTrue(operation.computeValue(context) instanceof Boolean);
        assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(5.0),
                new CoreNumericConstantExpression(3.0));
        assertTrue(operation.computeValue(context) instanceof Boolean);
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValueNull() {
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(null),
                new CoreNumericConstantExpression(3.0));
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testComputeValueNullArg2() {
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(3.0),
                new CoreNumericConstantExpression(null));
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericConstantExpression(3.0),
                new CoreNumericConstantExpression(5.0));
        assertEquals("<=", operation.getSymbol());
    }
}