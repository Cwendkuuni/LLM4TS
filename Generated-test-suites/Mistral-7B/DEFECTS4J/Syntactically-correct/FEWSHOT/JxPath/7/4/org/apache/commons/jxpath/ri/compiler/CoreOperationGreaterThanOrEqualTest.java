package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_true() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5d), new LiteralExpression(5d));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_false() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(4d), new LiteralExpression(5d));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_null() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5d), new LiteralExpression(null));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_differentTypes() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5d), new LiteralExpression("5"));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_doubleValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5.5d), new LiteralExpression(5d));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_floatValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5.5f), new LiteralExpression(5f));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_intValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5), new LiteralExpression(5));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testComputeValue_longValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5L), new LiteralExpression(5L));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(result == Boolean.TRUE);
    }
}