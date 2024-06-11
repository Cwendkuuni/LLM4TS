package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.SimpleNode;

public class CoreOperationEqualTest {
    @Test
    public void testCoreOperationEqual_WhenArgumentsAreEqual_ThenResultIsTRUE() {
        // Given
        Expression arg1 = new SimpleNode(1);
        Expression arg2 = new SimpleNode(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_WhenArgumentsAreNotEqual_ThenResultIsFALSE() {
        // Given
        Expression arg1 = new SimpleNode(1);
        Expression arg2 = new SimpleNode(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_WhenArgumentsAreBothNull_ThenResultIsTRUE() {
        // Given
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_WhenFirstArgumentIsNULL_ThenResultIsFALSE() {
        // Given
        Expression arg1 = null;
        Expression arg2 = new SimpleNode(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_WhenSecondArgumentIsNULL_ThenResultIsFALSE() {
        // Given
        Expression arg1 = new SimpleNode(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}