package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new CoreNumber(1.5);
        Expression arg2 = new CoreNumber(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSetMethodsStub(), new String[""], new Object[0]);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        Expression arg1 = new CoreNumber(1.5);
        Expression arg2 = new CoreNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSetMethodsStub(), new String[""], new Object[0]);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNull() {
        Expression arg1 = new CoreNumber(1.5);
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSetMethodsStub(), new String[""], new Object[0]);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }
}

class InfoSetMethodsStub implements InfoSetUtil {
    @Override
    public double doubleValue(Object node) {
        throw new UnsupportedOperationException();
    }
}