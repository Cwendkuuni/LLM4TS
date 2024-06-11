package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        // Create test objects
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Create test context
        EvalContext context = new EvalContext();

        // Test case 1: arg1 = 5, arg2 = 3
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        assertEquals(Boolean.TRUE, operationGreaterThan.computeValue(context));

        // Test case 2: arg1 = 3, arg2 = 5
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 5);
        assertEquals(Boolean.FALSE, operationGreaterThan.computeValue(context));

        // Test case 3: arg1 = 5, arg2 = 5
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        assertEquals(Boolean.FALSE, operationGreaterThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operationGreaterThan.getSymbol());
        }
}