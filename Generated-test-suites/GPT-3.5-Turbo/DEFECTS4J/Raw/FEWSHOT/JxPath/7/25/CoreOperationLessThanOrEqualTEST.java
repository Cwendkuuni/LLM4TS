
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("10");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("10");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}
