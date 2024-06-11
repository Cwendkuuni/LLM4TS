package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("CoreOperationEqual", coreOperationEqual.getClass().getSimpleName());
        assertSame(arg1, coreOperationEqual.getArg1());
        assertSame(arg2, coreOperationEqual.getArg2());
    }

    @Test
    public void testComputeValue_whenBothArgsAreBooleanAndAreEqual_thenReturnTrue() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvaluationContextMock();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreBooleanAndAreNotEqual_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvaluationContextMock();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreNotBooleans_thenReturnNull() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvaluationContextMock context = new EvaluationContextMock();
        Object result = coreOperationEqual.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArg1IsNull_thenThrowIllegalArgumentException() {
        Expression arg2 = new ExpressionMock();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, arg2);
        EvaluationContextMock context = new EvaluationContextMock();
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArg2IsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new ExpressionMock();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, null);
        EvaluationContextMock context = new EvaluationContextMock();
        coreOperationEqual.computeValue(context);
    }
}