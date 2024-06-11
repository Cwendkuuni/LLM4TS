package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Basic test case
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(1.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(2.0));

        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Assert.assertTrue(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with arguments of the same value
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(2.0));
        Assert.assertTrue(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with arguments in reverse order
        Expression arg1Reverse = mock(Expression.class);
        Expression arg2Reverse = mock(Expression.class);
        when(arg1Reverse.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(2.0));
        when(arg2Reverse.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(1.0));

        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqualReverse = new CoreOperationGreaterThanOrEqual(arg2Reverse, arg1Reverse);
        Assert.assertTrue(coreOperationGreaterThanOrEqualReverse.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with one argument as NaN
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(Double.NaN));
        Assert.assertFalse(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with both arguments as NaN
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(Double.NaN));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(Double.NaN));
        Assert.assertFalse(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with one argument as negative number and another as positive number
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(-1.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(1.0));
        Assert.assertFalse(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with one argument as positive number and another as negative number
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(1.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(-1.0));
        Assert.assertFalse(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));

        // Test with arguments of different types
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet("1.0"));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new InfoSet(2.0));
        Assert.assertFalse(coreOperationGreaterThanOrEqual.computeValue(mock(EvalContext.class)).equals(Boolean.TRUE));
    }
}