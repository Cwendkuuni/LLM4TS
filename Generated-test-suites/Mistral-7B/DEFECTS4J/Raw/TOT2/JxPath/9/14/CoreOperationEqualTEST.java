package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.LiteralExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_true() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("1"));

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_false() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("2"));

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_nullArg1() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, new LiteralExpression("1"));

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertNull(result); // NullPointerException should be thrown
    }

    @Test
    public void testComputeValue_nullArg2() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), null);

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertNull(result); // NullPointerException should be thrown
    }

    @Test
    public void testComputeValue_differentDataTypes() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression(1));

        // When & Then
        assertFalse(coreOperationEqual.isComparable());
        assertNull(coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_differentComparisonResults() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("2"));

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_sameComparisonResults() {
        // Given
        EvalContext context = new EvalContext();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("1"));

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertEquals(Boolean.TRUE, result);
    }
}