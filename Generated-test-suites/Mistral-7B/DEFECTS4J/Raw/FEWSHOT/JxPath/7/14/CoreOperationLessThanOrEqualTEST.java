package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqualWithDoubleValues() {
        double l = 5.0;
        double r = 10.0;
        EvalContext context = new EvalContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(l), new ConstantExpression(r));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        l = 10.0;
        r = 5.0;
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        l = 5.0;
        r = 5.0;
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        l = 5.5;
        r = 5.0;
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithMixedTypes() {
        double l = 5.0;
        String r = "5";
        EvalContext context = new EvalContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(l), new StringValueExpression(r));
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        l = 5.0;
        r = "5.0";
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        l = 5.5;
        r = "5.0";
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));

        l = 5.0;
        r = "5.1";
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNull() {
        Double l = null;
        Double r = 5.0;
        EvalContext context = new EvalContext(new InfoSet());
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(l), new ConstantExpression(r));
        assertNull(expression.computeValue(context));

        l = 5.0;
        r = null;
        assertNull(expression.computeValue(context));

        l = null;
        r = null;
        assertNull(expression.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithWrongNumberOfArguments() {
        new CoreOperationLessThanOrEqual();
    }
}