package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_whenValuesAreEqual_returnFalse() {
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertFalse(Boolean.valueOf(result.toString()));
    }

    @Test
    public void testComputeValue_whenValuesAreNotEqual_returnTrue() {
        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(Boolean.valueOf(result.toString()));
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();

        assertEquals("!=", symbol);
    }
}