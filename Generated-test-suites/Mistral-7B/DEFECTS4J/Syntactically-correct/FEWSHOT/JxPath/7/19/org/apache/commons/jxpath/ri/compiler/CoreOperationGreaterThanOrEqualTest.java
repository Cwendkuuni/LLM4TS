package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.test.TestUtils;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(4.0)));

        EvalContext context = TestUtils.createEvalContext(new InfoSet(), new SimpleNode("root"));

        assertTrue(op.computeValue(context).booleanValue());

        context = TestUtils.createEvalContext(new InfoSet(), new SimpleNode("root"));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(4.0)),
                new ConstantExpression(new Double(5.0)));

        assertFalse(op.computeValue(context).booleanValue());

        context = TestUtils.createEvalContext(new InfoSet(), new SimpleNode("root"));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        assertTrue(op.computeValue(context).booleanValue());

        context = TestUtils.createEvalContext(new InfoSet(), new SimpleNode("root"));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double.NaN()),
                new ConstantExpression(new Double.NaN()));

        assertTrue(op.computeValue(context).booleanValue());

        context = TestUtils.createEvalContext(new InfoSet(), new SimpleNode("root"));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double.NaN()),
                new ConstantExpression(new Double(5.0)));

        assertFalse(op.computeValue(context).booleanValue());
    }
}