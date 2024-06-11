package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.nodes.NumberNode;

public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new NumberNode(2.5);
        Expression arg2 = new NumberNode(1.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanTypeMismatch() {
        Expression arg1 = new NumberNode(2.5);
        Expression arg2 = new StringNode("2.5");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        coreOperationGreaterThan.computeValue(context);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanSameValue() {
        Expression arg1 = new NumberNode(2.5);
        Expression arg2 = new NumberNode(2.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanZeroAndNegativeNumber() {
        Expression arg1 = new NumberNode(-1.5);
        Expression arg2 = new NumberNode(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanZeroAndZeroNumber() {
        Expression arg1 = new NumberNode(0.5);
        Expression arg2 = new NumberNode(0.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanNegativeAndPositiveNumber() {
        Expression arg1 = new NumberNode(-2.5);
        Expression arg2 = new NumberNode(1.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanWithNullArguments() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreNullExpression();
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanNullArgumentsAtRuntime() {
        Expression arg1 = null;
        Expression arg2 = new CoreNullExpression();
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);
        coreOperationGreaterThan.computeValue(context);
    }
}