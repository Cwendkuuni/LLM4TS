package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual expr;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() throws Exception {
        arg1 = TestUtils.newLiteral(1.0);
        arg2 = TestUtils.newLiteral(2.0);
        expr = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() throws Exception {
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueFalse() throws Exception {
        EvalContext context = new EvalContext();
        arg1 = TestUtils.newLiteral(3.0);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueBothNegative() throws Exception {
        EvalContext context = new EvalContext();
        arg1 = TestUtils.newLiteral(-2.0);
        arg2 = TestUtils.newLiteral(-3.0);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueBothPositive() throws Exception {
        EvalContext context = new EvalContext();
        arg1 = TestUtils.newLiteral(2.0);
        arg2 = TestUtils.newLiteral(1.0);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueBothZero() throws Exception {
        EvalContext context = new EvalContext();
        arg1 = TestUtils.newLiteral(0.0);
        arg2 = TestUtils.newLiteral(0.0);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueArg1NaN() throws Exception {
        EvalContext context = new EvalContext();
        arg1 = TestUtils.newLiteral(Double.NaN);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValueArg2NaN() throws Exception {
        EvalContext context = new EvalContext();
        arg2 = TestUtils.newLiteral(Double.NaN);
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
    }

}