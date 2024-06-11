package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        InfoSet is1 = new InfoSet();
        InfoSet is2 = new InfoSet();
        EvalContext context = new EvalContext(is1, is2);

        Expression arg1 = new CoreNumber(4.5);
        Expression arg2 = new CoreNumber(5.6);

        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        Object result = lessThan.computeValue(context);
        assertTrue("Expected false", result instanceof Boolean);
        assertTrue("Expected result to be false", ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(1.2);
        arg2 = new CoreNumber(1.2);

        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertFalse("Expected true", result instanceof Boolean);

        arg1 = new CoreNumber(3.1);
        arg2 = new CoreNumber(3.1);

        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertFalse("Expected true", result instanceof Boolean);
        assertFalse("Expected result to be false", ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.7);

        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue("Expected false", result instanceof Boolean);
        assertFalse("Expected result to be true", ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(-1.5);
        arg2 = new CoreNumber(-1.7);

        lessThan = new CoreOperationLessThan(arg1, arg2);

        result = lessThan.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean);
        assertTrue("Expected result to be true", ((Boolean) result).booleanValue());
    }
}