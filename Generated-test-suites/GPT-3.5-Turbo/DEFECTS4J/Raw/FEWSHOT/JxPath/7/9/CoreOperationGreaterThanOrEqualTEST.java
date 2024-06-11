package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);

        // Test case 1
        expression.setArgs(new Expression[] { new Literal("2"), new Literal("1") });
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test case 2
        expression.setArgs(new Expression[] { new Literal("2"), new Literal("2") });
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test case 3
        expression.setArgs(new Expression[] { new Literal("1"), new Literal("2") });
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test case 4
        expression.setArgs(new Expression[] { new Literal("1.5"), new Literal("1.5") });
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test case 5
        try {
            expression.setArgs(new Expression[] { new Literal(null), new Literal("1") });
            expression.computeValue(context);
            fail("Expected JXPathException to be thrown");
        } catch (JXPathException e) {
            assertEquals("Left operand cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", expression.getSymbol());
    }
}