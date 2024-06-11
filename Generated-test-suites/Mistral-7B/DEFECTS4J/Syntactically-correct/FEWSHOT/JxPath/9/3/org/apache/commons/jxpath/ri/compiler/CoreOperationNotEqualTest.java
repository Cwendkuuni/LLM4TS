package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Value;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualTrue() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualFalse() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(null));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualNullLiteral() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new NullLiteralExpression());
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualBoolean() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(true), new ConstantExpression(false));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualBooleanFalse() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(false), new ConstantExpression(true));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualString() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("foo"), new ConstantExpression("bar"));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualStringEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("foo"), new ConstantExpression("foo"));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualNumber() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualNumberEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualList() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ListExpression(new Object[]{1, 2}), new ListExpression(new Object[]{2, 3}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualListEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ListExpression(new Object[]{1, 2}), new ListExpression(new Object[]{1, 2}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualMap() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MapExpression(new Object[]{"key1", "value1"}), new MapExpression(new Object[]{"key1", "value2"}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualMapKeyEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MapExpression(new Object[]{"key1", "value1"}), new MapExpression(new Object[]{"key1", "value1"}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualMapValueEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MapExpression(new Object[]{"key1", "value1"}), new MapExpression(new Object[]{"key1", "value1"}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertFalse((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualMapKeyNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MapExpression(new Object[]{"key1", "value1"}), new MapExpression(new Object[]{"null", "value2"}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }

    @Test
    public void testCoreOperationNotEqualMapValueNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new MapExpression(new Object[]{"key1", "value1"}), new MapExpression(new Object[]{"key1", null}));
        EvalContext context = new EvalContext();
        Value result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertTrue((boolean) result.getValue());
    }
}