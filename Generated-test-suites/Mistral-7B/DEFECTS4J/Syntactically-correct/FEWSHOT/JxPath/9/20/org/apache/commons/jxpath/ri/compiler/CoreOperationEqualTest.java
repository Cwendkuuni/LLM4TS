package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new SimpleNode("1"), new SimpleNode("1"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new SimpleNode("1"), new SimpleNode("2"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new SimpleNode("'1'"), new SimpleNode("'1'"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new SimpleNode("'1'"), new SimpleNode("'2'"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new SimpleNode("true"), new SimpleNode("true"));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new SimpleNode("true"), new SimpleNode("false"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        Expression arg1 = new SimpleNode("null");
        Expression arg2 = new SimpleNode("null");
        CoreOperationEqual coreOperationEqualNull = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqualNull.computeValue(new EvalContext()));
    }

    private void assertEquals(Boolean expected, CoreOperationEqual coreOperationEqual) {
        assertEquals(expected, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new SimpleNode("1"), null);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(null, new SimpleNode("1"));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(null, null);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }
}