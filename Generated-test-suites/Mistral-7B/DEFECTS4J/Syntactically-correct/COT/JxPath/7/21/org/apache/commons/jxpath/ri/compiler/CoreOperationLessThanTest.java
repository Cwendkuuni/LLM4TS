package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.functions.FunctionCall;
import org.junit.Test;

/**
 * Testing class for CoreOperationLessThan
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new FunctionCall(new Path("/test"), "number", new Object[] { 5.0 });
        Expression arg2 = new FunctionCall(new Path("/test"), "number", new Object[] { 10.0 });
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), new NodeInfo())).booleanValue());
    }

    @Test
    public void testCoreOperationLessThan_false() {
        Expression arg1 = new FunctionCall(new Path("/test"), "number", new Object[] { 10.0 });
        Expression arg2 = new FunctionCall(new Path("/test"), "number", new Object[] { 5.0 });
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), new NodeInfo())).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThan_invalidArguments() {
        Expression arg1 = new CoreOperationRelationalExpression(new Expression[] { null, new Expression() });
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, new Expression());
    }

    @Test
    public void testCoreOperationLessThan_zeroAndNegativeNumber() {
        Expression arg1 = new FunctionCall(new Path("/test"), "number", new Object[] { 0.0 });
        Expression arg2 = new FunctionCall(new Path("/test"), "number", new Object[] { -1.0 });
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), new NodeInfo())).booleanValue());
    }

    @Test
    public void testCoreOperationLessThan_zeroAndNonNegativeNumber() {
        Expression arg1 = new FunctionCall(new Path("/test"), "number", new Object[] { 0.0 });
        Expression arg2 = new FunctionCall(new Path("/test"), "number", new Object[] { 5.0 });
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(new EvalContext(new InfoSet(), new NodeInfo())).booleanValue());
    }
}