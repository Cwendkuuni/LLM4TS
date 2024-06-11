package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test method for CoreOperationNotEqual.computeValue(EvalContext).
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testComputeValue() {
        Expression arg1 = new Path("$[1]");
        Expression arg2 = new Path("$[2]");
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = notEqual.computeValue(new EvalContext());

        assertFalse("Expected false but got true", result.equals(Boolean.TRUE));
        assertTrue("Expected true but got false", result.equals(Boolean.FALSE));

        // Test with null arguments
        arg1 = null;
        arg2 = null;
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        result = notEqual.computeValue(new EvalContext());
        assertEquals("Expected null but got " + result, null, result);
    }

    /**
     * Test method for CoreOperationNotEqual.getSymbol().
     */
    @Test
    public void testGetSymbol() {
        Expression notEqual = new CoreOperationNotEqual(new Path("$[1]"), new Path("$[2]"));
        String symbol = notEqual.getSymbol();
        assertEquals("Expected != but got " + symbol, "!=", symbol);
    }
}