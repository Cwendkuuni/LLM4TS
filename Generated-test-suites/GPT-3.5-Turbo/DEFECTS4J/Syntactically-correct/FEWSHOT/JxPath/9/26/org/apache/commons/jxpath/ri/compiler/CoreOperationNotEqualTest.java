package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        // Test when arg1 and arg2 are equal
        arg1.setValue(5);
        arg2.setValue(5);
        assertFalse(op.computeValue(null));

        // Test when arg1 and arg2 are not equal
        arg1.setValue(5);
        arg2.setValue(10);
        assertTrue(op.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual op = new CoreOperationNotEqual(arg1, arg2);

        String symbol = op.getSymbol();
        assertEquals("!=", symbol);
    }
}