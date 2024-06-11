package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;

import java.math.BigDecimal;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new CoreStringConstant("5.0");
        Expression arg2 = new CoreNumberConstant(new BigDecimal("6.0"));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumberConstant(new BigDecimal("3.5"));
        arg2 = new CoreNumberConstant(new BigDecimal("2.5"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumberConstant(new BigDecimal("2.5"));
        arg2 = new CoreNumberConstant(new BigDecimal("3.5"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreNumberConstant(new BigDecimal("1.5"));
        arg2 = new CoreNumberConstant(new BigDecimal("2.5"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumberConstant(new BigDecimal("0.5"));
        arg2 = new CoreNumberConstant(new BigDecimal("0.0"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreStringConstant("5");
        arg2 = new CoreNumberConstant(new BigDecimal("6.0"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumberConstant(new BigDecimal("5.0"));
        arg2 = new CoreStringConstant("6");
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreStringConstant("5.5");
        arg2 = new CoreNumberConstant(new BigDecimal("5.5"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumberConstant(new BigDecimal("5.5"));
        arg2 = new CoreStringConstant("6.5");
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumberConstant(new BigDecimal("5.5"));
        arg2 = new CoreStringConstant("6");
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumberConstant(new BigDecimal("5.5"));
        arg2 = new CoreNumberConstant(new BigDecimal("5.5"));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullArgs() {
        Expression arg1 = new CoreExpressionNull();
        Expression arg2 = new CoreExpressionNull();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullArg1() {
        Expression arg1 = new CoreExpressionNull();
        Expression arg2 = new CoreNumberConstant(new BigDecimal("5.5"));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullArg2() {
        Expression arg1 = new CoreNumberConstant(new BigDecimal("5.5"));
        Expression arg2 = new CoreExpressionNull();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreExpressionNull(), new CoreExpressionNull());
        String symbol = coreOperationLessThan.getSymbol();
        assertEquals("<", symbol);
    }
}