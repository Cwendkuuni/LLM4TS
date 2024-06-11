
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new CoreOperationCompare(null, null);
        Expression arg2 = new CoreOperationCompare(null, null);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(notEqual);
        assertEquals("!=", notEqual.getSymbol());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationCompare(null, null);
        Expression arg2 = new CoreOperationCompare(null, null);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        Object value = notEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
    }

}
