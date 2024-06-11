package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean() {
        // Basic test case
        CoreOperationNotEqual operation = new CoreOperationNotEqual(Boolean.TRUE, Boolean.FALSE);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Edge cases
        operation = new CoreOperationNotEqual(null, null);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationNotEqual(null, Boolean.TRUE);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        operation = new CoreOperationNotEqual(Boolean.TRUE, null);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        operation = new CoreOperationNotEqual(Integer.valueOf(1), Double.valueOf(1.0));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Exception handling
        operation = new CoreOperationNotEqual(new Integer(1), new String("Hello"));
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}