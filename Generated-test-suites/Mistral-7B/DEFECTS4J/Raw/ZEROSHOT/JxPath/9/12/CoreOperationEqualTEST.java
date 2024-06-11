package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg1;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg2;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.test.ExpressionTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationEqual class.
 */
public class CoreOperationEqualTest extends ExpressionTestBase {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationEqualArg1(1), new CoreOperationEqualArg2(1));
        assertNotNull(coreOperationEqual);
        assertEquals("=", coreOperationEqual.getSymbol());
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        // Test with different values
        coreOperationEqual = new CoreOperationEqual(new CoreOperationEqualArg1(1), new CoreOperationEqualArg2(2));
        result = coreOperationEqual.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));
        assertFalse(Boolean.FALSE.equals(result));
        assertEquals(Boolean.FALSE, result);

        // Test with null values
        coreOperationEqual = new CoreOperationEqual(new CoreOperationEqualArg1(null), new CoreOperationEqualArg2(null));
        result = coreOperationEqual.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        coreOperationEqual = new CoreOperationEqual(new CoreOperationEqualArg1(null), new CoreOperationEqualArg2(new Object()));
        result = coreOperationEqual.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));
        assertFalse(Boolean.FALSE.equals(result));
        assertEquals(Boolean.FALSE, result);

        coreOperationEqual = new CoreOperationEqual(new CoreOperationEqualArg1(new Object()), new CoreOperationEqualArg2(null));
        result = coreOperationEqual.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));
        assertFalse(Boolean.FALSE.equals(result));
        assertEquals(Boolean.FALSE, result);
    }
}