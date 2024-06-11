package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.test.TestBase;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationLessThanOrEqualTest extends TestBase {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        // Basic test case for CoreOperationLessThanOrEqual method
        double l = 5.0;
        double r = 10.0;
        EvalContext context = mock(EvalContext.class);
        when(context.getInfoSet()).thenReturn(createMockInfoSet());
        when(args[0].computeValue(context)).thenReturn(l);
        when(args[1].computeValue(context)).thenReturn(r);

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(args[0], args[1]);
        boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertTrue(result);

        l = 10.0;
        r = 5.0;
        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNaN() {
        // Edge case test for CoreOperationLessThanOrEqual method with NaN values
        EvalContext context = mock(EvalContext.class);
        when(context.getInfoSet()).thenReturn(createMockInfoSet());

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(args[0], args[1]);

        when(args[0].computeValue(context)).thenReturn(Double.NaN);
        when(args[1].computeValue(context)).thenReturn(Double.NaN);

        boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertFalse(result);

        when(args[0].computeValue(context)).thenReturn(-0.0d);
        when(args[1].computeValue(context)).thenReturn(Double.NaN);

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertFalse(result);

        when(args[0].computeValue(context)).thenReturn(Double.NaN);
        when(args[1].computeValue(context)).thenReturn(-0.0d);

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationLessThanOrEqualWithInvalidTypes() {
        // Exception handling test case for CoreOperationLessThanOrEqual method with invalid types
        EvalContext context = mock(EvalContext.class);
        when(context.getInfoSet()).thenReturn(createMockInfoSet());

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(args[0], args[1]);

        when(args[0].computeValue(context)).thenThrow(new ClassCastException());
        when(args[1].computeValue(context)).thenReturn(5.0);

        coreOperationLessThanOrEqual.computeValue(context);
    }
}