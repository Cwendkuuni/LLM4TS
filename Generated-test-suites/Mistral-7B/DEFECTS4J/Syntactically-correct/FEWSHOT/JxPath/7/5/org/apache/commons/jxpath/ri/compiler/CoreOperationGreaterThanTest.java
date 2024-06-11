package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with valid double values
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new Constant(5.0), new Constant(3.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet());
        op = new CoreOperationGreaterThan(new Constant(3.0), new Constant(5.0));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Test with null argument
        context = new EvalContext(new InfoSet());
        op = new CoreOperationGreaterThan(new Constant(null), new Constant(3.0));
        result = op.computeValue(context);
        assertNull(result);

        context = new EvalContext(new InfoSet());
        op = new CoreOperationGreaterThan(new Constant(3.0), new Constant(null));
        result = op.computeValue(context);
        assertNull(result);

        // Test with non-numeric arguments
        context = new EvalContext(new InfoSet());
        op = new CoreOperationGreaterThan(new Constant("5"), new Constant("3"));
        result = op.computeValue(context);
        assertNull(result);

        context = new EvalContext(new InfoSet());
        op = new CoreOperationGreaterThan(new Constant("3"), new Constant("5"));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}