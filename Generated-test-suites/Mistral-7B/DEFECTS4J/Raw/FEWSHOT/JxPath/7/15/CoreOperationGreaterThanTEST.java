package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.TestHelper;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        InfoSet infoSet = TestHelper.createInfoSet("1.0, 2.5, 3.0");
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new VariableExpression("$v1", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 0, null, null, null), null));
        Expression arg2 = new VariableExpression("$v2", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 1, null, null, null), null));
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(arg1, arg2);

        Object result = coreOp.computeValue(context);
        assertTrue("Expected true, but received " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        InfoSet infoSet = TestHelper.createInfoSet("1.0, 2.5, 3.0");
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new VariableExpression("$v1", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 0, null, null, null), null));
        Expression arg2 = new VariableExpression("$v2", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 1, null, null, null), null));
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(arg1, arg2);

        context.setVariable("$v1", 2.5);
        context.setVariable("$v2", 3.0);

        Object result = coreOp.computeValue(context);
        assertFalse("Expected false, but received " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanNull() {
        EvalContext context = new EvalContext(null);
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(new VariableExpression("$v1", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 0, null, null, null), null)), new VariableExpression("$v2", new SimpleNode(TestHelper.createAxis(TestHelper.NAMESPACE_DEFAULT, TestHelper.XPATH_AXIS_NUM, 0, 1, null, null, null), null)));
        assertNull("Expected null, but received " + coreOp.computeValue(context), coreOp.computeValue(context));
    }
}