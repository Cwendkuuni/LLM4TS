package org.apache.commons.jxpath.ri.compiler.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionNumber;
import org.apache.commons.jxpath.ri.ExpressionString;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        verify(coreOperationNotEqual).setLeftExpression(arg1);
        verify(coreOperationNotEqual).setRightExpression(arg2);
        verifyNoMoreInteractions(coreOperationNotEqual);

        Assert.assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testCoreOperationNotEqualBasicUseCases() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
        when(arg2.computeValue(context)).thenReturn(Boolean.FALSE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = coreOperationNotEqual.computeValue(context);

        verify(coreOperationNotEqual).setLeftExpression(arg1);
        verify(coreOperationNotEqual).setRightExpression(arg2);
        verify(arg1).computeValue(context);
        verify(arg2).computeValue(context);
        verify(coreOperationNotEqual).setEvalContext(context);
        verify(coreOperationNotEqual).setOperatorSymbol("!=");
        verify(coreOperationNotEqual).setType(CoreOperation.TYPE_COMPARE);
        verify(coreOperationNotEqual).setLeftType(arg1.getType());
        verify(coreOperationNotEqual).setRightType(arg2.getType());

        Assert.assertNotNull(result);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualBasicUseCases2() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(context)).thenReturn(Boolean.FALSE);
        when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = coreOperationNotEqual.computeValue(context);

        verify(coreOperationNotEqual).setLeftExpression(arg1);
        verify(coreOperationNotEqual).setRightExpression(arg2);
        verify(arg1).computeValue(context);
        verify(arg2).computeValue(context);
        verify(coreOperationNotEqual).setEvalContext(context);
        verify(coreOperationNotEqual).setOperatorSymbol("!=");
        verify(coreOperationNotEqual).setType(CoreOperation.TYPE_COMPARE);
        verify(coreOperationNotEqual).setLeftType(arg1.getType());
        verify(coreOperationNotEqual).setRightType(arg2.getType());

        Assert.assertNotNull(result);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationNotEqualNullArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, mock(Expression.class));
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationNotEqualNullArguments2() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(mock(Expression.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullEvalContext() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(mock(Expression.class), mock(Expression.class));
        coreOperationNotEqual.setEvalContext(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualTypeMismatch() {
        Expression arg1 = mock(ExpressionNumber.class);
        Expression arg2 = mock(ExpressionString.class);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqualTypeMismatch_HandleTypeMismatch() {
        Expression arg1 = mock(ExpressionNumber.class);
        Expression arg2 = mock(ExpressionString.class);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        try {
            coreOperationNotEqual.computeValue(mock(EvalContext.class));
            fail("An IllegalArgumentException should have been thrown!");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationNotEqualEquals() {
        Expression arg1 = mock(ExpressionNumber.class);
        Expression arg2 = mock(ExpressionNumber.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(Boolean.TRUE);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(Boolean.TRUE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = coreOperationNotEqual.computeValue(mock(EvalContext.class));

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualNotEquals() {
        Expression arg1 = mock(ExpressionNumber.class);
        Expression arg2 = mock(ExpressionNumber.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(Boolean.TRUE);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(Boolean.FALSE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = coreOperationNotEqual.computeValue(mock(EvalContext.class));

        Assert.assertEquals(Boolean.TRUE, result);
    }
}