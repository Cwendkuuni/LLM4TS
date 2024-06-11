package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    private CoreOperationGreaterThan operation;

    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(1);
        arg2.setValue(2);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test case 2
        arg1.setValue(2);
        arg2.setValue(1);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}