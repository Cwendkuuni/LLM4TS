package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual_basic() {
        CoreOperationLessThanOrEqual express1 = new CoreOperationLessThanOrEqual(
                new Const(1.0), new Const(2.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = express1.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        express1 = new CoreOperationLessThanOrEqual(new Const(1.0), new Const(1.0));
        result = express1.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationLessThanOrEqual_invalidArguments() {
        CoreOperationLessThanOrEqual express1 = new CoreOperationLessThanOrEqual(
                null, new Const(1.0));
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationLessThanOrEqual_invalidArguments2() {
        CoreOperationLessThanOrEqual express1 = new CoreOperationLessThanOrEqual(
                new Const(1.0), null);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_edgeCases() {
        CoreOperationLessThanOrEqual express1 = new CoreOperationLessThanOrEqual(
                new Const(0.0), new Const(0.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = express1.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        express1 = new CoreOperationLessThanOrEqual(new Const(0.0), new Const(-1.0));
        result = express1.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        express1 = new CoreOperationLessThanOrEqual(new Const(1.0), new Const(0.0));
        result = express1.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationLessThanOrEqual_exceptions() {
        CoreOperationLessThanOrEqual express1 = new CoreOperationLessThanOrEqual(
                new Const(1.0), new Const(1.0));

        // Test with null EvalContext
        try {
            express1.computeValue(null);
            assertFalse("IllegalStateException expected but not thrown.");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test with null InfoSet
        InfoSet infoSet = null;
        EvalContext context = new EvalContext(infoSet);
        try {
            express1.computeValue(context);
            assertFalse("IllegalArgumentException expected but not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with non-numeric arguments
        Expressions expressions = new Expressions();
        Expression arg1 = expressions.string("a");
        Expression arg2 = expressions.number(1.0);
        CoreOperationLessThanOrEqual express2 = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet2 = new InfoSet();
        EvalContext context2 = new EvalContext(infoSet2);
        try {
            express2.computeValue(context2);
            assertFalse("ClassCastException expected but not thrown.");
        } catch (ClassCastException e) {
            // Expected
        }

        // Test with NaN arguments
        Expression arg3 = expressions.number(Double.NaN);
        Expression arg4 = expressions.number(Double.NaN);
        CoreOperationLessThanOrEqual express3 = new CoreOperationLessThanOrEqual(arg3, arg4);
        InfoSet infoSet3 = new InfoSet();
        EvalContext context3 = new EvalContext(infoSet3);
        Object result3 = express3.computeValue(context3);
        assertTrue(result3 instanceof Boolean);
        assertNotNull(result3);
    }

}