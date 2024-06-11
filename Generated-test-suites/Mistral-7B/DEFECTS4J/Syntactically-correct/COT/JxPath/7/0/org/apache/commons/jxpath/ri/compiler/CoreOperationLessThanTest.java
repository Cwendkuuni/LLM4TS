package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        assertNotNull(coreOperationLessThan);
    }

    @Test
    public void testComputeValue_whenLeftOperandIsLessThanRightOperand_thenReturnTrue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(3.0), new LiteralExpression(5.0));
        EvalContext evalContext = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenLeftOperandIsGreaterThanOrEqualRightOperand_thenReturnFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        EvalContext evalContext = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(evalContext);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenLeftOperandIsNull_thenThrowIllegalArgumentException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(null, new LiteralExpression(3.0));
        EvalContext evalContext = new EvalContext(new InfoSet());
        coreOperationLessThan.computeValue(evalContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenRightOperandIsNull_thenThrowIllegalArgumentException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(5.0), null);
        EvalContext evalContext = new EvalContext(new InfoSet());
        coreOperationLessThan.computeValue(evalContext);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        String symbol = coreOperationLessThan.getSymbol();
        assertEquals("<", symbol);
    }
}