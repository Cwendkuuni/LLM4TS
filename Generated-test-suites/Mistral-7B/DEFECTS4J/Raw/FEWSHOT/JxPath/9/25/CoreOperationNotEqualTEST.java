package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new Path("1");
        Expression arg2 = new Path("2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        arg1 = new Path("1");
        arg2 = new Path("1");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        arg1 = new Path("true");
        arg2 = new Path("false");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        arg1 = new Path("false");
        arg2 = new Path("true");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}