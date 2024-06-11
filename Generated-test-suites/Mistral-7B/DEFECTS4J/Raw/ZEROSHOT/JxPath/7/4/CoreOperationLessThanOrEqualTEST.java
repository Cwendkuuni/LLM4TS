package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithDoubleValues() {
        // Test less than or equal with valid double values
        Expression arg1 = new LiteralExpression("3.0", null);
        Expression arg2 = new LiteralExpression("3.0", null);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression("3.0", null);
        arg2 = new LiteralExpression("4.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("4.0", null);
        arg2 = new LiteralExpression("3.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("3.0", null);
        arg2 = new LiteralExpression("4.5", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("0.0", null);
        arg2 = new LiteralExpression("0.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression("0.0", null);
        arg2 = new LiteralExpression("5.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testLessThanOrEqualWithMixedTypeValues() {
        // Test less than or equal with mixed type values
        Expression arg1 = new LiteralExpression("3.0", null);
        Expression arg2 = new LiteralExpression("3", null);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression("3.0", null);
        arg2 = new LiteralExpression("4.5", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("3", null);
        arg2 = new LiteralExpression("3.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("3", null);
        arg2 = new LiteralExpression("4", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("3.0", null);
        arg2 = new LiteralExpression("true", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression("true", null);
        arg2 = new LiteralExpression("3.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testLessThanOrEqualWithNullValues() {
        // Test less than or equal with null values
        Expression arg1 = new LiteralExpression("3.0", null);
        Expression arg2 = new LiteralExpression(null, null);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression(null, null);
        arg2 = new LiteralExpression("3.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        arg1 = new LiteralExpression(null, null);
        arg2 = new LiteralExpression(null, null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithComplexValues() {
        // Test less than or equal with complex values
        Expression arg1 = new VariableExpression("x", null);
        Expression arg2 = new LiteralExpression("3.0", null);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        infoSet.setItem(0, new Double(2.5));
        context.setInfoSet(infoSet);
        Object result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        infoSet.setItem(0, new Double(3.0));
        context.setInfoSet(infoSet);
        result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new VariableExpression("x", null);
        arg2 = new VariableExpression("y", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = new InfoSet();
        context = new EvalContext(infoSet);
        infoSet.setItem(0, new Double(2.5));
        infoSet.setItem(1, new Double(3.0));
        context.setInfoSet(infoSet);
        result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);

        infoSet.setItem(0, new Double(3.0));
        infoSet.setItem(1, new Double(3.0));
        context.setInfoSet(infoSet);
        result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new VariableExpression("x", null);
        arg2 = new LiteralExpression("3.0", null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        infoSet = new InfoSet();
        context = new EvalContext(infoSet);
        infoSet.setItem(0, new Double(2.5));
        context.setInfoSet(infoSet);
        try {
            lessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLessThanOrEqualWithArityMismatch() {
        // Test less than or equal with arity mismatch
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(null, new LiteralExpression("3.0", null));
        EvalContext context = new EvalContext(new InfoSet());
        try {
            lessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        lessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralExpression("3.0", null), null);
        try {
            lessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(null, null);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(relationalExpression, new LiteralExpression("3.0", null));
        try {
            lessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        lessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralExpression("3.0", null), relationalExpression);
        try {
            lessThanOrEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}