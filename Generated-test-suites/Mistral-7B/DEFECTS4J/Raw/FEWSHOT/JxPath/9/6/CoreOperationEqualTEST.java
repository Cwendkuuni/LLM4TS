package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Values;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = Values.createNumber(1);
        Expression arg2 = Values.createNumber(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createString("foo");
        arg2 = Values.createString("foo");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createNumber(1);
        arg2 = Values.createNumber(2);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createString("foo");
        arg2 = Values.createNumber(1);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createNull();
        arg2 = Values.createNull();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createNumber(1);
        arg2 = Values.createNull();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        arg1 = Values.createString("foo");
        arg2 = null;
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }
}