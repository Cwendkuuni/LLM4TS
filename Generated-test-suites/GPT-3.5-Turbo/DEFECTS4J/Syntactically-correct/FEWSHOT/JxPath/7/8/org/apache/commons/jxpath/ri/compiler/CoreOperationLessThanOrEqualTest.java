package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        // add assertions here
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        // add assertions here
    }
}