// CoreOperationLessThanOrEqualTest.java
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        // Test with valid inputs
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new NodeInfo(infoSet));
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with non-numeric values
        arg1 = new StringLiteralExpression("a");
        arg2 = new StringLiteralExpression("b");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);

        // Test with negative numbers
        arg1 = new LiteralExpression(-1.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with very large numbers
        arg1 = new LiteralExpression(Double.MAX_VALUE);
        arg2 = new LiteralExpression(Double.MAX_VALUE);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue() {
        // Test with valid inputs
        EvalContext context = new EvalContext(new InfoSet());
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with null context
        context = null;
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);

        // Test with null arguments
        arg1 = null;
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);

        // Test with different data types
        arg1 = new IntegerLiteralExpression(1);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with very large numbers
        arg1 = new LiteralExpression(Double.MAX_VALUE);
        arg2 = new LiteralExpression(Double.MAX_VALUE);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}