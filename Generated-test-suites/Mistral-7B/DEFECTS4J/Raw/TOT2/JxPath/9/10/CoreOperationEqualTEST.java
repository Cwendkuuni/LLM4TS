package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_true() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_false() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(2));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_null() {
        CoreOperationEqual operation = new CoreOperationEqual(new NullExpression(), new NullExpression());
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_stringAndString() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression("Hello"), new LiteralExpression("Hello"));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_stringAndNull() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression("Hello"), new NullExpression());
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_nullAndString() {
        CoreOperationEqual operation = new CoreOperationEqual(new NullExpression(), new LiteralExpression("Hello"));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_nullAndNull() {
        CoreOperationEqual operation = new CoreOperationEqual(new NullExpression(), new NullExpression());
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_literalAndExpression() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression("1"));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_expressionAndLiteral() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression(1));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_differentTypes() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression('A'));
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_invalidArguments() {
        try {
            new CoreOperationEqual(null, new LiteralExpression(1));
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new CoreOperationEqual(new LiteralExpression("Hello"), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationEqual_NaN() {
        CoreOperationEqual operation = new CoreOperationEqual(new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_invalidComparison() {
        try {
            new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(">"));
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationEqual_exception() {
        try {
            new CoreOperationEqual(new LiteralExpression(new Object()), new LiteralExpression(new Object()));
            fail("RuntimeException expected.");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}