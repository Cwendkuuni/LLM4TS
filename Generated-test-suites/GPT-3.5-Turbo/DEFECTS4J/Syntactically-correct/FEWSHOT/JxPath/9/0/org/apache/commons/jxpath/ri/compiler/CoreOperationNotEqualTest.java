package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(null, null);
        EvalContext context = null;

        // Test with equal arguments
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test with unequal arguments
        expression = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual expression = new CoreOperationNotEqual(null, null);

        assertEquals("!=", expression.getSymbol());
    }
}