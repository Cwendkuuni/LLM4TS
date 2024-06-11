package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(1.5), new CoreConstant(1.0));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithDoubleValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(2.5), new CoreConstant(1.5));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithMixedValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(2.5), new CoreNumber(1.5));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(1.5), null);
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNaN() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(Double.NaN), new CoreConstant(1.0));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithInf() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(Double.POSITIVE_INFINITY), new CoreConstant(1.0));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeInf() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(Double.NEGATIVE_INFINITY), new CoreConstant(1.0));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithStringValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreString("2.5"), new CoreString("1.5"));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithDifferentTypes() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreString("2.5"), new CoreNumber(1.5));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZeroValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new CoreConstant(0), new CoreConstant(0));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[] {});
        Object result = operation.computeValue(context);
        assertNull(result);
    }
}