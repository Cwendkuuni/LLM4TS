package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.JXPathContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual testObject;

    @Test
    public void testComputeValue_withValuesGreaterThanOrEqual() {
        double l = 10.0;
        double r = 5.0;
        EvalContext context = mock(EvalContext.class);
        when(context.computeValue(any())).thenReturn(l, r);

        testObject = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));
        Object result = testObject.computeValue(context);

        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void testComputeValue_withValuesNotGreaterThanOrEqual() {
        double l = 5.0;
        double r = 10.0;
        EvalContext context = mock(EvalContext.class);
        when(context.computeValue(any())).thenReturn(l, r);

        testObject = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));
        Object result = testObject.computeValue(context);

        assertEquals(result, Boolean.FALSE);
    }

    @Test
    public void testGetSymbol() {
        String expectedSymbol = ">=";

        testObject = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));
        String result = testObject.getSymbol();

        assertEquals(result, expectedSymbol);
    }
}