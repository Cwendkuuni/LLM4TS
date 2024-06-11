package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithDoubleValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(2.5)),
                new CoreConstantExpression(new Double(3.5)));
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(3.5)),
                new CoreConstantExpression(new Double(2.5)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithNonNumericValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Integer(3)),
                new CoreConstantExpression(new String("3")));
    }

    @Test
    public void testCoreOperationLessThanWithNullValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(3.5)),
                new CoreConstantExpression(null));
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Double(3.5)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(null));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndNegativeValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(0.0)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(0.0)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(0.0)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(-0.0)),
                new CoreConstantExpression(new Double(-5.0)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(-0.0)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanWithNaN() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(Double.NaN)),
                new CoreConstantExpression(new Double(3.5)));
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(3.5)),
                new CoreConstantExpression(new Double(Double.NaN)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(new Double(Double.NaN)),
                new CoreConstantExpression(new Double(Double.NaN)));
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }
}