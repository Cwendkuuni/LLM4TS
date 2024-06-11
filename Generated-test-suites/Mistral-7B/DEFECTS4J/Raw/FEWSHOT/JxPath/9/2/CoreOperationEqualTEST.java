package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Value;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        EqualEvaluator evaluator = new EqualEvaluator();
        EvalContext context = new EvalContext();
        context.setCurrentNode(new CoreNode(evaluator, null));
        Value value = coreOperationEqual.computeValue(context);
        assertTrue(value.isBoolean());
        assertTrue(value.getBooleanValue());

        coreOperationEqual = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        value = coreOperationEqual.computeValue(context);
        assertTrue(value.isBoolean());
        assertFalse(value.getBooleanValue());

        coreOperationEqual = new CoreOperationEqual(new CoreVariableExpression("x"), new CoreVariableExpression("x"));
        context.setVariable("x", new CoreNumberValue(1));
        value = coreOperationEqual.computeValue(context);
        assertTrue(value.isBoolean());
        assertTrue(value.getBooleanValue());

        coreOperationEqual = new CoreOperationEqual(new CoreVariableExpression("x"), new CoreNumberValue(1));
        context.setVariable("x", new CoreNumberValue(1));
        value = coreOperationEqual.computeValue(context);
        assertTrue(value.isBoolean());
        assertTrue(value.getBooleanValue());

        coreOperationEqual = new CoreOperationEqual(new CoreVariableExpression("x"), new CoreNumberValue(2));
        context.setVariable("x", new CoreNumberValue(1));
        value = coreOperationEqual.computeValue(context);
        assertTrue(value.isBoolean());
        assertFalse(value.getBooleanValue());

        coreOperationEqual = new CoreOperationEqual(new CoreConstantExpression(null), new CoreConstantExpression(null));
        value = coreOperationEqual.computeValue(context);
        assertNull(value);
    }
}