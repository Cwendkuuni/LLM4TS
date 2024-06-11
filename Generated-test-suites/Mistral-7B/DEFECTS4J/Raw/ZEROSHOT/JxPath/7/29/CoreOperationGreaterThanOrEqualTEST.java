package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetBuilder;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.FunctionContext;
import org.apache.commons.jxpath.ri.Functions;
import org.apache.commons.jxpath.ri.FunctionsContext;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new NumberNode(new Double(5.0), null);
        Expression arg2 = new NumberNode(new Double(4.0), null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new FunctionContext(new FunctionsContext(Functions.EMPTY_FUNCTION_CONTEXT), null), new InfoSetBuilder().buildInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals("Expected result to be true", Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualFalse() {
        Expression arg1 = new NumberNode(new Double(4.0), null);
        Expression arg2 = new NumberNode(new Double(5.0), null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new FunctionContext(new FunctionsContext(Functions.EMPTY_FUNCTION_CONTEXT), null), new InfoSetBuilder().buildInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals("Expected result to be false", Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualEqual() {
        Expression arg1 = new NumberNode(new Double(5.0), null);
        Expression arg2 = new NumberNode(new Double(5.0), null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new FunctionContext(new FunctionsContext(Functions.EMPTY_FUNCTION_CONTEXT), null), new InfoSetBuilder().buildInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals("Expected result to be true", Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualZero() {
        Expression arg1 = new NumberNode(new Double(0.0), null);
        Expression arg2 = new NumberNode(new Double(5.0), null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new FunctionContext(new FunctionsContext(Functions.EMPTY_FUNCTION_CONTEXT), null), new InfoSetBuilder().buildInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals("Expected result to be false", Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualZeroZero() {
        Expression arg1 = new NumberNode(new Double(0.0), null);
        Expression arg2 = new NumberNode(new Double(0.0), null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new FunctionContext(new FunctionsContext(Functions.EMPTY_FUNCTION_CONTEXT), null), new InfoSetBuilder().buildInfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals("Expected result to be true", Boolean.TRUE, result);
    }
}