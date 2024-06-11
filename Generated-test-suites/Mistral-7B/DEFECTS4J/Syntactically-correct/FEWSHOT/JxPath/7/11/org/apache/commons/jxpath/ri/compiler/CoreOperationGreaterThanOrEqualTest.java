package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelper;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = TestHelper.createNumberExpression("1.0");
        Expression arg2 = TestHelper.createNumberExpression("2.0");
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg2 = TestHelper.createNumberExpression("1.0");
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestHelper.createNumberExpression("2.0");
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNull() {
        Expression arg1 = TestHelper.createNullExpression();
        Expression arg2 = TestHelper.createNumberExpression("1.0");
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);
        assertNull(result);

        arg1 = TestHelper.createNumberExpression("1.0");
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        Expression arg1 = TestHelper.createNumberExpression("NaN");
        Expression arg2 = TestHelper.createNumberExpression("1.0");
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = TestHelper.createNumberExpression("1.0");
        arg2 = TestHelper.createNumberExpression("NaN");
        op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithInfinity() {
        Expression arg1 = TestHelper.createNumberExpression("Double.POSITIVE_INFINITY");
        Expression arg2 = TestHelper.createNumberExpression("1.0");
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestHelper.createContext();

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = TestHelper.createNumberExpression("1.0");
        arg2 = TestHelper.createNumberExpression("Double.POSITIVE_INFINITY");
        op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}