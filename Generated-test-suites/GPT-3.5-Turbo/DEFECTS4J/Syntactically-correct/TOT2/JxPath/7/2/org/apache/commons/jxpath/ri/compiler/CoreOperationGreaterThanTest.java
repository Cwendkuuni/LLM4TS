package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5); // Edge case: arg1 is a constant
        Expression arg2 = new Constant(3); // Edge case: arg2 is a constant
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new TestEvalContext();

        // Typical use case
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Edge case: arg1 and arg2 are equal
        arg1 = new Constant(10);
        arg2 = new Constant(10);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);

        // Error scenario: arg1 is null
        arg1 = null;
        arg2 = new Constant(3);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        try {
            result = operation.computeValue(context);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Typical use case
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);

        // Edge case: arg1 and arg2 are equal
        arg1 = new Constant(10);
        arg2 = new Constant(10);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
    
    // Test method signature correctness
    @Test
    public void testConstructor() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(operation.getArguments());
        assertEquals(arg1, operation.getArguments()[0]);
        assertEquals(arg2, operation.getArguments()[1]);
    }
    
    private static class TestEvalContext extends EvalContext {
        @Override
        public boolean isEarley() {
            return false;
        }
    }

}