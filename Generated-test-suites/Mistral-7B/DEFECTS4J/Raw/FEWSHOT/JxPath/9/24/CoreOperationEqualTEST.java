package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.JavaToJxPathExpressions;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newLiteralExpression(1),
                JavaToJxPathExpressions.newLiteralExpression(1));
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithBoolean() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newBooleanExpression(true),
                JavaToJxPathExpressions.newBooleanExpression(true));
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithString() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newStringExpression("foo"),
                JavaToJxPathExpressions.newStringExpression("foo"));
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithNumbers() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newNumberExpression(1),
                JavaToJxPathExpressions.newNumberExpression(1));
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypes() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newNumberExpression(1),
                JavaToJxPathExpressions.newStringExpression("1"));
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithNulls() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newNullExpression(),
                JavaToJxPathExpressions.newNullExpression());
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithNullAndNumber() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newNumberExpression(1),
                JavaToJxPathExpressions.newNullExpression());
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperatioEqualWithNullAndString() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newStringExpression("foo"),
                JavaToJxPathExpressions.newNullExpression());
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithNullAndBoolean() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(JavaToJxPathExpressions.newBooleanExpression(true),
                JavaToJxPathExpressions.newNullExpression());
        assertNotNull(coreOperationEqual);
        assertNull(coreOperationEqual.getSymbol());

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}