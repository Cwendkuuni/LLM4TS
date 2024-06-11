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
        Expression arg1 = new CoreNumber(1.5);
        Expression arg2 = new CoreNumber(2.5);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.5);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.0);
        op = new CoreOperationLessThan(arg1, arg2);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}