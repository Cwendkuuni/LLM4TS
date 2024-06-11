package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CoreOperationGreaterThanOrEqualTest.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(5.0), new CoreConstantExpression(5.0));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(5.0), new CoreConstantExpression(6.0));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(4.0), new CoreConstantExpression(5.0));
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(Double.MAX_VALUE), new CoreConstantExpression(Double.MAX_VALUE));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(-1.0), new CoreConstantExpression(Double.MIN_VALUE));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        Expression arg1 = new CoreConstantExpression(Double.NaN);
        Expression arg2 = new CoreConstantExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual nanTest = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, nanTest.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(Double.MAX_VALUE), new CoreConstantExpression(-1.0));
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression("5.0"), new CoreConstantExpression("6.0"));
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet())) instanceof Boolean);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidTypes() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(5), new CoreConstantExpression("6"));
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidTypes2() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression("5"), new CoreConstantExpression(6));
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidTypes3() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression("5.0"), new CoreConstantExpression(new CoreConstantExpression(5)));
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidTypes4() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreConstantExpression(new CoreConstantExpression(5)), new CoreConstantExpression("6.0"));
        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }
}