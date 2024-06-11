package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(4.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));

        arg1 = new LiteralExpression(4.0);
        arg2 = new LiteralExpression(5.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));

        arg1 = new LiteralExpression(5.0);
        arg2 = new LiteralExpression(5.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithStrings() {
        Expression arg1 = new StringLiteralExpression("10");
        Expression arg2 = new StringLiteralExpression("9");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));

        arg1 = new StringLiteralExpression("9");
        arg2 = new StringLiteralExpression("10");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));

        arg1 = new StringLiteralExpression("10");
        arg2 = new StringLiteralExpression("10");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNull() {
        Expression arg1 = new LiteralExpression((Object) null);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertNull(coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(5.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, new LiteralExpression((Object) null));

        assertNull(coreOperationGreaterThanOrEqual.computeValue(new EvalContext()));
    }
}