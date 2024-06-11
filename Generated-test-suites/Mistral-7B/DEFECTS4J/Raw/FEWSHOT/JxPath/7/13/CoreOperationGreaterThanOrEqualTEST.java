package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));

        context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(6.0)));
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));

        context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(5.0)));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeNumbers() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(-3.0)), new CoreConstantExpression(new Double(-2.0)));

        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Double(-3.0)), new CoreConstantExpression(new Double(-4.0)));
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNonDoubleValues() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression("5"), new CoreConstantExpression("6.0"));

        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context) instanceof Boolean);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(5)));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new Integer(5)), new CoreConstantExpression(new Integer(6)));
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }
}