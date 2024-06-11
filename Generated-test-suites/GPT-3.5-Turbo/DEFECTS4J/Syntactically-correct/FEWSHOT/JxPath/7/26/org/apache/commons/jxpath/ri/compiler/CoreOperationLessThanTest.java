
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(null);
        Expression arg1 = new CoreOperationLessThan(arg1, arg2);
        Expression arg2 = new CoreOperationLessThan(arg1, arg2);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreOperationLessThan(arg1, arg2);
        Expression arg2 = new CoreOperationLessThan(arg1, arg2);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}
