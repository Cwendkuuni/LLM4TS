package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testEqualWithBooleanValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(Boolean.TRUE, null);
        NodeInfo arg2 = new NodeInfo(Boolean.TRUE, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualWithStringValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo("foo", null);
        NodeInfo arg2 = new NodeInfo("foo", null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualWithNumberValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(3.14, null);
        NodeInfo arg2 = new NodeInfo(3.14, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualWithNullValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(null, null);
        NodeInfo arg2 = new NodeInfo(null, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualWithBooleanValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(Boolean.TRUE, null);
        NodeInfo arg2 = new NodeInfo(Boolean.FALSE, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualWithStringValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo("foo", null);
        NodeInfo arg2 = new NodeInfo("bar", null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualWithNumberValues() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(3.14, null);
        NodeInfo arg2 = new NodeInfo(4.2, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualWithNullValuesAndNonNullValue() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(null, null);
        NodeInfo arg2 = new NodeInfo("foo", null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualWithNullValuesAndNullValue() {
        EvalContext context = new EvalContext();
        NodeInfo arg1 = new NodeInfo(null, null);
        NodeInfo arg2 = new NodeInfo(null, null);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result.equals(Boolean.FALSE));
    }
}