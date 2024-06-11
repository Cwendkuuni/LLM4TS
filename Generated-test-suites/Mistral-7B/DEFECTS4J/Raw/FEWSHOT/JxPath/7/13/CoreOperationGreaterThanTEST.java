package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestSupport;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan cog = new CoreOperationGreaterThan(
                new CoreConstantExpression(1.5),
                new CoreConstantExpression(1.0)
        );

        EvalContext context = TestSupport.getDefaultEvalContext();
        Object result = cog.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context = TestSupport.getDefaultEvalContext();
        cog = new CoreOperationGreaterThan(
                new CoreConstantExpression(1.0),
                new CoreConstantExpression(1.5)
        );

        result = cog.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = TestSupport.getDefaultEvalContext();
        cog = new CoreOperationGreaterThan(
                new CoreVariableExpression("x"),
                new CoreConstantExpression(1.0)
        );

        context.setContextItem(new Double[0], 2.0);
        result = cog.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context.setContextItem(new Double[0], 1.0);
        result = cog.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = TestSupport.getDefaultEvalContext();
        cog = new CoreOperationGreaterThan(
                new CoreVariableExpression("x"),
                new CoreVariableExpression("y")
        );

        context.setContextItem(new Double[] { 1.5, 1.0 }, null);
        result = cog.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        context.setContextItem(new Double[] { 1.0, 1.5 }, null);
        result = cog.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context.setContextItem(new Double[] { null, 1.5 }, null);
        result = cog.computeValue(context);
        assertNull(result);

        context.setContextItem(new Double[] { 1.5, null }, null);
        result = cog.computeValue(context);
        assertNull(result);
    }
}