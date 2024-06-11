package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.2), new ConstantExpression(2.3));
        EvalContext context = TestSupport.createEvalContext();

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.FALSE);

        context = TestSupport.createEvalContext();
        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.2), new ConstantExpression(1.2));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);

        context = TestSupport.createEvalContext();
        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.2), new ConstantExpression(1.3));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);

        context = TestSupport.createEvalContext();
        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Integer.MIN_VALUE), new ConstantExpression(2.3));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);

        context = TestSupport.createEvalContext();
        op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(Integer.MIN_VALUE), new ConstantExpression(Integer.MIN_VALUE));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);

        context = TestSupport.createEvalContext();
        op = new CoreOperationLessThanOrEqual(
                new VariableExpression("x"), new ConstantExpression(3.14));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result == Boolean.TRUE);

        context = TestSupport.createEvalContext();
        context.setVariable("x", new Double(3.14));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result == Boolean.TRUE);
    }
}