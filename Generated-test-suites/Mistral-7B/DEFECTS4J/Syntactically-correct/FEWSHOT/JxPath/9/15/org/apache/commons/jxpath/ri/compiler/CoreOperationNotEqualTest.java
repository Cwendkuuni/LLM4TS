package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Constant;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("1"), new Constant("2"));
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.FALSE);

        notEqual = new CoreOperationNotEqual(new Constant("2"), new Constant("1"));
        result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);

        notEqual = new CoreOperationNotEqual(new Constant("1"), new Constant("1"));
        result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result == Boolean.TRUE);
    }

    @Test
    public void testCoreOperationNotEqualNull() {
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new Constant("1"), null);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.FALSE);

        notEqual = new CoreOperationNotEqual(null, new Constant("1"));
        result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);
    }
}