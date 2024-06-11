package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    private CoreOperationLessThanOrEqual lessThanOrEqual;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object value1 = 5.0;
        Object value2 = 10.0;
        context.setValue("arg1", value1);
        context.setValue("arg2", value2);

        assertEquals(Boolean.TRUE, lessThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<=", lessThanOrEqual.getSymbol());
    }
}