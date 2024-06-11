package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqualWithTwoDoubleValues() {
        Expression arg1 = new CoreNumber(1.5);
        Expression arg2 = new CoreNumber(2.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        assertTrue(operation.computeValue(context).booleanValue());

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(operation.computeValue(context).booleanValue());

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.4);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithMismatchedTypes() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreString("2");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumber(1.5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithLessThanTwoArgs() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual();
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        operation.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithTwoNodeInfos() {
        Expression arg1 = new CoreNode(new NodeInfo(1, "root", null));
        Expression arg2 = new CoreNode(new NodeInfo(2, "root", null));
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        assertFalse(operation.computeValue(context).booleanValue());

        arg1 = new CoreNode(new NodeInfo(1, "root", null));
        arg2 = new CoreNode(new NodeInfo(1, "root", null));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(operation.computeValue(context).booleanValue());

        arg1 = new CoreNode(new NodeInfo(1, "root", null));
        arg2 = new CoreNode(new NodeInfo(2, "root", null));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithTwoNullNodeInfos() {
        Expression arg1 = new CoreNode(null);
        Expression arg2 = new CoreNode(null);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        assertTrue(operation.computeValue(context).booleanValue());
    }
}