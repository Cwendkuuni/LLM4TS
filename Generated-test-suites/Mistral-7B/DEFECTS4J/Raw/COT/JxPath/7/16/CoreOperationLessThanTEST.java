package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanConstruction() {
        Expression arg1 = new ConstExpr(1.0);
        Expression arg2 = new ConstExpr(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
    }

    @Test
    public void testComputeValue_true_case() {
        Expression arg1 = new ConstExpr(1.0);
        Expression arg2 = new ConstExpr(2.1);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_false_case() {
        Expression arg1 = new ConstExpr(1.0);
        Expression arg2 = new ConstExpr(1.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_invalidArgType() {
        Expression arg1 = new ConstExpr(1);
        Expression arg2 = new ConstExpr(2.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        coreOperationLessThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullArg() {
        Expression arg1 = null;
        Expression arg2 = new ConstExpr(2.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testComputeValue_sameValues() {
        Expression arg1 = new ConstExpr(1.0);
        Expression arg2 = new ConstExpr(1.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValue_equalValues_within_tolerance() {
        Expression arg1 = new ConstExpr(1.00001);
        Expression arg2 = new ConstExpr(1.00002);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValue_equalValues_beyond_tolerance() {
        Expression arg1 = new ConstExpr(1.00001);
        Expression arg2 = new ConstExpr(1.00003);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}