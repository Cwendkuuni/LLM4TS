package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(4.0));
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(new Double(4.0));
        arg2 = new LiteralExpression(new Double(5.0));
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(new Double(4.0));
        arg2 = new LiteralExpression(new Double(4.0));
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with int values
       arg1 = new LiteralExpression(new Integer(5));
        arg2 = new LiteralExpression(new Integer(4));
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(new Integer(4));
        arg2 = new LiteralExpression(new Integer(5));
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(new Integer(4));
        arg2 = new LiteralExpression(new Integer(4));
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with boolean values
        arg1 = new LiteralExpression(Boolean.TRUE);
        arg2 = new LiteralExpression(Boolean.FALSE);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(Boolean.FALSE);
        arg2 = new LiteralExpression(Boolean.TRUE);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(Boolean.FALSE);
        arg2 = new LiteralExpression(Boolean.FALSE);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with null values
        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(null);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

}