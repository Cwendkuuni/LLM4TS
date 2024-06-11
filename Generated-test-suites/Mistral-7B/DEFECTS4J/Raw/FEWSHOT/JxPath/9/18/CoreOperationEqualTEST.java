package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoConstants;

import java.util.ArrayList;
import java.util.List;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual op = new CoreOperationEqual(new CoreConstant(1), new CoreConstant(1));
        EvalContext context = new EvalContext(new NodeContext(), new ArrayList<>(), new ArrayList<>());

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        op = new CoreOperationEqual(new CoreConstant(1), new CoreConstant(2));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        op = new CoreOperationEqual(new CoreConstant(null), new CoreConstant(null));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        op = new CoreOperationEqual(new CoreConstant("foo"), new CoreConstant("foo"));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        op = new CoreOperationEqual(new CoreVariable(0), new CoreVariable(0));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        op = new CoreOperationEqual(new CoreVariable(0), new CoreConstant(1));
        result = op.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual op = new CoreOperationEqual(new CoreConstant(1), new CoreConstant(1));
        assertEquals("=", op.getSymbol());
    }
}