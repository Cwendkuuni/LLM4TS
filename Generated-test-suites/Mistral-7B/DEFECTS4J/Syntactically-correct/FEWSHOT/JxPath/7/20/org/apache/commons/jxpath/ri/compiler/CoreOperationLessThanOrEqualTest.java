package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestBase;

public class CoreOperationLessThanOrEqualTest extends TestBase {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(1.0),
                createNumberExpression(2.0));

        EvalContext ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        Object result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(2.0),
                createNumberExpression(1.0));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);

        ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(1.0),
                createNumberExpression(1.0));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithStrings() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                createStringExpression("1"),
                createStringExpression("2"));

        EvalContext ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        Object result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationLessThanOrEqual(
                createStringExpression("2"),
                createStringExpression("1"));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationLessThanOrEqual(
                createStringExpression("1"),
                createStringExpression("1"));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNulls() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                createNullExpression(),
                createNullExpression());

        EvalContext ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        Object result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationLessThanOrEqual(
                createNullExpression(),
                createNumberExpression(1.0));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(1.0),
                createNullExpression());
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithMixedTypes() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(1.0),
                createStringExpression("2"));

        EvalContext ctx = new EvalContext(new SimpleNode(null, null, null, null, null, null));
        Object result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationLessThanOrEqual(
                createStringExpression("2"),
                createNumberExpression(1.0));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationLessThanOrEqual(
                createNumberExpression(1.0),
                createNumberExpression("1.0"));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqualWithIllegalTypes() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                createStringExpression("1"),
                createBooleanExpression(true));
    }

    private Expression createBooleanExpression(boolean value) {
        return new CoreBooleanExpression(value);
    }

    private Expression createNumberExpression(double value) {
        return new CoreNumberExpression(value);
    }

    private Expression createNullExpression() {
        return new CoreNullExpression();
    }

    private Expression createStringExpression(String value) {
        return new CoreStringExpression(value);
    }
}