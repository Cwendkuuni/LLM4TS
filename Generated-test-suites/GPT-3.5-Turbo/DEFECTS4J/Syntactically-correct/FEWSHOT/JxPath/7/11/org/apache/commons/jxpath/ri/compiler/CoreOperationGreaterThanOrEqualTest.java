package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(MockExpression(2), MockExpression(1));
        assertEquals(Boolean.TRUE, op.computeValue(MockContext()));
        op = new CoreOperationGreaterThanOrEqual(MockExpression(1), MockExpression(2));
        assertEquals(Boolean.FALSE, op.computeValue(MockContext()));
        op = new CoreOperationGreaterThanOrEqual(MockExpression(1), MockExpression(1));
        assertEquals(Boolean.TRUE, op.computeValue(MockContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(MockExpression(1), MockExpression(2));
        assertEquals(">=", op.getSymbol());
    }

    private Expression MockExpression(double value) {
        Expression mock = new Expression() {
            public Object computeValue(EvalContext context) { return 0; }
        };
        return mock;
    }

    private EvalContext MockContext() {
        EvalContext mock = new EvalContext();
        return mock;
    }
}