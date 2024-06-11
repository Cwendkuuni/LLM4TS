package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    private CoreOperationLessThanOrEqual operation;

    @Test
    public void testComputeValue_lessThan() {
        EvalContext context = new EvalContext(null);

        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationLessThanOrEqual(new Constant(-5), new Constant(10));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(10));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationLessThanOrEqual(new Constant(10.5), new Constant(10));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_notNumbers() {
        EvalContext context = new EvalContext(null);

        operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant("10"));
        try {
            operation.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
        }

        operation = new CoreOperationLessThanOrEqual(new Constant("a"), new Constant(10));
        try {
            operation.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
        }
    }

    @Test
    public void testGetSymbol() {
        operation = new CoreOperationLessThanOrEqual(null, null);
        assertEquals("<=", operation.getSymbol());
    }
}