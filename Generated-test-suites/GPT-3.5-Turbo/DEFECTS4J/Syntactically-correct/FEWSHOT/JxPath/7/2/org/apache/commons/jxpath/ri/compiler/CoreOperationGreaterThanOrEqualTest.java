package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        context.getVariables().declareVariable("arg1", 5);
        context.getVariables().declareVariable("arg2", 10);
        assertEquals(Boolean.FALSE, expr.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        assertEquals(">=", expr.getSymbol());
    }
}