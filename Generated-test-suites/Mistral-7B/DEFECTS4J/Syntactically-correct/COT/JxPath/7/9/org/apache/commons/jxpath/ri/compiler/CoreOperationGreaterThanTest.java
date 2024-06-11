package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.test.TestUtil;

import java.math.BigDecimal;

public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = TestUtil.createConstantExpression(new BigDecimal("1.0"));
        Expression arg2 = TestUtil.createConstantExpression(new BigDecimal("2.0"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanComputeValue_true() {
        Expression arg1 = TestUtil.createConstantExpression(new BigDecimal("3.0"));
        Expression arg2 = TestUtil.createConstantExpression(new BigDecimal("2.0"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtil.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanComputeValue_false() {
        Expression arg1 = TestUtil.createConstantExpression(new BigDecimal("2.0"));
        Expression arg2 = TestUtil.createConstantExpression(new BigDecimal("3.0"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtil.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanComputeValue_null() {
        Expression arg1 = TestUtil.createConstantExpression(null);
        Expression arg2 = TestUtil.createConstantExpression(new BigDecimal("2.0"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtil.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanComputeValue_nullAndNull() {
        Expression arg1 = TestUtil.createConstantExpression(null);
        Expression arg2 = TestUtil.createConstantExpression(null);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtil.createEvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanConstructor_invalidArguments() {
        Expression arg1 = new CoreOperationRelationalExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        Expression arg2 = new CoreOperationRelationalExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        new CoreOperationGreaterThan(arg1, arg2);
    }
}