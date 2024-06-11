package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext(InfoSetUtil.createMockInfoSet());

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertTrue(result);

        context = new EvalContext(InfoSetUtil.createMockInfoSet());

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.0)),
                new CoreConstantExpression(new Double(5.0)));

        result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertFalse(result);

        context = new EvalContext(InfoSetUtil.createMockInfoSet());

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(5.0)));

        result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNonNumericValues() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new String("5")),
                new CoreConstantExpression(new String("3")));

        EvalContext context = new EvalContext(InfoSetUtil.createMockInfoSet());

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertTrue(result);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new String("3")),
                new CoreConstantExpression(new String("5")));

        result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNullValues() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext(InfoSetUtil.createMockInfoSet());

        assertNull(coreOperationGreaterThan.computeValue(context));

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.0)),
                new CoreConstantExpression(null));

        context = new EvalContext(InfoSetUtil.createMockInfoSet());

        assertNull(coreOperationGreaterThan.computeValue(context));
    }
}