package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        assertTrue(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(1.0));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        assertTrue(operation.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithDifferentTypes() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression("2"));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThanOrEqual(new ConstantExpression("1"), new ConstantExpression(2.0));
        assertFalse(operation.computeValue(new EvalContext()).booleanValue());

        operation = new CoreOperationLessThanOrEqual(new ConstantExpression("1"), new ConstantExpression("2"));
        assertTrue(operation.computeValue(new EvalContext()).booleanValue());
    }
}