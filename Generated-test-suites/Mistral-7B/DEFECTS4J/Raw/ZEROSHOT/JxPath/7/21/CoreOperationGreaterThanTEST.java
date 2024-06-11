package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

/**
 * CoreOperationGreaterThanTest class to test all methods in CoreOperationGreaterThan class
 */
public class CoreOperationGreaterThanTest {

    /**
     * Test constructor CoreOperationGreaterThan
     */
    @Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3.0;
            }
        };
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertNotNull(coreOperationGreaterThan.args);
        assertTrue(coreOperationGreaterThan.args.length == 2);
    }

    /**
     * Test method computeValue
     */
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3.0;
            }
        };
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3.0;
            }
        };
        arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test method getSymbol
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression() {
        };
        Expression arg2 = new Expression() {
        };
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        String symbol = coreOperationGreaterThan.getSymbol();
        assertEquals(">", symbol);
    }
}