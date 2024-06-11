package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
        assertEquals("<", coreOperationLessThan.getSymbol());

        EvalContext context = new EvalContext(InfoSetUtil.createInfoSet(), null);
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(InfoSetUtil.createInfoSet(), null);
        coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(2.0), new ConstantExpression(1.0));
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
        result = coreOperationLessThan.computeValue(context);
        assertNull(result);

        coreOperationLessThan = new CoreOperationLessThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }
}