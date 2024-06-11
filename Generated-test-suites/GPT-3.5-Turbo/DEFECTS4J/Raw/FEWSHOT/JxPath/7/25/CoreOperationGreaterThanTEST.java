package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    private EvalContext createMockContext(double arg1Value, double arg2Value) {
        EvalContext context = new EvalContext(null);
        context.setNodeSet(new Double[]{arg1Value, arg2Value});
        return context;
    }

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock());
        EvalContext context = createMockContext(5.0, 3.0);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Add more test cases for different values
    }

    @Test
    public void testComputeValueLessThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock());
        EvalContext context = createMockContext(3.0, 5.0);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Add more test cases for different values
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock());
        EvalContext context = createMockContext(5.0, 5.0);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Add more test cases for different values
    }
}