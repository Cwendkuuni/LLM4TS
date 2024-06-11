package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5),
                new ConstantExpression(10));
        assertFalse((boolean) expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(10),
                new ConstantExpression(5));
        assertTrue((boolean) expression.computeValue(context));

        expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5),
                new ConstantExpression(5));
        assertTrue((boolean) expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5),
                new ConstantExpression(10));
        assertEquals(">=", expression.getSymbol());
    }
}