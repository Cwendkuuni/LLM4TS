package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.apache.commons.jxpath.ri.test.TestHelper;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Tests for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationLessThan.getClass());
        assertEquals(2, coreOperationLessThan.getArity());
        assertEquals(arg1, coreOperationLessThan.getArg(0));
        assertEquals(arg2, coreOperationLessThan.getArg(1));
    }

    @Test
    public void testComputeValue_true() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestHelper.createEvalContext();
        InfoSet infoSet = TestHelper.createInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_false() {
        Expression arg1 = TestHelper.createNumberExpression(2.0);
        Expression arg2 = TestHelper.createNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestHelper.createEvalContext();
        InfoSet infoSet = TestHelper.createInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_nullArg1() {
        Expression arg1 = null;
        Expression arg2 = TestHelper.createNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestHelper.createEvalContext();
        InfoSet infoSet = TestHelper.createInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullArg2() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestHelper.createEvalContext();
        InfoSet infoSet = TestHelper.createInfoSet();
        context.setInfoSet(infoSet);
        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testComputeValue_sameNumber() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestHelper.createEvalContext();
        InfoSet infoSet = TestHelper.createInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertEquals(null, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = TestHelper.createNumberExpression(1.0);
        Expression arg2 = TestHelper.createNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}