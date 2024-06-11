package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression("foo");
        Expression arg2 = new ConstantExpression("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
        
        Expression arg3 = new ConstantExpression("foo");
        Expression arg4 = new ConstantExpression("bar");
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg3, arg4);
        EvalContext context2 = new EvalContext();
        assertEquals(Boolean.FALSE, coreOperationEqual2.computeValue(context2));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new ConstantExpression("foo");
        Expression arg2 = new ConstantExpression("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}