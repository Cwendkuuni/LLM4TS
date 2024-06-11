package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(3.5), new ConstantExpression(4.5));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("Expected false", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(3.5), new ConstantExpression(3.5));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(3.5), new ConstantExpression(2.5));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("Expected true", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(null), new ConstantExpression(3.5));
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull("Expected null", result);
    }
}