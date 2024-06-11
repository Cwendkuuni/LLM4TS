package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueTrue() {
        InfoSet is = createInfoSet(new Object[] { 5.0, 5.0 });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(5.0));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueFalse() {
        InfoSet is = createInfoSet(new Object[] { 4.0, 5.0 });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.0), new ConstantExpression(5.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueNull() {
        InfoSet is = createInfoSet(new Object[] { null, 5.0 });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(null), new ConstantExpression(5.0));
        assertFalse(operation.computeValue(context) instanceof Boolean);
    }

    @Test
    public void testComputeValueMixedNull() {
        InfoSet is = createInfoSet(new Object[] { 4.0, null });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.0), new ConstantExpression(null));
        assertFalse(operation.computeValue(context) instanceof Boolean);
    }

    @Test
    public void testComputeValueTypesMismatch() {
        InfoSet is = createInfoSet(new Object[] { 5.0, "5" });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression("5"));
        assertFalse(operation.computeValue(context) instanceof Boolean);
    }

    @Test
    public void testComputeValueTrueWithNaN() {
        InfoSet is = createInfoSet(new Object[] { Double.NaN, Double.NaN });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueFalseWithNaN() {
        InfoSet is = createInfoSet(new Object[] { Double.NaN, 5.0 });
        EvalContext context = new EvalContext(is);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(Double.NaN), new ConstantExpression(5.0));
        assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
    }

    private InfoSet createInfoSet(Object[] objects) {
        InfoSet is = new InfoSet();
        for (Object o : objects) {
            is.addNode(new NodeInfoImpl("node", o));
        }
        return is;
    }
}