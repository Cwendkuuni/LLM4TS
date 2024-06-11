package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    private CoreOperationLessThanOrEqual expression;

    @Before
    public void setup() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<=", expression.getSymbol());
    }
}