package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));
        EvalContext context = TestSupport.createEvalContext();
        assertTrue(op.computeValue(context).booleanValue());

        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(6.0)));
        assertFalse(op.computeValue(context).booleanValue());

        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));
        assertTrue(op.computeValue(context).booleanValue());

        op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"),
                new ConstantExpression(new Double(5.0)));
        EvalContext ctx = TestSupport.createEvalContext();
        ctx.setVariable("x", new Double(6.0));
        assertFalse(op.computeValue(ctx).booleanValue());

        ctx.setVariable("x", new Double(4.0));
        assertTrue(op.computeValue(ctx).booleanValue());
    }
}