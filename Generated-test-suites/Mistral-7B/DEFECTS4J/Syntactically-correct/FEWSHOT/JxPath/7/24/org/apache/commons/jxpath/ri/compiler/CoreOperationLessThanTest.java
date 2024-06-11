package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanTest {
    @Test
    public void testCoreOperationLessThan() {
        // Test with double values
        EvalContext context = new EvalContext(new InfoSet());
        double l = 3.5;
        double r = 5.2;
        assertTrue(CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), new ConstantExpression(r) }).computeValue(context).equals(Boolean.TRUE));

        l = 3.5;
        r = 3.5;
        assertFalse(CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), new ConstantExpression(r) }).computeValue(context).equals(Boolean.TRUE));

        l = 5.2;
        r = 3.5;
        assertFalse(CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), new ConstantExpression(r) }).computeValue(context).equals(Boolean.TRUE));

        l = 5.2;
        r = 5.2;
        assertFalse(CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), new ConstantExpression(r) }).computeValue(context).equals(Boolean.TRUE));

        // Test with null values
        context = new EvalContext(new InfoSet());
        l = InfoSetUtil.doubleValue(null);
        r = InfoSetUtil.doubleValue(null);
        assertNull(CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), new ConstantExpression(r) }).computeValue(context));

        // Test with invalid arguments
        context = new EvalContext(new InfoSet());
        try {
            CoreOperationLessThan.class.newInstance(new Expression[0]);
            assert false : "Exception expected.";
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            CoreOperationLessThan.class.newInstance(new Expression[] { new ConstantExpression(l), null });
            assert false : "Exception expected.";
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            CoreOperationLessThan.class.newInstance(new Expression[] { null, new ConstantExpression(r) });
            assert false : "Exception expected.";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}